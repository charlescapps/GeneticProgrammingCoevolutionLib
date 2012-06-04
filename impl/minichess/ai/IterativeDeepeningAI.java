package minichess.ai;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import minichess.*; 

import minichess.ai.threads.ExitThreadException;
import minichess.boards.Board; 
import minichess.ai.threads.IterativeDeepeningThread;
import minichess.config.*; 

import minichess.heuristic.HeuristicInterface;

public abstract class IterativeDeepeningAI implements AiInterface {
	
	protected COLOR myColor;
	protected IterativeDeepeningThread idThread;
	protected long timePerMove;
	protected int minDepth; 
	protected BufferedWriter debugWriter;
	protected int depthReturned; 
	protected HeuristicInterface myHeuristic;
	
	public IterativeDeepeningAI() {
		this.minDepth = MinichessConfig.getIterativeDeepMinDepth(); 
		this.idThread = null;
		this.timePerMove = MinichessConfig.getTimePerMove(); 
	}

	public IterativeDeepeningAI(long timePerMoveMillis, int minDepth) {
		
		this.timePerMove = timePerMoveMillis; 
		this.idThread = null; 
		this.minDepth = minDepth; 
		this.debugWriter = null; 
	}

	public void setColor(COLOR c) {
		this.myColor = c;
	}

	public COLOR getColor() {
		return myColor;
	}

	public long getTimePerMove() {
		return timePerMove; 
	}
	public void setTimePerMove(long time) {
		this.timePerMove = time;
	}

	public int getMinDepth() {
		return minDepth; 
	}
	public void setMinDepth(int depth) {
		this.minDepth = depth;
	}
	
	@Override
	public Move makeMove(Board b) {
		
		idThread = new IterativeDeepeningThread(this, b, minDepth);
		idThread.start(); 

		
		Move toPlay = null; 

        while (toPlay == null) {
            try {
                Thread.sleep(timePerMove);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1); 
            } 
            toPlay = idThread.getMove(); 
            
        }
		idThread.setStopFlag();
		
		return toPlay;
	}
	
	public abstract Move makeMove(Board b, int depth) throws ExitThreadException;

	public void stop() {
		if (idThread != null) {
			idThread.setStopFlag(); 
		}
	} 
	
	public int getDepth() {
		return depthReturned; 
	}

	@Override
	public HeuristicInterface getHeuristic() {
		return this.myHeuristic; 
	}

	@Override
	public void setHeuristic(HeuristicInterface hi) {
		this.myHeuristic = hi; 
	}
}
