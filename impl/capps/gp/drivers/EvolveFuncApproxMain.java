package capps.gp.drivers; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import capps.gp.gpcreatures.FunctionApproxCreature;
import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpglobal.GPConfig;

import capps.gp.gppopulations.NonSpatialTournamentPop;

/**
 * Toy example to test that this shit actually works. Evolving a function
 * tree to approximate sin^2(x) in the interval (-2*pi, 2*pi)
 */

public class EvolveFuncApproxMain {

	private static NonSpatialTournamentPop myPop; 
	private static final String configFile = "configs/nonspatial.gp"; 

	public static void main(String[] args) throws Exception {
		/**Input data from config file. Fixed name here.*/
		GPConfig.loadConfig(new BufferedReader(new FileReader(configFile)));

		myPop = new NonSpatialTournamentPop(
				capps.gp.gpcreatures.FunctionApproxCreature.class); 

		int numGens = GPConfig.getNumGens(); 

		GPCreature curBest = null; 
		for (int i = 0; i < numGens; i++) {
			myPop.computeFitnesses(); 
			curBest = myPop.getBestCreature(); 

			System.out.println("GENERATION NUMBER: " + i); 
			System.out.println("BEST FITNESS     : " 
					+ String.format("%1$,.4f",curBest.getFitness())); 
			System.out.println(); 

			myPop.evolveNextGeneration(); 
		}

		GPCreature best = myPop.getBestCreature(); 
		String dot = best.getTree().toDot("Best function"); 
		FileWriter fw = null; 
		fw = new FileWriter(GPConfig.getOutputDir() + "/best.dot"); 
		fw.write(dot); 
		fw.close(); 

		System.out.println("Best creature height=" + best.getTree().getHeight()); 

		System.out.println(((FunctionApproxCreature)best).printStats()); 
	}
}
