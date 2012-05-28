package minichess.heuristic; 

import minichess.boards.Board;

/**Dumb heuristic always returning 0 used by random ai*/
public class ZeroHeuristic implements HeuristicInterface {

    @Override
	public double evaluateBoard(Board b) {
        return 0.0; 
    }

}
