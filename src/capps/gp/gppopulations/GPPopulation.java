package capps.gp.gppopulations; 

import java.util.List;

import capps.gp.gpcreatures.GPCreature;

public interface GPPopulation {

	public List<List<GPCreature>> getAllGenerations(); 
	public List<GPCreature> getNewestGeneration(); 
	public void computeFitnesses(); 
	public void getNextGeneration(); 

}
