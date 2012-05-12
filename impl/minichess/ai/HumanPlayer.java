package minichess.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import minichess.*;
import minichess.boards.*;
import minichess.exceptions.*;
import minichess.heuristic.HeuristicInterface;

public class HumanPlayer implements AiInterface {
	private BufferedReader in; 
	private BufferedWriter out;
	private COLOR myColor;
	
	public HumanPlayer() {
		in  = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
	}

	public HumanPlayer(COLOR c) {
		this.myColor = c; 
		in  = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
	}

	public void setColor(COLOR c) {
		this.myColor = c; 
	}
	
	//In case you weren't getting input from the keyboard
	public HumanPlayer(COLOR c, BufferedReader in, BufferedWriter out) {
		this.myColor = c; 
		this.in = in; 
		this.out = out; 
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
			out.write("Enter a move (e.g. 'b5-b4')>"); 
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

	@Override
	public COLOR getColor() {
		return myColor;
	}

	@Override
	public HeuristicInterface getHeuristic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHeuristic(HeuristicInterface hi) {
		// TODO Auto-generated method stub
		
	}

	
}
