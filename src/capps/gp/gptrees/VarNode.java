package capps.gp.gptrees; 

import java.util.Map; 
import java.util.HashMap;

/**Node storing a variable. By default, assume single variable function and
 * just give it the name 'x'. Could easily implement a node that randomly
 * chooses a variable name from some list. Need to use default constructor
 * since that is how GPTree randomly generates trees! 
 */
public class VarNode extends GPTerminal {
	private String name; 

	public VarNode() {
		this.name = "x"; 
	}

	public VarNode(String name) {
		this.name = name; 
	}

	@Override
	public String label() {
		return "Var " + name; 
	}

	@Override
	public double interpretForResult(GameState state) {
		assert VarValues.class.isInstance(state) : 
				"GameState passed to a VarNode must be an instance of VarValues class!"; 

		VarValues vv = (VarValues) state; 
		return vv.getValue(this.name); 
	}

	@Override
	public Object clone() {
		return new VarNode(this.name); 
	}

	/**Inner class implementing GameState for evaluating variables.
	 * Map variable names to values. 
	 */
	public static class VarValues implements GameState {
		private final Map<String, Double> nameToValue;  

		public VarValues() {
			this.nameToValue = new HashMap<String,Double>();  
		}
		public void addVarValue(String name, Double val) {
			nameToValue.put(name, val); 
		}

		public double getValue(String name) {
			return nameToValue.get(name); 
		}
	}

}
