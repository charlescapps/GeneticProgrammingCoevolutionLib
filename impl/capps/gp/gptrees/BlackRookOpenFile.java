package capps.gp.gptrees; 

import capps.gp.gpgamestates.MinichessState;
import capps.gp.gptrees.GameState;

import minichess.PIECE;

import minichess.boards.Board;

public class BlackRookOpenFile extends GPTerminal {

	@Override
	public double interpretForResult(GameState state) {
        Board b = ((MinichessState)state).board; 
        PIECE[][] boardState = b.getBoardState();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 5; c++) {
                if (boardState[r][c]==PIECE.B_ROOK){
                    for (int r1 = r + 1; r < 6; r++) {
                        if (boardState[r1][c] != PIECE.EMPTY_PIECE) {
                            return 0.0;
                        }
                    }
                    return 1.0;
                }
            }
        }
        return 0.0; 
	}

	@Override
	public String label() {
		return "#r_open";
	}

	@Override
	public Object clone() {
		return this;
	}


}
