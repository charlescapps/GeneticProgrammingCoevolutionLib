package minichess.boards; 

import minichess.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements Iterable<PIECE>{

	public static final int ROWS = 6; 
	public static final int COLS = 5; 

	protected PIECE[][] boardState;
	protected COLOR whoseTurn; 
	protected COLOR winner; 
	protected int plyNumber; //A ply is ONE move by ONE player, see Wikipedia.
	protected boolean isGameOver; 
	protected List<Move> validMoveCache; 

	public COLOR getWhoseTurn() {
		return whoseTurn;
	}

	public COLOR getWinner() {
		return winner;
	}

	public boolean isGameOver() {
		return isGameOver; 
	}

	public int getPlyNumber() {
		return plyNumber;
	}

    public void setDraw() { //Just sets gameOver = true, winner = null
    	isGameOver = true; 
    	winner = null;
    }

	/**Default constructor: hard code in initial board.*/
	public Board() {

        this.boardState = new PIECE[ROWS][COLS];
		this.whoseTurn = COLOR.WHITE; 
		this.winner = null; 
		this.plyNumber = 0; 
		this.isGameOver = false; 
		this.validMoveCache = null; 
        
		/**Empty spaces in middle of board.*/
        for (int i = 2; i <= 3; i++) {
            for (int j = 0; j < COLS; j++) {
                boardState[i][j] = PIECE.EMPTY_PIECE; 
            }
        }
        
		/**Black's back row.*/
        boardState[0][0] = PIECE.B_KING; 
        boardState[0][1] = PIECE.B_QUEEN; 
        boardState[0][2] = PIECE.B_BISHOP; 
        boardState[0][3] = PIECE.B_KNIGHT; 
        boardState[0][4] = PIECE.B_ROOK; 
        
		/**Black's pawns*/
        int row = 1; 
        for (int j = 0; j < COLS; j++) {
            boardState[row][j] = PIECE.B_PAWN; 
        }
        
		/**White's back row*/
        boardState[ROWS - 1][4] = PIECE.W_KING; 
        boardState[ROWS - 1][3] = PIECE.W_QUEEN; 
        boardState[ROWS - 1][2] = PIECE.W_BISHOP; 
        boardState[ROWS - 1][1] = PIECE.W_KNIGHT; 
        boardState[ROWS - 1][0] = PIECE.W_ROOK; 
        
		/**White's pawns*/
        row = ROWS - 2; 
        for (int j = 0; j < COLS; j++) {
            boardState[row][j] = PIECE.W_PAWN; 
        }
	}

	public Board(Board toCopy) {
		this.boardState = new PIECE[ROWS][COLS]; 

		for (int i = 0; i < ROWS; i++) 
			for (int j = 0; j < COLS; j++)
				this.boardState[i][j] = toCopy.boardState[i][j]; 

		this.isGameOver = toCopy.isGameOver; 
		this.plyNumber = toCopy.plyNumber; 
		this.whoseTurn = toCopy.whoseTurn; 
		this.winner = toCopy.winner; 
		this.validMoveCache = toCopy.validMoveCache; 
	}

    public PIECE[][] getBoardState() {
        return boardState;
    }

	/**Scan through each position on the board, and add all the getAllValidMoves for each
	 * piece to a list. */
    public List<Move> getAllValidMoves() { //Get all the getAllValidMoves as an LinkedList<Move>
		if (validMoveCache != null) 
			return validMoveCache; 
    	
		List<Move> allMoves = new ArrayList<Move>();
		
		for (int i = 0; i < ROWS; i++) 
		    for (int j = 0; j < COLS; j++) 
				if (boardState[i][j] != PIECE.EMPTY_PIECE 
						&& boardState[i][j].color() == whoseTurn) //Add getAllValidMoves for piece if it's the proper turn for that piece
				    getAllValidMovesForOnePiece(allMoves, i, j); //Adds moves for piece at (i, j) to the allMoves List
		
		if (allMoves.isEmpty()) { //For draw, set gameOver = true, winner = null
			setDraw(); 
		}
		
		this.validMoveCache = allMoves; 

		return allMoves; 
    }

	/**Add the getAllValidMoves for a given piece on a given row / col to a list of moves.*/
    private void getAllValidMovesForOnePiece(List<Move> addToThis, int r, int c) { //Gets all moves for 1 piece. Assumes boardState[r][c] isn't null
		PIECE p = boardState[r][c]; 
		
		switch(p) {
			case W_PAWN: 
				if (r > 0 && boardState[r - 1][c] == PIECE.EMPTY_PIECE) {
					addToThis.add(new Move(r, c, r-1, c));
				}
				if (c > 0 && r > 0 && boardState[r - 1][c - 1] != PIECE.EMPTY_PIECE && (boardState[r - 1][c - 1].color() != p.color()) ) {
					addToThis.add(new Move(r, c, r-1, c-1));
				}
				if (c < COLS - 1 && r > 0 && boardState[r - 1][c + 1] != PIECE.EMPTY_PIECE && boardState[r - 1][c + 1].color() != p.color()) {
					addToThis.add(new Move(r, c, r-1, c+1));
				}
                break; 
		
			case B_PAWN:
				if (r < ROWS - 1 && boardState[r + 1][c] == PIECE.EMPTY_PIECE) {
					addToThis.add(new Move(r, c, r+1, c));
				}
				if (c > 0 && r < ROWS - 1 && boardState[r + 1][c - 1] != PIECE.EMPTY_PIECE && boardState[r + 1][c - 1].color() != p.color()) {
					addToThis.add(new Move(r, c, r+1, c-1));
				}
				if (c < COLS - 1 && r < ROWS - 1 && boardState[r + 1][c + 1] != PIECE.EMPTY_PIECE && boardState[r + 1][c + 1].color() != p.color()) {
					addToThis.add(new Move(r, c, r + 1, c + 1));
				}
                break;
				
			case W_KING:  //All 8 directions, only move one square, can take
			case B_KING: 
				scan(addToThis, r, c, 1, 0, true, true, p.color());
				scan(addToThis, r, c, -1, 0, true, true, p.color());
				scan(addToThis, r, c, 0, 1, true, true, p.color());
				scan(addToThis, r, c, 0, -1, true, true, p.color());
				scan(addToThis, r, c, 1, 1, true, true, p.color());
				scan(addToThis, r, c, 1, -1, true, true, p.color());
				scan(addToThis, r, c, -1, 1, true, true, p.color());
				scan(addToThis, r, c, -1,-1, true, true, p.color());
				break;
				
			case W_BISHOP:  
			case B_BISHOP: 
				//Changing color, no taking allowed
				scan(addToThis, r, c, 1, 0, true, false, p.color()); 
				scan(addToThis, r, c, 0, 1, true, false, p.color());
				scan(addToThis, r, c, -1, 0, true, false, p.color());
				scan(addToThis, r, c, 0, -1, true, false, p.color());
				//Diagonals, taking allowed, can move more than one square
				scan(addToThis, r, c, 1, 1, false, true, p.color()); 
				scan(addToThis, r, c, -1, 1, false, true, p.color());
				scan(addToThis, r, c, 1, -1, false, true, p.color());
				scan(addToThis, r, c, -1, -1, false, true, p.color());
				break;
			
			case W_QUEEN: //Like king, but can move any # of squares
			case B_QUEEN:
				scan(addToThis, r, c, 1, 0, false, true, p.color());
				scan(addToThis, r, c, -1, 0, false, true, p.color());
				scan(addToThis, r, c, 0, 1, false, true, p.color());
				scan(addToThis, r, c, 0, -1, false, true, p.color());
				scan(addToThis, r, c, 1, 1, false, true, p.color());
				scan(addToThis, r, c, 1, -1, false, true, p.color());
				scan(addToThis, r, c, -1, 1, false, true, p.color());
				scan(addToThis, r, c, -1,-1, false, true, p.color());
				break;
				
			case W_KNIGHT: //The 8 corners of timely doom
			case B_KNIGHT: 
				scan(addToThis, r, c, 2, 1, true, true, p.color());
				scan(addToThis, r, c, 2, -1, true, true, p.color());
				scan(addToThis, r, c, -2, 1, true, true, p.color());
				scan(addToThis, r, c, -2, -1, true, true, p.color());
				scan(addToThis, r, c, 1, 2, true, true, p.color());
				scan(addToThis, r, c, -1, 2, true, true, p.color());
				scan(addToThis, r, c, 1, -2, true, true, p.color());
				scan(addToThis, r, c, -1, -2, true, true, p.color());
				break; 
				
			case W_ROOK: //The 4 cardinal directions of slowly creeping doom
			case B_ROOK: 
				scan(addToThis, r, c, 1, 0, false, true, p.color());
				scan(addToThis, r, c, -1, 0, false, true, p.color());
				scan(addToThis, r, c, 0, 1, false, true, p.color());
				scan(addToThis, r, c, 0, -1, false, true, p.color());
				break;
		}
    }

    //Adds the getAllValidMoves obtained by scanning in some direction once or many times. 
    private void scan(List<Move> addToThis, int r, int c, int dr, int dc, 
			boolean once, boolean canTake, COLOR color) {
		int curRow = r + dr; 
	    int curCol = c + dc;
	    
	    //If we're out of bounds, just return
	    if (curRow < 0 || curCol < 0 || curRow >= ROWS || curCol >= COLS)
	    	return; 
	    
	    //Just return if there's a piece of the same color in the way 
	    if (boardState[curRow][curCol].color() == color)
	    	return; 
		
		if (once) {
			if (boardState[curRow][curCol] == PIECE.EMPTY_PIECE)
				addToThis.add(new Move(r, c, curRow, curCol));
			else if (canTake && boardState[curRow][curCol].color() != color)
				addToThis.add(new Move(r, c, curRow, curCol));
		}
		else {
			do {
				if (boardState[curRow][curCol]==PIECE.EMPTY_PIECE) {
				    addToThis.add(new Move(r, c, curRow, curCol));
				}
				else if (canTake && boardState[curRow][curCol].color() != color) {  //Hit something of a different color and can take
				    addToThis.add(new Move(r, c, curRow, curCol)); 
				    break;
				}
				else {	//Hit something we can't take
					break;
				}
			
				curRow += dr; 
				curCol += dc; 
		    } while (curRow >=0 && curRow < ROWS && curCol >= 0 && curCol < COLS);
		}
		return;
    }

	public Board performMoveAndClone(Move m) {
		Board bClone = (Board)this.clone(); 
		bClone.performMove(m); 
		return bClone; 
	}

    public void performMove(Move m) { //Perform a move and set appropriate flags
    	
    	final int r1 = m.row1; 
    	final int c1 = m.col1;
    	final int r2 = m.row2; 
    	final int c2 = m.col2; 
    	
    	PIECE toMove = boardState[r1][c1];
    	PIECE taken  = boardState[r2][c2];
    	
    	assert (getAllValidMoves().contains(m)) : "Attempt to do a move not in the list of all moves: " + m + "\n"; 
    	
    	assert(toMove != PIECE.EMPTY_PIECE) : "Attempt to move from a square w/o a piece!\n";
    	
    	//Catch stupid mistakes, i.e. giving a command to move a piece on the wrong turn
    	assert ( (whoseTurn == COLOR.BLACK && toMove.color() == COLOR.BLACK) || 
    			 (whoseTurn == COLOR.WHITE && toMove.color() == COLOR.WHITE));
    	
    	boardState[r1][c1] = PIECE.EMPTY_PIECE; 
    	boardState[r2][c2] = toMove; 
    	
    	whoseTurn = (whoseTurn == COLOR.BLACK ? COLOR.WHITE : COLOR.BLACK ); //Swap whose turn it is
    	
		++plyNumber; 
    	
    	if (taken == PIECE.B_KING) { //win conditions
    		isGameOver = true; 
    		winner = COLOR.WHITE; 
			return; 
    	}
    	else if (taken == PIECE.W_KING) {
    		isGameOver = true; 
    		winner = COLOR.BLACK; 
			return; 
    	}
    	else if (plyNumber >= 80) {
			setDraw(); 
			return; 
    	}
    	
    	if (toMove == PIECE.W_PAWN && 0 == r2) { //Queen me!
    		boardState[r2][c2] = PIECE.W_QUEEN;
    	}
    	else if (toMove == PIECE.B_PAWN && r2 == ROWS - 1) {
    		boardState[r2][c2] = PIECE.B_QUEEN; 
    	}
    	
		this.validMoveCache = null; 
    	
    	//Unfortunately, must generate all moves if it's not a game over to guarantee we catch draws
		getAllValidMoves(); 

    }
    
	@Override
	public String toString() {
		return this.toString(false); 
	}

    public String toString(boolean reverseBoardDisplay) { //Print in awesome format
        
        StringBuilder sb = new StringBuilder();
        
        if (isGameOver) {
        	sb.append("    GAME OVER, " + 
        				(winner == null ? "NOBODY" : winner) + 
        				" wins!\n*******************************\n" );
        }
        
        sb.append("PLY (" + plyNumber + ")\tPLAYER (" + whoseTurn + ")\n");
        sb.append("-------------------------------\n");
        
        for (int i = -2; i <= ROWS; i++) {
            sb.append("\t");
            for (int j = -2; j <= COLS; j++) {
                if ( (i == -1 || i == ROWS) && (j == -1 || j == COLS) ) {
                    sb.append("+");
                }
                else if ( (i == -2 || i == ROWS) && (j == -2 || j == COLS) ) {
                    sb.append(" ");
                }
                else if ( i == -2 && j == -1) {
                    sb.append(" ");
                }
               
                else if (i == -2) {
                	if (!reverseBoardDisplay)
                		sb.append(String.valueOf((char) ('a' + j)) + " ");
                	else
                		sb.append(String.valueOf((char) ('e' - j)) + " ");
                }
                else if (j == -2) {
                    if (i == -1)
                        sb.append(" ");
                    else {
                    	if (!reverseBoardDisplay)
                    		sb.append(String.valueOf(i + 1));
                    	else 
                    		sb.append(String.valueOf(6 - i));
                    }
                }
                else if (i == -1 || i == ROWS) {
                    sb.append("--");
                }
                else if (j == -1 || j == COLS) {
                    sb.append("|");
                }
                else {
                	if (!reverseBoardDisplay)
                		sb.append(boardState[i][j] + " ");
                	else
                		sb.append(boardState[ROWS - i - 1][COLS - j - 1] + " ");
                }
            }
            sb.append("\n");
        }
        
        return sb.toString(); 
    }

	@Override
	public Object clone() {
		return new Board(this); 
	}

	@Override
	public Iterator<PIECE> iterator() {
		return new BoardIter();
	}

	private class BoardIter implements Iterator<PIECE> {
		private int r; 
		private int c;

		public BoardIter() {
			this.r = 0;
			this.c = 0; 
		}

		@Override
		public boolean hasNext() {
			if (r >= ROWS)
				return false;
			return true; 
		}

		@Override
		public PIECE next() {
			PIECE p = boardState[r][c]; 
			++c; 
			if (c >= COLS){
				c = 0;
				++r; 
			}
			return p;
		}

		@Override
		public void remove() {
			
		}

	}
}
