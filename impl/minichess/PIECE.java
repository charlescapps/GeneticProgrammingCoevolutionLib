package minichess;

public enum PIECE {
    W_PAWN (COLOR.WHITE), W_KNIGHT (COLOR.WHITE), W_BISHOP (COLOR.WHITE), 
		W_ROOK (COLOR.WHITE), W_KING (COLOR.WHITE), W_QUEEN (COLOR.WHITE), 
    	B_PAWN (COLOR.BLACK), B_KNIGHT (COLOR.BLACK), B_BISHOP (COLOR.BLACK), 
		B_ROOK (COLOR.BLACK), B_KING (COLOR.BLACK), B_QUEEN (COLOR.BLACK), 
    EMPTY_PIECE (null); 
    
    private final COLOR color;
    
    PIECE(COLOR c) {
        this.color = c;
    }

    public COLOR color() {
    	return this.color;
    }

    public String toString() {
		switch(this) {
			case W_PAWN:
				return "P"; 
			case B_PAWN:
				return "p"; 
			case W_KNIGHT:
				return "N"; 
			case B_KNIGHT:
				return "n"; 
			case W_BISHOP:
				return "B"; 
			case B_BISHOP:
				return "b"; 
			case W_ROOK:
				return "R"; 
			case B_ROOK:
				return "r"; 
			case W_KING:
				return "K"; 
			case B_KING:
				return "k"; 
			case W_QUEEN:
				return "Q"; 
			case B_QUEEN:
				return "q"; 
			default:
				return "_"; 
		}
    }
    
    public static PIECE charToPiece(char c) {
    	switch (c) {
			case ('p'):
				return PIECE.B_PAWN; 
			case ('P'):
				return PIECE.W_PAWN; 
			case ('n'): 
				return PIECE.B_KNIGHT; 
			case ('N'): 
				return PIECE.W_KNIGHT; 
			case ('k'):
				return PIECE.B_KING; 
			case ('K'):
				return PIECE.W_KING; 
			case ('q'):
				return PIECE.B_QUEEN; 
			case ('Q'): 
				return PIECE.W_QUEEN;
			case ('b'):
				return PIECE.B_BISHOP; 
			case ('B'):
				return PIECE.W_BISHOP; 
			case ('r'):
				return PIECE.B_ROOK; 
			case ('R'):
				return PIECE.W_ROOK;
			default: 
				return PIECE.EMPTY_PIECE; 
    	}
    }
    
    
}
