package minichess.exceptions;

public class FailedToMove extends Exception {
	public FailedToMove() {
		super("No one knows why, but somehow your AI failed to make a move...\n"); 
	}
}
