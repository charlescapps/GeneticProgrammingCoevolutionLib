package capps.gp.gpgamestates; 

import minichess.boards.Board;
import capps.gp.gptrees.GameState;

/**This is a trivial wrapper class to make a Minichess Board into a 
 * GP GameState. 
 *
 * Another solution would have been to simply have the minichess Board class
 * implement GameState, but then horrible intanglement between minichess
 * code and GP code would ensue. */

public class MinichessState implements GameState {
    public final Board board; 

    public MinichessState(Board b) {
        this.board = b;
    }
}
