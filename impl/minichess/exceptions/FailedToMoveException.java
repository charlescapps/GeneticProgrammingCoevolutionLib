package minichess.exceptions;

public class FailedToMoveException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedToMoveException() {
		super("No one knows why, but somehow your AI failed to make a move...\n"); 
	}
}
