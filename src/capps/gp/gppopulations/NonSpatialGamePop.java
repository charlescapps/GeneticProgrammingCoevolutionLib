package capps.gp.gppopulations; 

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import capps.gp.gpcreatures.GPCreature;
import capps.gp.gpcreatures.GameCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public class NonSpatialGamePop extends GPPopulation {
	private List<GameCreature> currentPop; 
	private final int POPSIZE;
    private final int TOURNY_SIZE; 

	private final double[] probDistro; 

	private int numGensSoFar = 1; 

	public NonSpatialGamePop() 
			throws InstantiationException, IllegalAccessException{

		super(); 
		this.POPSIZE = GPConfig.getPopSize(); 
        this.TOURNY_SIZE = GPConfig.getTournySize();

		this.currentPop = new ArrayList<GameCreature>(); 
		for (int i = 0; i < POPSIZE; i++) {
			GameCreature c = (GameCreature)CREATURE_TYPE.newInstance(); 
			c.setId(i); 
			currentPop.add(c); 
		}
		/**Pre-compute Prob(selection) = 2^(-RANK)*/
		probDistro = new double[TOURNY_SIZE]; 
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
		List<GPCreature> newGen = new ArrayList<GPCreature>(); 
		for (GPCreature c: currentPop) {
			newGen.add(getReplacement(c)); 
		}
		for (GPCreature c: newGen) {
            double chanceMutate = RANDGEN.nextDouble(); 

            if (chanceMutate <= GPConfig.getProbMutate())
                c.mutate();

			c.invalidateFitness(); 
        }
		this.setNewestGeneration(newGen); 	
		 
		++numGensSoFar; 
	}

	@Override
	public List<? extends GPCreature> getNewestGeneration() {
		return currentPop;
	}

	/**Compute fitness of each GameCreature by playing against TOURNY_SIZE-1
	 * random opponents. This implies that the opponents for determining
	 * fitness are different than the opponents used for the crossover 
	 * "tournament". For nonspatial game evolution, this seems like the
	 * best method. You have to know the fitness of each creature before
	 * the "getReplacement" phase (selection and crossover.) */
	@Override
	public void computeFitnesses() {

        final int maxIndex = TOURNY_SIZE - 1;
		for (GameCreature c: currentPop) {
			/**Randomly select TOURNY_SIZE-1 opponents*/
			List<GameCreature> pool = new ArrayList<GameCreature>();
			for (int i = 0; i < maxIndex; i++) {
				int randIndex = RANDGEN.nextInt(POPSIZE); 
				pool.add(currentPop.get(randIndex)); 
			}
			/**Play games against each opponent to compute fitness.*/
			c.setOpponents(pool); 
			c.computeFitness(); 
		}

	}

	@Override
	public GPCreature getReplacement(GPCreature current) {
		/**First see if crossover occurs for this creature*/
		double randDouble = RANDGEN.nextDouble(); 
		if (randDouble >= GPConfig.getProbCrossover())
			return current; //do nothing in this case

        GameCreature myGameCreature = (GameCreature)current;

		/**Get the list of opponents that was used to compute fitness*/
		List<GameCreature> pool = myGameCreature.getOpponents(); 
		pool.add(myGameCreature); 

		/**Sort by fitness in ascending order*/
		java.util.Collections.sort(pool);
		//java.util.Collections.reverse(pool); 
		try {
			assert(pool.get(0).getFitness() <= pool.get(1).getFitness()):
				"NonSpatialGamePop: tournament pool not sorted."; 
		}
		catch (InvalidFitnessException e) {}

		randDouble = RANDGEN.nextDouble();
		GameCreature winner = null;

        final int maxIndex = probDistro.length - 1;
		/**Randomly select a winner*/
		for (int i = 0; i <= maxIndex; i++) {
			if (randDouble < probDistro[i]) {
				winner = pool.get(maxIndex - i); 
				break; 
			}
		}
		assert (winner != null) : 
			"NonSpatialGamePop: Failed to select a tournament winner"; 
		
		/**Randomly choose creature to mate with the winner, return offspring*/
		GameCreature randomMate = null; 
        final int winnerID = winner.getId(); 
		do { //Don't allow winner to reproduce with itself
			randomMate = pool.get(RANDGEN.nextInt(pool.size())); 
		} while (randomMate.getId() == winnerID); 

		GameCreature offspring = (GameCreature)winner.getOffspring(randomMate); 
		offspring.setId(current.getId()); 
		return offspring; 
	}

	@Override
	public int getPopSize() {
		return POPSIZE;
	}

	@Override
	public List<String> getLongGenInfo() throws InvalidFitnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setNewestGeneration(List<? extends GPCreature> creatures) {
		this.currentPop = (List<GameCreature>)creatures; 
	}


}
