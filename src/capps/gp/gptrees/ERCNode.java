package capps.gp.gptrees; 

public class ERCNode extends GPTerminal{
    private double value; 

	public ERCNode() {
		double r = Math.random()*128.0;
        value = Math.round(r - 64.0); //Value from [-32.0 , 32.0)
    }

	@Override
    public double interpretForResult(GameState state) {
        return value; 
    }

	@Override
    public String label() {
        return "ERC=" + value; 
    }

}
