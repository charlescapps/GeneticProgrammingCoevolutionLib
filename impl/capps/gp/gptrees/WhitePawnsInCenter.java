package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class WhitePawnsInCenter extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        PIECE[][] boardState = b.getBoardState();
        int num = 0; 
        for (int r = 2; r < 4; r++) 
            for (int c = 0; c < 5; c++)
                if (boardState[r][c]==PIECE.W_PAWN)
                    ++num;
		return (double)num;
	}

	@Override
	public String label() {
		return "#P_c";
	}

	@Override
	public Object clone() {
		return this;
	}


}
