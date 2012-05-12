package minichess;

public class MoveValue {
	private final Move move; 
	private final double value; 
	
	public MoveValue(Move m, double val) {
		this.move = m; 
		this.value = val; 
	}
	
	public Move getMove() {return move; }
	public double getValue() {return value; }
}
