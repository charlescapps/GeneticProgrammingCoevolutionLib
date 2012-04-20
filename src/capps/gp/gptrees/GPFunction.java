package capps.gp.gptrees; 

/**
 * This is currently a dummy class to indicate that any implementing class
 * is a function node. Highly useful to divide nodes in terminals, non-terminals 
 * May think of some common functionality of all functions that I can implement 
 * here. 
 *
 **/

public abstract class GPFunction extends GPNode {

	@Override
    public abstract int interpretForResult(GameState state); 
	@Override
    public abstract int numSubtrees(); 
	@Override
    public abstract String label();
}

