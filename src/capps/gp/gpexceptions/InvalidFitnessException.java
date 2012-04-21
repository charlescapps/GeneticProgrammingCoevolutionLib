package capps.gp.gpexceptions; 

public class InvalidFitnessException extends Exception {
	public InvalidFitnessException() {
		super("Exception: Attempt to access fitness for creature that"
				+ " hasn't had fitness computed for current generation."); 
	}
}
