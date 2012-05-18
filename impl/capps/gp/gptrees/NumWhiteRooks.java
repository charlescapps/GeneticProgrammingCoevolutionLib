package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class NumWhiteRooks extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        int num = 0;
        for (PIECE p: b) {
            if (p==PIECE.W_ROOK)
                num++;
        }
		return (double)num;
	}

	@Override
	public String label() {
		return "#R";
	}

	@Override
	public Object clone() {
		return this;
	}


}
