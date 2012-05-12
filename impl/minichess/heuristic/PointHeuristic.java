package minichess.heuristic; 

import minichess.*; 
import minichess.ai.AiInterface;
import minichess.boards.Board; 

import static minichess.COLOR.*;

public class PointHeuristic implements HeuristicInterface {
	@Override
	public double evaluateBoard(Board b) {
		if (b.isGameOver()) {
    		//DRAW, return 0
    		if (b.getWinner() == null) {
    			return 0.0; 
    		}
    		
    		//Return +infinity (-infinity) if WHITE (BLACK) wins
    		return ( b.getWinner() == WHITE ? 
					AiInterface.MAX_SCORE : AiInterface.MIN_SCORE); 
    	}
    	
    	double sumOfPieces = 0.0; 
    	
    	for (PIECE p: b) {
	    		switch (p) {
	    			case W_QUEEN:
	    				sumOfPieces +=9.0; 
	    				break;
	    			case W_ROOK: 
	    				sumOfPieces+=5.0;
	    				break;
	    			case W_BISHOP: 
	    			case W_KNIGHT:
	    				sumOfPieces+=3.0; 
	    				break; 
	    			case W_PAWN: 
	    				sumOfPieces+=1.0;
	    				break;
	    			case B_QUEEN: 
	    				sumOfPieces -=9.0; 
	    				break; 
	    			case B_ROOK: 
	    				sumOfPieces -=5.0; 
	    				break;
	    			case B_BISHOP: 
	    			case B_KNIGHT: 
	    				sumOfPieces -=3.0; 
	    				break; 
	    			case B_PAWN: 
	    				sumOfPieces -=1.0; 
	    				break; 
	    			default: 
	    				break; 
	    		}
    	}
    	
		/**Deal with double having -0, sigh*/
    	if (sumOfPieces == -0.0)
    		sumOfPieces = 0.0; 
    	//Record the value of this board in the board, and also return it. 
    	return sumOfPieces; 
	}
}
