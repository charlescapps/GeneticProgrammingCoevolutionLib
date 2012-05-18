package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class BlackBishopsOffBack extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        PIECE[][] boardState = b.getBoardState();
        for (int r = 1; r < 6; r++) 
            for (int c = 0; c < 5; c++)
                if (boardState[r][c]==PIECE.B_BISHOP)
                    return 1.0;
		return 0.0;
	}

	@Override
	public String label() {
		return "b_off_back";
	}

	@Override
	public Object clone() {
		return this;
	}


}
