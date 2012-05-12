package minichess.ai; 

import minichess.boards.*; 
import minichess.*; 

import minichess.heuristic.HeuristicInterface;

public interface AiInterface {

	public static double MIN_SCORE = Double.NEGATIVE_INFINITY; 
	public static double MAX_SCORE = Double.POSITIVE_INFINITY; 

	public COLOR getColor(); 
	public void setColor(COLOR c); 

	public HeuristicInterface getHeuristic(); 
	public void setHeuristic(HeuristicInterface hi); 

	public Move makeMove(Board b); 

}
