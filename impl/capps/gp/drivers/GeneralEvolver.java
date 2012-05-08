package capps.gp.drivers; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import capps.gp.gpcreatures.SinSqCreature;
import capps.gp.gpcreatures.GPCreature;

import capps.gp.gpglobal.GPConfig;

import capps.gp.gppopulations.GPPopulation;
import capps.gp.gppopulations.NonSpatialTournamentPop;

/**
 * Toy example to test that this shit actually works. Evolving a function
 * tree to approximate sin^2(x) in the interval (-2*pi, 2*pi)
 */

public class GeneralEvolver {

	public static String GP_USAGE = "java -jar general_evolver.jar " +
		"<config_filename>";
	private static GPPopulation myPop; 
	private static String configFile;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Error: require exactly 1 argument."); 
			System.out.println(GP_USAGE); 
			System.exit(1); 
		}
		configFile = args[0]; 

		/**Input data from config file. Fixed name here.*/
		GPConfig.loadConfig(new BufferedReader(new FileReader(configFile)));

		myPop = GPConfig.getPopType().newInstance(); 

		int numGens = GPConfig.getNumGens(); 

		GPCreature curBest = null; 
		myPop.computeFitnesses(); 
		for (int i = 0; i < numGens; i++) {

			myPop.evolveNextGeneration(); 
			myPop.computeFitnesses(); 

			curBest = myPop.getBestCreature(); 

			System.out.println("GENERATION NUMBER: " + i); 
			System.out.println("BEST FITNESS     : " 
					+ String.format("%1$,.4f",curBest.getFitness())); 
			System.out.println(); 
		}

		GPCreature best = myPop.getBestCreature(); 
		System.out.println("Best creature height=" + best.getTree().getHeight()); 

		/**Output best creature to dot file*/
		String dot = best.getTree().toDot("Best function"); 
		FileWriter fw = null; 
		fw = new FileWriter(GPConfig.getOutputDir() + "/best.dot"); 
		fw.write(dot); 
		fw.close(); 


		/**Output header file*/
		System.out.println("Writing header to " + GPConfig.getHeaderFile()); 
		fw = new FileWriter(GPConfig.getHeaderFile()); 
		fw.write(myPop.getHeader()); 
		fw.close(); 

		/**Output short info file*/
		System.out.println("Writing short info to " + GPConfig.getShortInfoFile()); 
		fw = new FileWriter(GPConfig.getShortInfoFile()); 
		for (String row: myPop.getShortGenInfo())
			fw.write(row + "\n"); 
		fw.close(); 
	}
}
