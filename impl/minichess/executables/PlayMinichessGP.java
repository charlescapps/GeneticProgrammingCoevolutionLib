package minichess.executables; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import capps.gp.gpcreatures.MinichessCreature;

import capps.gp.gpglobal.GPConfig;

import minichess.COLOR;

import minichess.ai.AiInterface;
import minichess.ai.IterativeDeepeningAI;

import minichess.boards.Board;

import minichess.heuristic.HeuristicInterface;

import minichess.play.*;
import minichess.config.MinichessConfig;

public class PlayMinichessGP {

	public static final String PLAY_MINICHESS_USAGE = 
		"java -jar play_minichess.jar <mc_config_file> <gp_config_file>"; 

	private static String configFileName; 
	private static String gpConfigFileName; 
	private static PlayInterface myPlayInterface; 
	private static AiInterface whitePlayer;
	private static AiInterface blackPlayer;

	public static void main(String[] args) throws Exception {
		parseArgs(args); 

		System.out.println(MinichessConfig.printConfig()); 

		myPlayInterface.play(); 
	}

	private static void parseArgs(String[] args) 
		throws Exception {
		if (args.length != 2) {
			System.out.println("Need exactly 2 arguments.\n" 
					+ PLAY_MINICHESS_USAGE); 
			System.exit(1); 
		}

		/**Parse config file and load into MinichessConfig class*/
		configFileName = args[0]; 
        gpConfigFileName = args[1]; 
		BufferedReader configReader = 
			new BufferedReader(new FileReader(configFileName)); 
		MinichessConfig.loadConfig(configReader); 

        BufferedReader gpConfigReader =
			new BufferedReader(new FileReader(gpConfigFileName)); 
		GPConfig.loadConfig(gpConfigReader); 

		/**Set the play interface from the config file.*/
		Class<? extends PlayInterface> myPlayInterfaceClass 
			= MinichessConfig.getPlayInterfaceClass(); 
		myPlayInterface = myPlayInterfaceClass.newInstance(); 

		/**Instantiate new AI's*/
		Class<? extends AiInterface> whitePlayerClass 
			= MinichessConfig.getWhitePlayerClass(); 
		whitePlayer = whitePlayerClass.newInstance(); 
		whitePlayer.setColor(COLOR.WHITE); 

        Class<? extends HeuristicInterface> wHeur = MinichessConfig.getWhiteHeuristicClass();

        if (GPConfig.getWhiteStringToParse() != null && 
                wHeur.equals(MinichessCreature.class)) {
            whitePlayer.setHeuristic(new MinichessCreature(GPConfig.getWhiteStringToParse())); 

        }
        else {
            whitePlayer.setHeuristic(wHeur.newInstance()); 
        }

		if (IterativeDeepeningAI.class.isInstance(whitePlayer)) {
			IterativeDeepeningAI whiteID = (IterativeDeepeningAI) whitePlayer;
			whiteID.setTimePerMove(MinichessConfig.getTimePerMove()); 
			whiteID.setMinDepth(MinichessConfig.getIterativeDeepMinDepth()); 
		}

		Class<? extends AiInterface> blackPlayerClass 
			= MinichessConfig.getBlackPlayerClass(); 
		blackPlayer = blackPlayerClass.newInstance(); 
		blackPlayer.setColor(COLOR.BLACK); 

        Class<? extends HeuristicInterface> bHeur = MinichessConfig.getBlackHeuristicClass();

        if (GPConfig.getBlackStringToParse() != null && 
                bHeur.equals(MinichessCreature.class)) {
            System.out.println("Setting black heuristic to:"); 
            System.out.println(GPConfig.getBlackStringToParse()); 
            blackPlayer.setHeuristic(new MinichessCreature(GPConfig.getBlackStringToParse())); 

        }
        else {
            blackPlayer.setHeuristic(bHeur.newInstance()); 
        }

		if (IterativeDeepeningAI.class.isInstance(blackPlayer)) {
			IterativeDeepeningAI blackID = (IterativeDeepeningAI) blackPlayer;
			blackID.setTimePerMove(MinichessConfig.getTimePerMove()); 
			blackID.setMinDepth(MinichessConfig.getIterativeDeepMinDepth()); 
		}

        if (MinichessCreature.class.isInstance(blackPlayer.getHeuristic())) {
            BufferedWriter printDotFile = new BufferedWriter(
                    new FileWriter("dots/minichess_tree.dot")); 
            printDotFile.write( 
                ((MinichessCreature)blackPlayer.getHeuristic()).getTree().toDot("Minichess Tree"));

            printDotFile.close(); 

        }

		/**Pass the AI's to the PlayInterface, and give it a default board.*/
		myPlayInterface.setStartBoard(new Board()); 
		myPlayInterface.setWhitePlayer(whitePlayer); 
		myPlayInterface.setBlackPlayer(blackPlayer); 
	}

}
