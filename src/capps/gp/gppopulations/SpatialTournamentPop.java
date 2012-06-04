package capps.gp.gppopulations; 

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public class SpatialTournamentPop extends GPPopulation {
	private GPCreature[][] gridPop; 
	private final static int NUM_NBRS=9; 
	private final int N; //side length of the NxN grid

	private double[] probDistro; 

	public SpatialTournamentPop() 
			throws InstantiationException, IllegalAccessException{

		super(); 

		this.N = GPConfig.getSpatialPopSize(); 

		this.gridPop = new GPCreature[N][N]; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				GPCreature c = CREATURE_TYPE.newInstance(); 
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
		GPCreature[][] newGen = new GPCreature[N][N]; 
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
	public List<GPCreature> getNewestGeneration() {
		List<GPCreature> gridToList = new ArrayList<GPCreature>(); 
		for (GPCreature[] cs: gridPop) 
			for (GPCreature c: cs) 
				gridToList.add(c); 
		return gridToList;
	}

	@Override
	public void computeFitnesses() {
		for (GPCreature[] cs: gridPop) 
			for (GPCreature c: cs)
				c.computeFitness(); 
	}

	@Override 
	protected GPCreature getReplacement(GPCreature c) {
		int row = c.getId() / N; 
		int col = c.getId() % N; 
		return getReplacement(row, col); 
	}

	private GPCreature getReplacement(int r, int c) {
		GPCreature current = gridPop[r][c]; 
		/**First see if crossover occurs for this creature*/
		double randDouble = RANDGEN.nextDouble(); 
		if (randDouble >= GPConfig.getProbCrossover())
			return current; //do nothing in this case

		/**select adjacent opponents*/
		List<GPCreature> pool = new ArrayList<GPCreature>();

		for (int i = r-1; i <= r+1; i++) {
			for (int j = c-1; j <= c+1; j++) {
				int iMod = (i < 0 ? i + N : i % N); //Deal with the fact that % op
				int jMod = (j < 0 ? j + N : j % N); //returns negative values
				assert (gridPop[iMod][jMod] != null) : "NULL entry in gridPop ("
												  + i + "," + j + ")"; 
				pool.add(gridPop[iMod][jMod]); 
				//System.out.println(gridPop[i][j].toString()); 
			}
		}

		/**Sort by fitness in descending order*/
		java.util.Collections.sort(pool);

		randDouble = RANDGEN.nextDouble();
		GPCreature winner = null;

		/**Randomly select a winner*/
		for (int i = 0; i < probDistro.length; i++) {
			if (randDouble < probDistro[i]) {
				winner = pool.get(NUM_NBRS - i - 1); 
				break; 
			}
		}
		assert (winner != null) : 
			"NonSpatialTournamentPop: Failed to select a tournament winner"; 
		
		/**Randomly choose creature to mate with the winner, return offspring*/
		GPCreature randomMate = null; 
        final int winnerID = winner.getId(); 
		do { //Don't allow winner to reproduce with itself
			randomMate = pool.get(RANDGEN.nextInt(NUM_NBRS)); 
		} while (randomMate.getId() == winnerID); 

		/**Randomly choose which 'base' creature to return*/
		//Meh seems to get better results when we keep the winner.
		/*int randChoice = RANDGEN.nextInt() % 2; 
		return (randChoice == 0 ? winner.getOffspring(randomMate) :
				randomMate.getOffspring(winner)); */

		GPCreature offspring = winner.getOffspring(randomMate); 
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
			gridPop[i/N][i%N] = creatures.get(i); 
		
	}



}
