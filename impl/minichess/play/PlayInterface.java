package minichess.play;

import java.io.IOException;
import minichess.exceptions.*;

public interface PlayInterface {

	public void play() throws InvalidMoveException, IOException; 
	public void display() throws IOException, InvalidMoveException; 
	
}
