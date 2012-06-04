package capps.gp.gptrees; 

import capps.gp.gpglobal.GPConfig;

public class ERCNode extends GPTerminal implements Cloneable{
    private final double value; 

	public ERCNode() {
		value = GPConfig.getRandGen().nextDouble()*4.0 - 2.0;
    }

    public ERCNode(double val) {
        this.value = val; 
    }

	@Override
    public double interpretForResult(GameState state) {
        return value; 
    }

	@Override
    public String label() {
        return "ERC=" + value; //String.format("%1$,.3f",value); 
    }

	@Override
	public Object clone() {
		return this; 
	}

}
