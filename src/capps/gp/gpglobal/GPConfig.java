package capps.gp.gpglobal; 

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Random;

public class GPConfig {

	/**To avoid passing a GPConfig object to every single bloody
	 * object in the hierarchy, have a static, default option for
	 * generating random numbers. 
	 *
	 * Google Guice solves GPConfig sort of problem but I don't feel like going
	 * that route at the moment. Less dependencies is better. */
	public static Random DEFAULT_RANDOM=null; 

	/**Strings as they appear in the config file*/
	private static final char COMMENT = '#'; 
	private static final String _SEED = "SEED"; 
	private static final String _NULL_SEED = "NULL"; 
	private static final String _NUM_GENS = "NUM_GENS"; 
	private static final String _PROB_CROSS = "PROB_CROSS"; 
	private static final String _MAX_FITNESS = "MAX_FITNESS"; 
	private static final String _POP_SIZE = "POP_SIZE"; 
	private static final String _TOURNY_SIZE = "TOURNY_SIZE"; 
	private static final String _OUTPUT_DIR = "OUTPUT_DIR"; 
	private static final String _GP_DEPTH = "GP_DEPTH"; 

	private static Long seed; //if NULL, use System.currentTimeMillis()
	private static Double probCrossover;
	private static Double maxFitness; 
	private static Integer numGens;
	private static Integer popSize;
	private static Integer tournySize;
	private static Integer gpDepth;
	private static Random randGen; 
	private static String outputDir; 

	public static void loadConfig(BufferedReader configFile) throws IOException {

		String line; 

		while ((line = configFile.readLine()) != null) {

			line = line.trim(); 
			String[] tokens = line.split("\\s"); //split on whitespace

			if (tokens.length < 2)
				continue; 

			if (line.charAt(0) == COMMENT)
				continue; 

			//CASE "SEED"
			if (tokens[0].equals(_SEED)) {
				if (tokens[1].equals(_NULL_SEED)) {
					seed = null; 
				}
				else {
					seed = Long.parseLong(tokens[1]); 
				}
			}
			//CASE "NUM_GENS"
			else if (tokens[0].equals(_NUM_GENS)) {
				GPConfig.numGens = Integer.parseInt(tokens[1]); 
				assert(numGens > 0); 
			}
			//CASE "PROB_CROSSOVER"
			else if (tokens[0].equals(_PROB_CROSS)) {
				GPConfig.probCrossover = Double.parseDouble(tokens[1]); 
			}
			//CASE "MAX_FITNESS"
			else if (tokens[0].equals(_MAX_FITNESS)) {
				GPConfig.maxFitness = Double.parseDouble(tokens[1]); 
			}
			//CASE "POP_SIZE"
			else if (tokens[0].equals(_POP_SIZE)) {
				GPConfig.popSize = Integer.parseInt(tokens[1]); 
			}
			//CASE "TOURNY_SIZE"
			else if (tokens[0].equals(_TOURNY_SIZE)) {
				GPConfig.tournySize = Integer.parseInt(tokens[1]); 
			}
			//CASE "OUTPUT_DIR"
			else if (tokens[0].equals(_OUTPUT_DIR)) {
				GPConfig.outputDir = (tokens[1]); 
			}
			//CASE "GP_DEPTH"
			else if (tokens[0].equals(_GP_DEPTH)) {
				GPConfig.gpDepth = Integer.parseInt(tokens[1]); 
			}
		}

		if (seed == null)
			seed = System.currentTimeMillis(); 

		randGen = new Random(System.currentTimeMillis()); 

		if (GPConfig.DEFAULT_RANDOM == null)
			GPConfig.DEFAULT_RANDOM = randGen; 
	}

	public static Long getSeed() {
		return seed;
	}

	public static Double getProbCrossover() {
		return probCrossover; 
	}

	public static Double getMaxFitness() {
		return maxFitness; 
	}

	public static Integer getNumGens() {
		return numGens; 
	}

	public static Integer getPopSize() {
		return popSize;
	}

	public static Integer getTournySize() {
		return tournySize;
	}

	public static Integer getGpDepth() {
		return gpDepth;
	}

	public static Random getRandGen() {
		return randGen;
	}

	public static String getOutputDir() {
		return outputDir; 
	}

}
