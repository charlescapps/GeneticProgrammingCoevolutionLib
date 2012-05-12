package minichess.executables; 

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;

import minichess.COLOR;
import minichess.Move;
import minichess.PIECE; 

import minichess.ai.AiInterface;
import minichess.ai.IterativeDeepeningAI;

import minichess.boards.Board;

import minichess.play.*;
import minichess.config.MinichessConfig;

public class PlayMinichessFromConfig {

	public static final String PLAY_MINICHESS_USAGE = 
		"java -jar play_minichess.jar <config_file>"; 

	private static String configFileName; 
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
		if (args.length != 1) {
			System.out.println("Need exactly 1 argument.\n" 
					+ PLAY_MINICHESS_USAGE); 
			System.exit(1); 
		}

		/**Parse config file and load into MinichessConfig class*/
		configFileName = args[0]; 
		BufferedReader configReader = 
			new BufferedReader(new FileReader(configFileName)); 
		MinichessConfig.loadConfig(configReader); 

		/**Set the play interface from the config file.*/
		Class<? extends PlayInterface> myPlayInterfaceClass 
			= MinichessConfig.getPlayInterfaceClass(); 
		myPlayInterface = myPlayInterfaceClass.newInstance(); 

		/**Instantiate new AI's*/
		Class<? extends AiInterface> whitePlayerClass 
			= MinichessConfig.getWhitePlayerClass(); 
		whitePlayer = whitePlayerClass.newInstance(); 
		whitePlayer.setColor(COLOR.WHITE); 
		whitePlayer.setHeuristic(
				MinichessConfig.getWhiteHeuristicClass().newInstance()); 
		if (IterativeDeepeningAI.class.isInstance(whitePlayer)) {
			IterativeDeepeningAI whiteID = (IterativeDeepeningAI) whitePlayer;
			whiteID.setTimePerMove(MinichessConfig.getTimePerMove()); 
			whiteID.setMinDepth(MinichessConfig.getIterativeDeepMinDepth()); 
		}

		Class<? extends AiInterface> blackPlayerClass 
			= MinichessConfig.getBlackPlayerClass(); 
		blackPlayer = blackPlayerClass.newInstance(); 
		blackPlayer.setColor(COLOR.BLACK); 
		blackPlayer.setHeuristic(
				MinichessConfig.getBlackHeuristicClass().newInstance()); 
		if (IterativeDeepeningAI.class.isInstance(blackPlayer)) {
			IterativeDeepeningAI blackID = (IterativeDeepeningAI) blackPlayer;
			blackID.setTimePerMove(MinichessConfig.getTimePerMove()); 
			blackID.setMinDepth(MinichessConfig.getIterativeDeepMinDepth()); 
		}

		/**Pass the AI's to the PlayInterface, and give it a default board.*/
		myPlayInterface.setStartBoard(new Board()); 
		myPlayInterface.setWhitePlayer(whitePlayer); 
		myPlayInterface.setBlackPlayer(blackPlayer); 
	}

}
