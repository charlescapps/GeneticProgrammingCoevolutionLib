package capps.gp.gptrees; 

public class PlusFunc extends GPFunction {
	public PlusFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public int interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		return subtrees.get(0).interpretForResult(state) + 
			subtrees.get(1).interpretForResult(state); 

	}

	@Override
	public String label() {
		return "+"; 
	}

}
