package minichess.play;

import java.io.IOException;

import minichess.ai.AiInterface;

import minichess.boards.Board;
import minichess.exceptions.*;

public interface PlayInterface {

	public void play() throws InvalidMoveException, IOException; 
	public void display() throws IOException, InvalidMoveException; 
	public void setWhitePlayer(AiInterface ai); 
	public void setBlackPlayer(AiInterface ai); 
	public void setStartBoard(Board b);
	
}
