package capps.gp.gptrees; 

import java.util.List; 

public abstract class GPNode {
    protected List<GPNode> subtrees;

    public abstract int interpretForResult(GameState state); 
    public abstract int numSubtrees(); 
    public void attachSubtrees(List<GPNode> subtrees) {
        this.subtrees = subtrees; 
    }
    public List<GPNode> subtrees() {
        return subtrees; 
    }

    public abstract String label();


}
