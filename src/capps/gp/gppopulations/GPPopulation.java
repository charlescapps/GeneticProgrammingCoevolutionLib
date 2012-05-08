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

	public double getAvgFitnessCurrentGen() {
		double sum=0.0; 
		List<GPCreature> cs = getNewestGeneration(); 
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
		List<GPCreature> cs = getNewestGeneration(); 
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

	public abstract List<String> getFinalGenInfo() throws InvalidFitnessException; 
	public abstract List<String> getShortGenInfo() throws InvalidFitnessException; 
	public abstract List<String> getLongGenInfo() throws InvalidFitnessException; 

}
