package minichess.play;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import minichess.ai.PlayerInterface;
import minichess.boards.*; 
import minichess.exceptions.*; 

import static minichess.boards.Board.COLOR.*;

public class PlayMinichess implements PlayInterface{
	private PlayerInterface whitePlayer; 
	private PlayerInterface blackPlayer; 
	private Board startBoard; 
	private Move lastMovePlayed; 
	private Board previousBoard;
	private Board currentBoard; 
	
	private BufferedWriter out; 
	
	public PlayMinichess(PlayerInterface whitePlayer, 
			PlayerInterface blackPlayer, 
			Board startBoard, boolean printMoves) {
		
		this.whitePlayer = whitePlayer; 
		this.blackPlayer = blackPlayer; 
		this.startBoard = startBoard; 
		this.lastMovePlayed = null; 
		
		this.currentBoard = (Board)this.startBoard.clone(); 
		this.previousBoard = null; 
		
		this.out = new BufferedWriter(new OutputStreamWriter(System.out));
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
		
		out.write("*******FINAL BOARD STATE*******\n");
		display();
	}
	
	@Override
	public void display() throws IOException, InvalidMoveException {
		out.write(currentBoard.toString(false)); //Print board
		out.newLine(); 
		out.write("Previous Move:\n\t" + (lastMovePlayed==null ? 
					"No previous move." : 
					lastMovePlayed) + "\n");

		out.newLine();
		out.flush(); 
	}
	
}
