package capps.gp.gptrees; 

public class MaxFunc extends GPFunction implements Cloneable{
	public MaxFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		return Math.max(subtrees.get(0).interpretForResult(state), 
			subtrees.get(1).interpretForResult(state)); 

	}

	@Override
	public String label() {
		return "MAX"; 
	}

	@Override
	public Object clone() {
		return new MaxFunc(); 
	}

}
