package capps.gp.gppopulations; 

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public abstract class GPPopulation {

	protected final List<String> shortInfo; 
	protected final boolean saveShortInfo; 
	protected final boolean saveLongInfo; 
	protected final Random RANDGEN; 
	protected final Class<? extends GPCreature> CREATURE_TYPE; 

	protected int numGensSoFar=1; 
	protected long RUNNUM; 

	public abstract List<? extends GPCreature> getNewestGeneration(); 
	protected abstract void setNewestGeneration(List<? extends GPCreature> creatures); 
	public abstract void computeFitnesses(); 
	protected abstract GPCreature getReplacement(GPCreature c); 

	public abstract int getPopSize();

	public GPPopulation() {
		this.saveShortInfo = GPConfig.outputShort(); 
		this.saveLongInfo = GPConfig.outputLong();
		if (saveShortInfo) {
			shortInfo = new ArrayList<String>(); 
			shortInfo.add(getShortInfoColumnHeader()); 
		}
		else {
			shortInfo = null; 
		}

		this.RANDGEN = GPConfig.getRandGen(); 
		this.RUNNUM=0; 

		while (RUNNUM <=0)
			this.RUNNUM = RANDGEN.nextLong(); 

		this.CREATURE_TYPE = GPConfig.getCreatureType(); 
	}

	public Class<? extends GPCreature> getCreatureType() {
		return CREATURE_TYPE; 
	}

	public int getGenNum() {
		return numGensSoFar; 
	}

	public long getRunNum() {
		return RUNNUM;
	}

	public long getSeedUsed() {
		return GPConfig.getSeed(); 
	}

	public GPCreature getBestCreature() {
		List<GPCreature> shallowCopy = new ArrayList<GPCreature>
			(this.getNewestGeneration()); 
		java.util.Collections.sort(shallowCopy); 

		return shallowCopy.get(shallowCopy.size() - 1); 
	}

	public void evolveNextGeneration() {
		saveGenInfo(); 
		List<? extends GPCreature> currentGen = getNewestGeneration(); 
		List<GPCreature> newGen = new ArrayList<GPCreature>(); 
		for (GPCreature c: currentGen) {
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

	protected void saveGenInfo() {
		if (saveShortInfo) {
			shortInfo.add(getShortInfoOneGen()); 
		}
	}

	public double getAvgFitnessCurrentGen() {
		double sum=0.0; 
		List<? extends GPCreature> cs = getNewestGeneration(); 
		try {
			for (GPCreature c: cs)
				sum+=c.getFitness(); 
		} catch (InvalidFitnessException e) {
			System.err.println("GPPopulation: getAvgFitnessCurrentGen: "
					+ "Attempt to get invalid fitness."); 
			return -1.0; 
		}

		return sum/(double)cs.size(); 
	}

	public double getStdDevFitnessCurrentGen() {
		double avg = getAvgFitnessCurrentGen(); 
		double sumSquares=0.0; 
		List<? extends GPCreature> cs = getNewestGeneration(); 
		try {
			for (GPCreature c: cs) {
				double diff = avg - c.getFitness(); 
				sumSquares+=diff*diff;
			}
		} catch (InvalidFitnessException e) {
			System.err.println("GPPopulation: getStdDevFitnessCurrentGen: "
					+ "Attempt to get invalid fitness."); 
			return -1.0; 
		}

		return Math.sqrt(sumSquares/(double)cs.size()); 
	}

	public String getHeader() {
		double bestFitness = 0.; 
		try { bestFitness = getBestCreature().getFitness(); }
		catch (InvalidFitnessException e) {
			System.err.println("Must compute fitness before calling " +
					"GPPopulation: getHeader()"); 
			return null; 
		}
		String header = "RUNNUM " + this.getRunNum() + "\n" + 
					"POP_CLASS " + this.getClass().getName() + "\n" +
					"CREAT_CLASS " + this.getCreatureType().getName() + "\n" +
					"POPSIZE " + GPConfig.getPopSize() + "\n" + 
					"NUM_GENS " + GPConfig.getNumGens() + "\n" +
					"SEED " + GPConfig.getSeed() + "\n" +
					"BEST_FITNESS " + String.format("%1$.3f",bestFitness) + "\n" +
					"AVG_FITNESS " + String.format("%1$.3f",getAvgFitnessCurrentGen()) + "\n" +
					"STD_DEV_FITNESS " + String.format("%1$.3f",getStdDevFitnessCurrentGen()) + "\n";
		return header;
	}

	public List<String> getFinalGenInfo() throws InvalidFitnessException {
		List<String> finalGenInfo = new ArrayList<String>(); 
		String colHeader = String.format("%1$-12s", "ID") +
						   String.format("%1$-12s", "FITNESS") +
						   String.format("%1$-12s", "NUM_GENS"); 
		finalGenInfo.add(colHeader); 

		List<? extends GPCreature> currentPop = getNewestGeneration(); 

		for(GPCreature c: currentPop) {
			String info = String.format("%1$-12d",c.getId()) 
						+ String.format("%1$-12.4f",c.getFitness()); 
			finalGenInfo.add(info); 
		}
		return finalGenInfo;
	}

	protected String getShortInfoOneGen() {
		double bestFitness = 0.0; 

		try { 
			bestFitness = getBestCreature().getFitness(); 
		} catch (InvalidFitnessException e) {
			System.err.println("Must compute fitness before calling " +
					"GPPopulation: getShortInfoOneGen()"); 
			return null; 
		}

		/**GEN_NUMBER, POPSIZE, BEST_FITNESS, AVG_FITNESS, STD_DEV_FITNESS*/
		String shortInfoGen = String.format("%1$-12d",this.getGenNum()) + 
					String.format("%1$-12d",getPopSize()) + 
					String.format("%1$-12.3f",bestFitness) +
					String.format("%1$-12.3f",getAvgFitnessCurrentGen()) +
					String.format("%1$-12.3f",getStdDevFitnessCurrentGen());
		
		return shortInfoGen; 
	}

	protected String getShortInfoColumnHeader() {
		String shortInfoHeader = String.format("%1$-12s","GEN_NUM") + 
					String.format("%1$-12s","POP_SIZE") + 
					String.format("%1$-12s","BEST_FIT") +
					String.format("%1$-12s","AVG_FIT") +
					String.format("%1$-12s","STD_DEV_FIT");
		return shortInfoHeader; 
	}

	public List<String> getShortGenInfo() {
		return this.shortInfo; 
	}

	public abstract List<String> getLongGenInfo() throws InvalidFitnessException; 

}
