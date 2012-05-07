package capps.gp.gppopulations; 

import java.util.List;

import capps.gp.gpcreatures.GPCreature;

public interface GPPopulation {

	public List<GPCreature> getNewestGeneration(); 
	public GPCreature getBestCreature(); 
	public void computeFitnesses(); 
	public void evolveNextGeneration(); 

	public long getSeedUsed(); 
	public long getRunNum(); 
	public int getPopSize();

	public String getHeader(); 
	public String getShortGenInfo(); 
	public String getLongGenInfo(); 

}
