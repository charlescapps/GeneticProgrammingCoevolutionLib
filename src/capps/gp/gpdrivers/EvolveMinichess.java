package capps.gp.gpdrivers; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import capps.gp.gpcreatures.GPCreature;
import capps.gp.gpglobal.GPConfig;
import capps.gp.gppopulations.GPPopulation;
import minichess.config.MinichessConfig;

/**
 * Toy example to test that this shit actually works. Evolving a function
 * tree to approximate sin^2(x) in the interval (-2*pi, 2*pi)
 */

public class EvolveMinichess {

	public static String GP_USAGE = "java -jar evolve_minichess.jar " +
		"<gp_config_filename> <mc_config_filename>";
	private static GPPopulation myPop; 

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Error: require exactly 2 argument."); 
			System.out.println(GP_USAGE); 
			System.exit(1); 
		}

		/**Input data from config file. .*/
		GPConfig.loadConfig(new BufferedReader(new FileReader(args[0])));
        /**Minichess config file just used for initial iterative deepening
         * depth, and time per move*/
		MinichessConfig.loadConfig(new BufferedReader(new FileReader(args[1])));

        System.out.println("Instantiating new population..."); 
		myPop = GPConfig.getPopType().newInstance(); 

		int numGens = GPConfig.getNumGens(); 
        System.out.println("Total generations to compute: " + numGens); 

		GPCreature curBest = null; 
    
        System.out.println("Computing fitness of first generation..."); 
		myPop.computeFitnesses(); 
		for (int i = 0; i < numGens; i++) {

			curBest = myPop.getBestCreature(); 

			System.out.println("GENERATION NUMBER: " + i); 
			System.out.println("BEST FITNESS     : " 
					+ String.format("%1$,.4f",curBest.getFitness())); 
			myPop.evolveNextGeneration(); 
			myPop.computeFitnesses(); 

			System.out.println(); 
		}

		GPCreature best = myPop.getBestCreature(); 
		System.out.println("Best creature height=" + best.getTree().getHeight()); 
		System.out.println("Best fitness=" + best.getFitness()); 

		/**Output best creature to dot file*/
		String dot = best.getTree().toDot("Best creature"); 
		FileWriter fw = null; 
		fw = new FileWriter(GPConfig.getOutputDir() + "/best.dot"); 
		fw.write(dot); 
		fw.close(); 

		/**Output header file*/
        String fullFileName = GPConfig.getOutputDir() + "/" + GPConfig.getHeaderFile();
		System.out.println("Writing header to " + fullFileName); 
		fw = new FileWriter(fullFileName); 
		fw.write(myPop.getHeader()); 
		fw.close(); 

		/**Output short info file*/
		if (GPConfig.outputShort()) {
            fullFileName = GPConfig.getOutputDir() + "/" + GPConfig.getShortInfoFile();
			System.out.println("Writing short info to " + fullFileName); 
			fw = new FileWriter(fullFileName); 
			for (String row: myPop.getShortGenInfo())
				fw.write(row + "\n"); 
			fw.close(); 
		}

		/**Output final info file*/
		if (GPConfig.outputFinalGen()) {
            fullFileName = GPConfig.getOutputDir() + "/" + GPConfig.getFinalGenFile();
			System.out.println("Writing final gen info to " + fullFileName); 
			fw = new FileWriter(fullFileName); 
			for (String row: myPop.getFinalGenInfo())
				fw.write(row + "\n"); 
			fw.close(); 
		}

    }
}
