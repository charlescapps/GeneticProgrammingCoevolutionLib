package capps.gp.gpglobal; 

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Random;

import capps.gp.gpcreatures.GPCreature;

import capps.gp.gppopulations.GPPopulation;

public class GPConfig {

	/**To avoid passing a GPConfig object to every single bloody
	 * object in the hierarchy, have a static, default option for
	 * generating random numbers. 
	 *
	 * Google Guice solves GPConfig sort of problem but I don't feel like going
	 * that route at the moment. Less dependencies is better. */

	/**Strings as they appear in the config file*/
	private static final char COMMENT = '#'; 
	private static final String _SEED = "SEED"; 
	private static final String _NULL_SEED = "NULL"; 
	private static final String _NUM_GENS = "NUM_GENS"; 
	private static final String _PROB_CROSS = "PROB_CROSS"; 
	private static final String _MAX_FITNESS = "MAX_FITNESS"; 
	private static final String _POP_SIZE = "POP_SIZE"; 
	private static final String _SPATIAL_POP_SIZE = "POP_SIZE_SPATIAL"; 
	private static final String _TOURNY_SIZE = "TOURNY_SIZE"; 
	private static final String _GP_DEPTH = "GP_DEPTH"; 

	private static final String _OUTPUT_DIR = "OUTPUT_DIR"; 
	private static final String _HEADER_FILE = "HEADER_FILE"; 
	private static final String _SHORT_INFO_FILE = "SHORT_INFO_FILE"; 

	private static final String _POP_CLASS = "POP_CLASS"; 
	private static final String _CREATURE_CLASS = "CREATURE_CLASS"; 

	private static Long seed; //if NULL, use System.currentTimeMillis()
	private static Double probCrossover;
	private static Double maxFitness; 
	private static Integer numGens;
	private static Integer popSize;
	private static Integer spatialPopSize;
	private static Integer tournySize;
	private static Integer gpDepth;
	private static Random randGen; 

	private static String outputDir; 
	private static String headerOutputFile; 
	private static String shortInfoFile; 

	private static Class<? extends GPPopulation> popClass; 
	private static Class<? extends GPCreature> creatureClass; 

	/**Spits back out the config file. Questionable usefulness. */
	public static String toConfigFormat() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(_SEED + (seed==null?_NULL_SEED:seed) + "\n"); 
		sb.append(_NUM_GENS + numGens + "\n"); 

		return sb.toString(); 
	}

	public static void loadConfig(BufferedReader configFile)
		throws IOException, ClassNotFoundException {

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
			//CASE "POP_SIZE_SPATIAL"
			else if (tokens[0].equals(_SPATIAL_POP_SIZE)) {
				GPConfig.spatialPopSize = Integer.parseInt(tokens[1]); 
			}
			//CASE "TOURNY_SIZE"
			else if (tokens[0].equals(_TOURNY_SIZE)) {
				GPConfig.tournySize = Integer.parseInt(tokens[1]); 
			}
			//CASE "GP_DEPTH"
			else if (tokens[0].equals(_GP_DEPTH)) {
				GPConfig.gpDepth = Integer.parseInt(tokens[1]); 
			}
			//CASE "OUTPUT_DIR"
			else if (tokens[0].equals(_OUTPUT_DIR)) {
				GPConfig.outputDir = (tokens[1]); 
			}
			//CASE "HEADER_FILE"
			else if (tokens[0].equals(_HEADER_FILE)) {
				GPConfig.headerOutputFile = (tokens[1]); 
			}
			//CASE "SHORT_INFO_FILE"
			else if (tokens[0].equals(_SHORT_INFO_FILE)) {
				GPConfig.shortInfoFile = (tokens[1]); 
			}
			//CASE "POP_CLASS"
			else if (tokens[0].equals(_POP_CLASS)) {
				GPConfig.popClass = (Class<? extends GPPopulation>)Class.forName(tokens[1]); 
			}
			//CASE "CREATURE_CLASS"
			else if (tokens[0].equals(_CREATURE_CLASS)) {
				GPConfig.creatureClass = (Class<? extends GPCreature>)Class.forName(tokens[1]); 
			}
		}

		if (seed == null)
			seed = System.currentTimeMillis(); 

		randGen = new Random(seed); 

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

	public static Integer getSpatialPopSize() {
		return spatialPopSize;
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

	public static String getHeaderFile() {
		return headerOutputFile; 
	}

	public static String getShortInfoFile() {
		return shortInfoFile; 
	}

	public static Class<? extends GPCreature> getCreatureType() {
		return creatureClass; 
	}

	public static Class<? extends GPPopulation> getPopType() {
		return popClass;
	}
}
