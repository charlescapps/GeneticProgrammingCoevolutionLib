package capps.gp.gppopulations; 

import java.util.ArrayList;
import java.util.List;

import capps.gp.gpcreatures.GPCreature;
import capps.gp.gpcreatures.GameCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public class SpatialGamePop extends GPPopulation {
	private GameCreature[][] gridPop; 
	private final static int NUM_NBRS=9; 
	private final int N; //side length of the NxN grid

	private final double[] probDistro; 

	public SpatialGamePop() 
			throws InstantiationException, IllegalAccessException{

		super(); 
		this.N = GPConfig.getSpatialPopSize(); 

		this.gridPop = new GameCreature[N][N]; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				GameCreature c = (GameCreature)CREATURE_TYPE.newInstance(); 
				c.setId(i*N + j); 
				gridPop[i][j] = c; 
			}
		}

		/**Pre-compute Prob(selection) = 2^(-RANK)*/
		probDistro = new double[NUM_NBRS]; 
		double cumProb = 0.0; 
		double curProb = 1.0; 
		for (int i = 0; i < probDistro.length; i++) {
			curProb = curProb/2.0; 
			cumProb += curProb; 
			probDistro[i] = cumProb; 
		}

		/**Normalize: last entry of cumulative distro must be 1.0*/
		probDistro[probDistro.length-1] = 1.0; 
	}

	@Override
	public void evolveNextGeneration() {
		saveGenInfo(); 
        GameCreature[][] newGen = new GameCreature[N][N]; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				newGen[i][j] = getReplacement(i, j); 

                double chanceMutate = RANDGEN.nextDouble(); 
                if (chanceMutate <= GPConfig.getProbMutate())
                    newGen[i][j].mutate();

				newGen[i][j].invalidateFitness(); 
			}
		}
		this.gridPop = newGen; 		 
		++numGensSoFar; 
	}

	@Override 
	protected GPCreature getReplacement(GPCreature c) {
		int row = c.getId() / N; 
		int col = c.getId() % N; 
		return getReplacement(row, col); 
	}

	@Override
	public List<? extends GPCreature> getNewestGeneration() {
        List<GPCreature> gridToList = new ArrayList<GPCreature>(); 
		for (GPCreature[] cs: gridPop) 
			for (GPCreature c: cs) 
				gridToList.add(c); 
		return gridToList;
	}

	/**Compute fitness of each GameCreature by playing against TOURNY_SIZE-1
	 * random opponents. This implies that the opponents for determining
	 * fitness are different than the opponents used for the crossover 
	 * "tournament". For nonspatial game evolution, this seems like the
	 * best method. You have to know the fitness of each creature before
	 * the "getReplacement" phase (selection and crossover.) */
	@Override
	public void computeFitnesses() {

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                List<GameCreature> pool = new ArrayList<GameCreature>();

                for (int i = r-1; i <= r+1; i++) {
                    for (int j = c-1; j <= c+1; j++) {
                        int iMod = (i < 0 ? i + N : i % N); //Deal with the fact that % op
                        int jMod = (j < 0 ? j + N : j % N); //returns negative values
                        assert (gridPop[iMod][jMod] != null) : "NULL entry in gridPop ("
                                                          + i + "," + j + ")"; 
                        if (i != 0 || j != 0) 
                            pool.add(gridPop[iMod][jMod]); 
                    }
                }
                /**Play games against each adjacent creature to compute fitness.*/
                gridPop[r][c].setOpponents(pool); 
                gridPop[r][c].computeFitness(); 
            }
        }
	}

	public GameCreature getReplacement(int r, int c) {
        GameCreature current = gridPop[r][c]; 

		/**First see if crossover occurs for this creature*/
		double randDouble = RANDGEN.nextDouble(); 
		if (randDouble >= GPConfig.getProbCrossover())
			return current; //do nothing in this case

		/**Get the list of opponents that was used to compute fitness*/
		List<GameCreature> pool = current.getOpponents(); 
        pool.add(current); 

        java.util.Collections.sort(pool); 

		randDouble = RANDGEN.nextDouble();
		GameCreature winner = null;

		/**Randomly select a winner*/
		for (int i = 0; i < NUM_NBRS; i++) {
			if (randDouble < probDistro[i]) {
				winner = pool.get(NUM_NBRS - i - 1); 
				break; 
			}
		}
		
		/**Randomly choose creature to mate with the winner, return offspring*/
		GameCreature randomMate = null; 
        final int winnerID = winner.getId(); 
		do { //Don't allow winner to reproduce with itself
            int randomId = RANDGEN.nextInt(NUM_NBRS); 
			randomMate = pool.get(randomId); 
		} while (randomMate.getId() == winnerID); 

		GameCreature offspring = (GameCreature)winner.getOffspring(randomMate); 
		offspring.setId(current.getId()); 
		return offspring; 
	}

	@Override
	public int getPopSize() {
		return N*N;
	}

	@Override
	public List<String> getLongGenInfo() throws InvalidFitnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setNewestGeneration(List<? extends GPCreature> creatures) {
		assert(creatures.size() == getPopSize()); 
		int popsize = getPopSize(); 
		for (int i = 0; i < popsize; i++) 
			gridPop[i/N][i%N] = (GameCreature)creatures.get(i); 
	}

}
