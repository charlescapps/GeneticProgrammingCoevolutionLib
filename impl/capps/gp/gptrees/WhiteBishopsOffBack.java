package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class WhiteBishopsOffBack extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        PIECE[][] boardState = b.getBoardState();
        for (int r = 0; r < 5; r++) 
            for (int c = 0; c < 5; c++)
                if (boardState[r][c]==PIECE.W_BISHOP)
                    return 1.0;
		return 0.0; 
	}

	@Override
	public String label() {
		return "B_off_back";
	}

	@Override
	public Object clone() {
		return this;
	}


}
