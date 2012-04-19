package capps.gp.gptrees; 

import java.util.List; 

public abstract class GPFunction extends GPNode {

    public abstract int interpretForResult(GameState state); 
    public abstract int numSubtrees(); 
    public abstract void attachSubtrees(List<GPNode> subtrees); 
    public abstract List<GPNode> subtrees(); 
    public abstract String label();
}

