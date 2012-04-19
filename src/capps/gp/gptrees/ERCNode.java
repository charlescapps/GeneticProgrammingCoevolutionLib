package capps.gp.gptrees; 

public class ERCNode extends GPTerminal{
    private int value; 

	public ERCNode() {
		float r = (float) Math.random()*64.0f;
        value = Math.round(r - 32.0f); //Value from -32 to 31
    }

	@Override
    public int interpretForResult(GameState state) {
        return value; 
    }

	@Override
    public String label() {
        return "ERC=" + value; 
    }

}
