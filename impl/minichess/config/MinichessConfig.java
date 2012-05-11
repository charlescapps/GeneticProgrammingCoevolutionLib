package minichess.config; 

import java.io.BufferedReader;
import java.io.IOException;

import minichess.ai.PlayerInterface; 

public class MinichessConfig {

	/**Strings as they appear in the config file*/
	private static final char COMMENT='#'; 
	private static final String _WHITE_AI = "WHITE_AI"; 
	private static final String _BLACK_AI = "BLACK_AI"; 
	private static final String _REVERSE_DISPLAY = "REVERSE_DISPLAY"; 

	private static Class<? extends PlayerInterface> whitePlayerClass = null; 
	private static Class<? extends PlayerInterface> blackPlayerClass = null; 
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

			//CASE "SEED"
			if (tokens[0].equals(_WHITE_AI)) {
				whitePlayerClass = (Class<? extends PlayerInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_BLACK_AI)) {
				blackPlayerClass = (Class<? extends PlayerInterface>)Class.forName(tokens[1]); 
			}
			else if (tokens[0].equals(_REVERSE_DISPLAY)) {
				reverseDisplay = Boolean.parseBoolean(tokens[1]); 
			}
		}
	}

	public Class<? extends PlayerInterface> getWhitePlayerClass() {
		return whitePlayerClass; 
	}

	public Class<? extends PlayerInterface> getBlackPlayerClass() {
		return blackPlayerClass; 
	}

	public boolean reverseDisplay() {
		return reverseDisplay; 
	}
}
