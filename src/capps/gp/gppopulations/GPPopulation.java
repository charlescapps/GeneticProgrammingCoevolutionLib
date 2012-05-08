package capps.gp.gppopulations; 

import java.util.List;

import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gpglobal.GPConfig;

public abstract class GPPopulation {

	public abstract List<GPCreature> getNewestGeneration(); 
	public abstract GPCreature getBestCreature(); 
	public abstract void computeFitnesses(); 
	public abstract void evolveNextGeneration(); 

	public abstract long getSeedUsed(); 
	public abstract long getRunNum(); 
	public abstract int getPopSize();
	public abstract Class<? extends GPCreature> getCreatureType(); 

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
					"BEST_FITNESS " + String.format("%1$.3f",bestFitness) + "\n";
		return header;
	}

	public abstract List<String> getShortGenInfo() throws InvalidFitnessException; 
	public abstract List<String> getLongGenInfo() throws InvalidFitnessException; 

}
