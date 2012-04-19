package capps.gp.gptrees; 

/**
 * This is currently a dummy class to indicate that any implementing class
 * is a function node. Highly useful to divide nodes in terminals, non-terminals 
 * May think of some common functionality of all functions that I can implement 
 * here. 
 *
 **/

public abstract class GPFunction extends GPNode {

    public abstract int interpretForResult(GameState state); 
    public abstract int numSubtrees(); 
    public abstract String label();
}

