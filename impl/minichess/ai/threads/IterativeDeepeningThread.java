package minichess.ai.threads;

import minichess.Move; 
import minichess.boards.Board; 
import minichess.ai.IterativeDeepeningAI;
import minichess.ai.threads.ExitThreadException;

public class IterativeDeepeningThread extends Thread {
	
	private static final int MAX_DEPTH = 80; 
	private Move toPlay; 
	private Board theBoard; 
	private int depth; 
	private boolean isStopped; 
	private IterativeDeepeningAI ai; 
	
	public IterativeDeepeningThread(IterativeDeepeningAI ai, Board b, int minDepth) {
		this.setPriority(Thread.MAX_PRIORITY);
		this.toPlay = null; 
		this.theBoard = b; 
		this.depth = minDepth; 
		this.ai = ai; 
		this.isStopped = false;
	}
	
	@Override
	public void run() {
		for (; ; depth++) {
			//If we've calculated a move to the largest meaningful depth
			if (toPlay != null && ( (depth + theBoard.getPlyNumber()) > MAX_DEPTH) ) {
				break; 
			}
			try {
				toPlay = ai.makeMove((Board)theBoard.clone(), depth);
			}
			catch (ExitThreadException e) {
				//System.out.println("Exiting thread at depth " + depth + "...\n"); 
				return; 
			}
			if (isStopped) {
				return;
			}
		}
	}
	
	public Move getMove() {
		System.out.println("\nDEBUG: Iterative Deepening returned move while searching at depth: " + depth); 
		
		return toPlay;
	}
	
	public int getDepth() {
		return depth; 
	}
	
	public void setStopFlag() {isStopped = true;}

	public boolean isStopped() {return isStopped;}
}
