package capps.gp.gppopulations; 

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public class NonSpatialTournamentPop implements GPPopulation {
	private List<GPCreature> currentPop; 
	private final int POPSIZE;
	private final long RUNNUM; 
	private final Random RANDGEN;
	private final Class<? extends GPCreature> CREATURE_TYPE; 

	private int numGensSoFar = 1; 

	public NonSpatialTournamentPop(
			Class<? extends GPCreature> creatureType) 
			throws InstantiationException, IllegalAccessException{
		this.POPSIZE = GPConfig.getPopSize(); 
		this.RANDGEN = GPConfig.getRandGen(); 

		this.RUNNUM = RANDGEN.nextLong(); 
		this.CREATURE_TYPE = creatureType; 
		this.currentPop = new ArrayList<GPCreature>(); 
		for (int i = 0; i < POPSIZE; i++) {
			currentPop.add(CREATURE_TYPE.newInstance()); 
		}
	}

	@Override
	public long getSeedUsed() {
		return GPConfig.getSeed();
	}

	@Override
	public List<GPCreature> getNewestGeneration() {
		return currentPop;
	}

	@Override
	public void computeFitnesses() {
		for (GPCreature c: currentPop) {
			c.computeFitness(); 
		}
	}

	@Override
	public void evolveNextGeneration() {
		List<GPCreature> newGen = new ArrayList<GPCreature>(); 
		for (GPCreature c: currentPop) {
			newGen.add(getReplacement(c)); 
		}
		currentPop = newGen; 	
		for (GPCreature c: currentPop) 
			c.invalidateFitness(); 
	}

	private GPCreature getReplacement(GPCreature current) {
		/**First see if crossover occurs for this creature*/
		double randDouble = RANDGEN.nextDouble(); 
		if (randDouble >= GPConfig.getProbCrossover())
			return current; //do nothing in this case

		/**Randomly select TOURNY_SIZE-1 opponents*/
		List<GPCreature> pool = new ArrayList<GPCreature>();
		pool.add(current); 
		int TOURNY_SIZE = GPConfig.getTournySize();
		for (int i = 0; i < TOURNY_SIZE-1; i++) {
			int randIndex = RANDGEN.nextInt(currentPop.size()); 
			pool.add(currentPop.get(randIndex)); 
		}

		/**Sort by fitness in descending order*/
		java.util.Collections.sort(pool);
		java.util.Collections.reverse(pool); 
		try {
			assert(pool.get(0).getFitness() > pool.get(1).getFitness()):
				"NonSpatialTournamentPop: tournament pool not sorted."; 
		}
		catch (InvalidFitnessException e) {}

		/**Prob(selection) = 2^(-RANK)*/
		double[] probDistro = new double[pool.size()]; 
		double cumProb = 0.0; 
		double curProb = 1.0; 
		for (int i = 0; i < probDistro.length; i++) {
			curProb = curProb/2.0; 
			cumProb += curProb; 
			probDistro[i] = cumProb; 
		}

		/**Normalize: last entry of cumulative distro must be 1.0*/
		probDistro[probDistro.length-1] = 1.0; 

		randDouble = RANDGEN.nextDouble();
		GPCreature winner = null;

		/**Randomly select a winner*/
		for (int i = 0; i < probDistro.length; i++) {
			if (randDouble < probDistro[i]) {
				winner = pool.get(i); 
				break; 
			}
		}
		assert (winner != null) : 
			"NonSpatialTournamentPop: Failed to select a tournament winner"; 
		
		/**Randomly choose creature to mate with the winner, return offspring*/
		GPCreature randomMate = pool.get(RANDGEN.nextInt(pool.size())); 
		/**Randomly choose which 'base' creature to return*/
		/*int randChoice = RANDGEN.nextInt() % 2; 
		return (randChoice == 0 ? winner.getOffspring(randomMate) :
				randomMate.getOffspring(winner)); */

		return winner.getOffspring(randomMate); 
	}

	@Override
	public long getRunNum() {
		return RUNNUM;
	}

	@Override
	public int getPopSize() {
		return POPSIZE;
	}

	@Override
	public String getHeader() {
		String header = "RUNNUM " + RUNNUM + 
						"POPSIZE " + POPSIZE + 
						"NUM_GENS " + numGensSoFar +
						"SEED " + GPConfig.getSeed(); 
		return header;
	}

	@Override
	public GPCreature getBestCreature() {
		List<GPCreature> shallowCopy = new ArrayList<GPCreature>(currentPop); 
		java.util.Collections.sort(shallowCopy); 

		return shallowCopy.get(shallowCopy.size() - 1); 
	}

	@Override
	public String getShortGenInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongGenInfo() {
		// TODO Auto-generated method stub
		return null;
	}



}
