package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class BlackPawnsInCenter extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        PIECE[][] boardState = b.getBoardState();
        int num = 0; 
        for (int r = 2; r < 4; r++) 
            for (int c = 0; c < 5; c++)
                if (boardState[r][c]==PIECE.B_PAWN)
                    ++num;
		return (double)num;
	}

	@Override
	public String label() {
		return "#p_c";
	}

	@Override
	public Object clone() {
		return this;
	}


}
