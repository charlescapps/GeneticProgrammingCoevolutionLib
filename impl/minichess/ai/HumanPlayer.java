package minichess.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import minichess.boards.*;
import minichess.exceptions.*;

public class HumanPlayer implements PlayerInterface {
	private BufferedReader in; 
	private BufferedWriter out;
	private Board.COLOR myColor;
	
	public HumanPlayer(Board.COLOR c) {
		this.myColor = c; 
		in  = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
	}
	
	//In case you weren't getting input from the keyboard
	public HumanPlayer(Board.COLOR c, BufferedReader in, BufferedWriter out) {
		this.myColor = c; 
		this.in = in; 
		this.out = out; 
	}

	@Override
	public Board.COLOR getColor() {
		return myColor;
	}
	
	@Override
	public Move makeMove(Board b) {
		Move toPlay = null;
		try {
			toPlay = inputMove(b); 
		}
		catch (IOException e) {
			System.err.println("IO Failure occurred when getting a move. Exiting with status code 1...\n");
			e.printStackTrace(System.err);
			System.exit(1); 
		}
		
		return toPlay;
	}

	//Fancy: input a move string, OR a number for a valid board in a list of new boards
	private Move inputMove(Board b) throws IOException{
		Move m = null; 
		String userInput = null;
		List<Move> moves = b.getAllValidMoves(); 
		
		while (m == null) {
			out.write("Enter a move (e.g. 'b5-b4'>"); 
			out.flush();
			userInput = in.readLine();
			out.newLine();
			
			try { //Try to parse move in format e.g. a1-b1
				m = Move.parseMove(userInput); 
				//Final check: If the move isn't a legal move for this board, throw exception. 
				if (!moves.contains(m)){
					throw new InvalidMoveException("****Invalid move for current board state.****\n");  
				}
			}
			catch (InvalidMoveException e) { //Failed to be of form 'a5-b5', or not a legal move
				out.write(e.getMessage()); 
				out.newLine(); 
				m = null; 
			}
		}
		return m;
	}

	
}
