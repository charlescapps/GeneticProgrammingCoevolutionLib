package capps.gp.gptrees; 


public abstract class GPFunction extends GPNode {

    public abstract int interpretForResult(GameState state); 
    public abstract int numSubtrees(); 
    public abstract String label();
}

