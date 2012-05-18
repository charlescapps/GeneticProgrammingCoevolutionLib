package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class NumWhitePawns extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        int numPawns = 0;
        for (PIECE p: b) {
            if (p==PIECE.W_PAWN)
                numPawns++;
        }
		return (double)numPawns;
	}

	@Override
	public String label() {
		return "#P";
	}

	@Override
	public Object clone() {
		return this;
	}


}
