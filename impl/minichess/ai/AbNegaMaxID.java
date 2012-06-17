package minichess.ai;

import java.util.List;

import minichess.*; 
import minichess.boards.Board; 
import minichess.ai.threads.*;


public class AbNegaMaxID extends IterativeDeepeningAI {
	
	public AbNegaMaxID() {
		super();
        this.setHeuristic(new minichess.heuristic.PointHeuristic()); 
	}

	public AbNegaMaxID(long panicTimeMillis, int minDepth) {
		super(panicTimeMillis, minDepth);
        this.setHeuristic(new minichess.heuristic.PointHeuristic()); 
	}

	//Not the recursive method; this is the top-level call 
	public Move makeMove(Board b, int depth) throws ExitThreadException {
		
		MoveValue result = negaMax(b, depth, AiInterface.MIN_SCORE, AiInterface.MAX_SCORE); 
		
		return result.getMove();
	}
	
	private MoveValue negaMax(Board b, int remainingDepth, double alpha, double beta) 
		throws ExitThreadException {
		if ( ((IterativeDeepeningThread)Thread.currentThread()).isStopped() ) {
			throw new ExitThreadException();
		}
		
		if (remainingDepth <= 0 || b.isGameOver()) { //At bottom of tree, return node with its board value
			double boardVal = myHeuristic.evaluateBoard(b); 
			return new MoveValue(null, 
					(b.getWhoseTurn() == COLOR.BLACK ? -boardVal : boardVal)); 
		}
		
		double bestScore = AiInterface.MIN_SCORE;
		double subScore; 
		List<Move> moves = b.getAllValidMoves(); 
		Move bestMove = moves.get(0); 
		
		for (Move m: moves) {
			Board boardAfterMove = b.performMoveAndClone(m);

			subScore = -negaMax(boardAfterMove, remainingDepth - 1, -beta, -Math.max(bestScore, alpha)).getValue();
			
			if (subScore > bestScore) { 
				bestScore = subScore; 
				bestMove = m; 
			}
			
			if (bestScore >= beta) {
				return new MoveValue(bestMove, bestScore); 
			}
		}
		
		return new MoveValue(bestMove, bestScore);
		
	}

}
