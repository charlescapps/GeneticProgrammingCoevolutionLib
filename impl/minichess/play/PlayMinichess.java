package minichess.play;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.List;

import minichess.ai.AiInterface;
import minichess.boards.*; 
import minichess.exceptions.*; 
import minichess.*;

import static minichess.COLOR.*; 

import minichess.heuristic.PointHeuristic;

public class PlayMinichess implements PlayInterface{
	private AiInterface whitePlayer; 
	private AiInterface blackPlayer; 
	private Board startBoard; 
	private Move lastMovePlayed; 
	private Board previousBoard;
	private Board currentBoard; 
	private PointHeuristic ph;
	
	private BufferedWriter out; 
	
	public PlayMinichess() {
		this.out = new BufferedWriter(new OutputStreamWriter(System.out));
		this.ph = new PointHeuristic(); 
	}

	public PlayMinichess(AiInterface whitePlayer, 
			AiInterface blackPlayer, 
			Board startBoard) {
		
		this.whitePlayer = whitePlayer; 
		this.blackPlayer = blackPlayer; 
		this.startBoard = startBoard; 
		this.lastMovePlayed = null; 
		this.ph = new PointHeuristic(); 
		
		this.currentBoard = (Board)this.startBoard.clone(); 
		this.previousBoard = null; 
		
		this.out = new BufferedWriter(new OutputStreamWriter(System.out));
	}

	@Override
	public void setWhitePlayer(AiInterface ai) {
		this.whitePlayer = ai; 
	}
	
	@Override
	public void setBlackPlayer(AiInterface ai) {
		this.blackPlayer = ai; 
	}

	@Override
	public void setStartBoard(Board b) {
		this.startBoard = b; 
		this.currentBoard = (Board)this.startBoard.clone(); 
		this.previousBoard = null; 
	}

	@Override
	public void play() throws InvalidMoveException, IOException {
		while (!currentBoard.isGameOver()) {
			display(); 
			previousBoard = currentBoard; 
			lastMovePlayed = (currentBoard.getWhoseTurn() == WHITE ? 
					whitePlayer.makeMove(currentBoard): 
					blackPlayer.makeMove(currentBoard)); 
			currentBoard = currentBoard.performMoveAndClone(lastMovePlayed); 

		}
		
		display();
	}
	
	@Override
	public void display() throws IOException, InvalidMoveException {
		out.newLine(); 
		out.write(currentBoard.toString(false)); //Print board
		out.newLine(); 
		out.write("Previous Move: " + (lastMovePlayed==null ? 
					"No previous move." : 
					lastMovePlayed.toString())) ;
		System.out.println("Board point value: " + ph.evaluateBoard(currentBoard)); 

		out.newLine();
		out.flush(); 
	}
	
}
