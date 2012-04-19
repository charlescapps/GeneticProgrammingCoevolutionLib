package capps.gp.gptrees; 

public class DivideFunc extends GPFunction {
	public DivideFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public int interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		int result0 = subtrees.get(0).interpretForResult(state); 
		int result1 = subtrees.get(1).interpretForResult(state);
		return (result1 == 0 ? result0 : Math.round((float)result0 / (float)result1)); 

	}

	@Override
	public String label() {
		return "/"; 
	}

}
