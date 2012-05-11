package minichess.ai; 

import static minichess.boards.Board.COLOR; 
import minichess.boards.*; 

public interface PlayerInterface {

	public COLOR getColor(); 
	public Move makeMove(Board b); 

}
