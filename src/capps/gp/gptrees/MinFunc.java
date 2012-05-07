package capps.gp.gptrees; 

public class MinFunc extends GPFunction implements Cloneable{
	public MinFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		return Math.min(subtrees.get(0).interpretForResult(state), 
			subtrees.get(1).interpretForResult(state)); 

	}

	@Override
	public String label() {
		return "MIN"; 
	}

	@Override
	public Object clone() {
		return new MinFunc(); 
	}

}
