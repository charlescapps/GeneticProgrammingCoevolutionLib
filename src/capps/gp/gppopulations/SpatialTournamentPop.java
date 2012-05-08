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
	private long RUNNUM; //A random long identifying this run
	private final Random RANDGEN;
	private final Class<? extends GPCreature> CREATURE_TYPE; 

	private double[] probDistro; 

	private int numGensSoFar = 1; 

	public SpatialTournamentPop() 
			throws InstantiationException, IllegalAccessException{
		this.N = GPConfig.getSpatialPopSize(); 
		this.RANDGEN = GPConfig.getRandGen(); 
		this.RUNNUM=0; 

		while (RUNNUM <=0)
			this.RUNNUM = RANDGEN.nextLong(); 

		this.CREATURE_TYPE = GPConfig.getCreatureType(); 
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
	public long getSeedUsed() {
		return GPConfig.getSeed();
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
	public void evolveNextGeneration() {
		GPCreature[][] newGen = new GPCreature[N][N]; 
		for (int i=0; i<N; i++) 
			for (int j = 0; j < N; j++)
				newGen[i][j]=getReplacement(i,j); 

		gridPop = newGen; 	
		for (GPCreature[] cs: gridPop) 
			for (GPCreature c: cs) 
				c.invalidateFitness(); 
		 
		++numGensSoFar; 
	}

	private GPCreature getReplacement(int r, int c) {
		GPCreature current = gridPop[r][c]; 
		/**First see if crossover occurs for this creature*/
		double randDouble = RANDGEN.nextDouble(); 
		if (randDouble >= GPConfig.getProbCrossover())
			return current; //do nothing in this case

		/**Randomly select TOURNY_SIZE-1 opponents*/
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
		java.util.Collections.reverse(pool); 

		try {
			assert(pool.get(0).getFitness() >= pool.get(1).getFitness()):
				"NonSpatialTournamentPop: tournament pool not sorted."; 
		}
		catch (InvalidFitnessException e) {}

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
		GPCreature randomMate = null; 
		do { //Don't allow winner to reproduce with itself
			randomMate = pool.get(RANDGEN.nextInt(pool.size())); 
		} while (randomMate.getId() == winner.getId()); 

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
	public long getRunNum() {
		return RUNNUM;
	}

	@Override
	public int getPopSize() {
		return N*N;
	}


	@Override
	public GPCreature getBestCreature() {
		double bestFitness = Double.MIN_VALUE; 
		GPCreature bestGuy = null; 
		try {
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++)
				if (gridPop[i][j].getFitness() > bestFitness) {
					bestFitness = gridPop[i][j].getFitness(); 
					bestGuy = gridPop[i][j]; 
				}

		} catch (InvalidFitnessException e) {
			System.err.println("Tried to get best creature before computing fitnesses."); 
			return null; 
		}
		return bestGuy; 
	}

	@Override
	public List<String> getShortGenInfo() throws InvalidFitnessException {
		List<String> shortInfo = new ArrayList<String>(); 
		String colHeader = String.format("%1$-12s", "ID") +
						   String.format("%1$-12s", "FITNESS") +
						   String.format("%1$-12s", "NUM_GENS"); 
		shortInfo.add(colHeader); 

		for(GPCreature[] cs: gridPop) {
			for (GPCreature c: cs) {
				String info = String.format("%1$-12d",c.getId()) 
							+ String.format("%1$-12.4f",c.getFitness()); 
				shortInfo.add(info); 
			}
		}
		return shortInfo;
	}

	@Override
	public List<String> getLongGenInfo() throws InvalidFitnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends GPCreature> getCreatureType() {
		// TODO Auto-generated method stub
		return CREATURE_TYPE;
	}



}
