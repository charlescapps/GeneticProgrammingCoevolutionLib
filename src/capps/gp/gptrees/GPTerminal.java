package capps.gp.gptrees; 

import java.util.List; 

/**
 * A GP Node that has no children. Used for GPTree to know how to randomly
 * generate trees of a fixed depth.
 */
public abstract class GPTerminal extends GPNode {

    public abstract int interpretForResult(GameState state); 

	@Override
    public int numSubtrees() { 
        return 0; 
    }

    public void attachSubtrees(List<GPNode> subtrees) {
        return;
    }

    public List<GPNode> getSubtrees() {
        return null;
    }

    public abstract String label(); 
}
