package capps.gp.gptrees; 

import capps.gp.gpglobal.GPConfig;

public class ERCNode extends GPTerminal implements Cloneable{
    private double value; 

	public ERCNode() {
		value = GPConfig.DEFAULT_RANDOM.nextDouble()*4.0 - 2.0;
    }

	@Override
    public double interpretForResult(GameState state) {
        return value; 
    }

	@Override
    public String label() {
        return "ERC=" + String.format("%1$,.3f",value); 
    }

	@Override
	public Object clone() {
		ERCNode cloneNode = new ERCNode(); 
		cloneNode.value = this.value; 
		return cloneNode; 
	}

}
