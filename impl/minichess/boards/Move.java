package minichess.boards;

import minichess.exceptions.InvalidMoveException;

public class Move {

	public final int row1;  
	public final int row2;  
	public final int col1;  
	public final int col2;  

	public Move(int r1, int c1, int r2, int c2) {
		this.row1 = r1; 
		this.row2 = r2; 
		this.col1 = c1; 
		this.col2 = c2; 
	}

    public static Move parseMove(String move) throws InvalidMoveException { //meh to fix up later it works
    	
    	move = move.trim().toLowerCase();
    	
    	if (move.length() != 5 || move.charAt(2) != '-') {
    		throw new InvalidMoveException("Move string isn't 5 chars long or 3rd char isn't '-'!\n");
    	}
    	else if (
    			move.charAt(0) < 'a' || move.charAt(0) > 'e' || 
    			move.charAt(3) < 'a' || move.charAt(3) > 'e'
    			) {
    		throw new InvalidMoveException("Move string: 1st char or 4th char isn't a letter from a-e!\n");
    	}
    	else if (
    			(move.charAt(1) - '0' < 1) || (move.charAt(1) - '0' > 6) || 
    			(move.charAt(4) - '0' < 1) || (move.charAt(4) - '0' > 6)
    			) { 
    		throw new InvalidMoveException("Move string: 2nd char or 5th char isn't a number from 1-6!\n");
    	}
    	
    	return new Move(move.charAt(1) - '1', move.charAt(0) - 'a', move.charAt(4) - '1', move.charAt(3) - 'a');
    }

	@Override
	public boolean equals(Object o) {
		Move moveObj = (Move) o; 
		return moveObj.row1 == this.row1 && moveObj.row2 == this.row2 &&
			moveObj.col1 == this.col1 && moveObj.col2 == this.col2; 
	}
}
