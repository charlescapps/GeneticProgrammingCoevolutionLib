package capps.gp.gptrees; 

public class IfLessThanFunc extends GPFunction implements Cloneable{
	public IfLessThanFunc() {

	}

	@Override
	public int numSubtrees() {
		return 4; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert(subtrees.size() == 4); 
        double result0 = subtrees.get(0).interpretForResult(state);   
		double result1 = subtrees.get(1).interpretForResult(state); 
		if (result0 <= result1) {
            return subtrees.get(2).interpretForResult(state); 
        } 
        else {
            return subtrees.get(3).interpretForResult(state); 
        }

	}

	@Override
	public String label() {
		return "IfLessThan"; 
	}

	@Override
	public Object clone() {
		return new IfLessThanFunc(); 
	}

}
