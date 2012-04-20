package capps.gp.gptrees; 

import java.util.List; 

/**
 * The root of the GP Node hierarchy. Any node has a list of subtrees, 
 * can be interpreted to return an integer, and can have subtrees 
 * attached to it. 
 *
 * Every GPNode must implement label() so that a dot file can be produced
 * for visualizing a GP tree. 
 *
 */

public abstract class GPNode {
    protected List<GPNode> subtrees;

    public abstract int interpretForResult(GameState state); 
    public abstract int numSubtrees(); 

    public void attachSubtrees(List<GPNode> subtrees) {
		assert(subtrees.size() == this.numSubtrees()); 
        this.subtrees = subtrees; 
    }
    public List<GPNode> getSubtrees() {
        return subtrees; 
    }

	public GPNode getSubtree(int n) {
		assert (subtrees != null && n >= 0 && n < subtrees.size()); 
		return subtrees.get(n); 
	}

    public abstract String label();
}
