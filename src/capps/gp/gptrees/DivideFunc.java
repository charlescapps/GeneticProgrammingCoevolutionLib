package capps.gp.gptrees; 

public class DivideFunc extends GPFunction implements Cloneable {
	public DivideFunc() {

	}

	@Override
	public int numSubtrees() {
		return 2; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert(subtrees.size() == 2); 
		double result0 = subtrees.get(0).interpretForResult(state); 
		double result1 = subtrees.get(1).interpretForResult(state);
		return (result1 == 0. ? result0 : result0/result1); 
	}

	@Override
	public String label() {
		return "/"; 
	}

	@Override
	public Object clone() {
		return new DivideFunc(); 
	}

}
