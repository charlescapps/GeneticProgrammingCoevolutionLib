package capps.gp.gptrees; 

public class ERCNode extends GPTerminal{
    private int value; 

	public ERCNode() {
		float r = (float) Math.random()*255.0f;
        value = Math.round(r - 128.0f); //Value from -128 to 127
    }

    public int interpretForResult(GameState state) {
        return value; 
    }

    public String label() {
        return "ERC=" + value; 
    }

}
