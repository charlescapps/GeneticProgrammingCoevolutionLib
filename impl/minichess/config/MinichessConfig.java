package minichess.config; 

import java.io.BufferedReader;
import java.io.IOException;

import minichess.ai.AiInterface; 

import minichess.heuristic.HeuristicInterface;
import minichess.play.PlayInterface;

public class MinichessConfig {

	/**Strings as they appear in the config file*/
	private static final char COMMENT='#'; 
	private static final String _PLAY_INTERFACE = "PLAY_INTERFACE"; 
	private static final String _WHITE_AI = "WHITE_AI"; 
	private static final String _BLACK_AI = "BLACK_AI"; 
	private static final String _WHITE_HEURISTIC = "WHITE_HEURISTIC"; 
	private static final String _BLACK_HEURISTIC = "BLACK_HEURISTIC"; 
	private static final String _TIME_PER_MOVE = "TIME_PER_MOVE"; 
	private static final String _ITERATIVE_DEEP_MIN_DEPTH = "ITERATIVE_DEEP_MIN_DEPTH"; 
	private static final String _REVERSE_DISPLAY = "REVERSE_DISPLAY"; 

	private static Class<? extends PlayInterface> playInterfaceClass = null;
	private static Class<? extends AiInterface> whitePlayerClass = null; 
	private static Class<? extends AiInterface> blackPlayerClass = null; 
	private static Class<? extends HeuristicInterface> whiteHeuristicClass = null; 
	private static Class<? extends HeuristicInterface> blackHeuristicClass = null; 

	private static long timePerMove; 
	private static int iterativeDeepMinDepth; 

	private static boolean reverseDisplay = false; 

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

			if (tokens[0].equals(_PLAY_INTERFACE)) {
				playInterfaceClass = (Class<? extends PlayInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_WHITE_AI)) {
				whitePlayerClass = (Class<? extends AiInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_BLACK_AI)) {
				blackPlayerClass = (Class<? extends AiInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_WHITE_HEURISTIC)) {
				whiteHeuristicClass = (Class<? extends HeuristicInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_BLACK_HEURISTIC)) {
				blackHeuristicClass = (Class<? extends HeuristicInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_TIME_PER_MOVE)) {
				timePerMove = Long.parseLong(tokens[1]); 
			}
			else if (tokens[0].equals(_ITERATIVE_DEEP_MIN_DEPTH)) {
				iterativeDeepMinDepth = Integer.parseInt(tokens[1]); 
			}
			else if (tokens[0].equals(_REVERSE_DISPLAY)) {
				reverseDisplay = Boolean.parseBoolean(tokens[1]); 
			}
		}
	}

	public static Class<? extends PlayInterface> getPlayInterfaceClass() {
		return playInterfaceClass; 
	}

	public static Class<? extends AiInterface> getWhitePlayerClass() {
		return whitePlayerClass; 
	}

	public static Class<? extends AiInterface> getBlackPlayerClass() {
		return blackPlayerClass; 
	}

	public static Class<? extends HeuristicInterface> getWhiteHeuristicClass() {
		return whiteHeuristicClass;
	}

	public static Class<? extends HeuristicInterface> getBlackHeuristicClass() {
		return blackHeuristicClass;
	}

	public static long getTimePerMove() {
		return timePerMove; 
	}

	public static int getIterativeDeepMinDepth() {
		return iterativeDeepMinDepth; 
	}

	public static boolean reverseDisplay() {
		return reverseDisplay; 
	}

	public static String printConfig() {
		StringBuffer sb = new StringBuffer(); 
		sb.append("GLOBAL MINICHESS CONFIGURATION:\n"); 
		sb.append("WHITE_AI=" + whitePlayerClass.getName() + "\n"); 
		sb.append("BLACK_AI=" + blackPlayerClass.getName() + "\n"); 
		sb.append("WHITE_HEURISTIC=" + whiteHeuristicClass.getName() + "\n"); 
		sb.append("BLACK_HEURISTIC=" + blackHeuristicClass.getName() + "\n"); 
		sb.append("ITERATIVE_DEEP_MIN_DEPTH=" + iterativeDeepMinDepth + "\n"); 
		sb.append("TIME_PER_MOVE=" + timePerMove + "\n"); 
		sb.append("PLAY_INTERFACE_CLASS=" + playInterfaceClass.getName() + "\n"); 

		return sb.toString(); 

	}
}
