package capps.gp.gptrees; 

public class MinusFunc extends GPFunction implements Cloneable{
	public MinusFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		return subtrees.get(0).interpretForResult(state) - 
			subtrees.get(1).interpretForResult(state); 

	}

	@Override
	public String label() {
		return "-"; 
	}

	@Override
	public Object clone() {
		return new MinusFunc(); 
	}

}
