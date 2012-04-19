package capps.gp.gptrees;

import java.util.Random; 
import java.util.List; 
import java.util.ArrayList; 

/**
 * Class storing a GPTree. The constructor will create a random GPTree 
 * (Currently a full tree.) It takes the depth, the method (FULL or GROW), and
 * a list of GPFunction classes and a list of GPTerminal classes. 
 *
 * It assumes a default constructor is defined for each node class type
 * passed in. 
 *
 **/
public class GPTree {

    public static enum METHOD {GROW, FULL}; 
    private GPNode root; 
    private List<Class<? extends GPFunction>> funcs; 
    private List<Class<? extends GPTerminal>> terms;
    private Random ranGen; 
    
    public GPTree() {
        this.root = null; 
    }

    public GPTree(int maxDepth, METHOD method, 
            List<Class<? extends GPFunction>> funcs, 
            List<Class<? extends GPTerminal>> terms) {

        ranGen = new Random(System.currentTimeMillis()); 

        this.funcs = funcs; 
        this.terms = terms; 
        this.root = recursiveGrow(maxDepth, method);    

    }

    public int getResult(GameState state) {
        return root.interpretForResult(state); 
    }

    /**
     * Only implemented FULL grow technique so far. 
     */
    private GPNode recursiveGrow(int depth, METHOD m) {

        try {

            if (depth==0) { //Return a random terminal node, uniform probability
                int choice = ranGen.nextInt(terms.size()); 
                GPTerminal term = null; 
                    term = terms.get(choice).newInstance(); 

                return term; 
            }

            /*Otherwise, choose a random function, and recursively
             * call recursiveGrow to attach function arguments*/

            int randFuncChoice = ranGen.nextInt(funcs.size()); 
            GPNode thisNode = null; 

            thisNode = funcs.get(randFuncChoice).newInstance(); 

            int numArgs = thisNode.numSubtrees(); 
            List<GPNode> subtrees = new ArrayList<GPNode>(); 

            for (int i = 0; i < numArgs; i++) {
                subtrees.add(recursiveGrow(depth-1, METHOD.FULL));     
            }
			
        	thisNode.attachSubtrees(subtrees); 
            return thisNode; 
        }

        catch (Exception e) {
           System.err.println("Failed to instantiate nodes in recursiveGrow.");  
           e.printStackTrace(); 
           System.exit(1); 
        }

        return null; 
    }

    public String toDot(String graphName) {
		StringBuilder dotStr = new StringBuilder(); 
		dotStr.append("graph " + graphName + "{" + "\n");
		 
		dotRecurse(root, dotStr, 0); 

		dotStr.append("}"); 
        return dotStr.toString();  
    }

	private void dotRecurse(GPNode node, StringBuilder dotStr, int nodeNo) {
		String id = "\"" + Integer.toString(nodeNo) + "\""; 
		String lab = "\"" + node.label() + "\""; 
		dotStr.append(id + " [label=" + lab + "];\n"); 

		if (node.subtrees() == null)
			return; 

		for (GPNode n: node.subtrees()) {
			dotStr.append(id + " -> \"" + (++nodeNo) + "\"\n");
			dotRecurse(n, dotStr, nodeNo); 
		}
	}


}
