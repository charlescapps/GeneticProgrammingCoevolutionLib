package capps.gp.gptrees;

import java.util.Random; 
import java.util.List; 
import java.util.ArrayList; 

import capps.gp.gpglobal.GPConfig;

import capps.misc.MutableInt; 

/**
 * Class storing a GPTree and the types of nodes it can have. 
 * The constructor will create a random GPTree (Currently a full tree.) 
 * It takes the depth, the method (FULL or GROW), 
 * a list of GPFunction classes and a list of GPTerminal classes. 
 *
 * It assumes a default constructor is defined for each node class type
 * passed in. 
 *
 * Implemented generic function to produce a .dot file for graphviz. Highly
 * useful, because personally I wouldn't have any confidence in my code if I
 * couldn't visualize the trees. 
 **/
public class GPTree implements Cloneable {

    public static enum METHOD {GROW, FULL}; 
    private GPNode root; 
    private List<Class<? extends GPFunction>> funcs; 
    private List<Class<? extends GPTerminal>> terms;
    private Random ranGen; 
    
    public GPTree() {
		this.ranGen = GPConfig.getRandGen();
    }

	/**Store the types of allowed nodes, but don't grow a tree.*/
    public GPTree(List<Class<? extends GPFunction>> funcs, 
            List<Class<? extends GPTerminal>> terms) {

		ranGen = GPConfig.getRandGen();  
		assert (ranGen != null) : "Random generator is null in GPTree."; 

        this.funcs = funcs; 
        this.terms = terms; 
    }

    public GPTree(int maxDepth, METHOD method, 
            List<Class<? extends GPFunction>> funcs, 
            List<Class<? extends GPTerminal>> terms) {

		ranGen = GPConfig.getRandGen(); 
		assert (ranGen != null) : "Random generator is null in GPTree."; 

        this.funcs = funcs; 
        this.terms = terms; 
        this.root = recursiveGrow(maxDepth, method);    
    }
    
	public GPNode getRoot() {
		return root; 
	}

	public void setRoot(GPNode root) {
		this.root = root; 
	}

    public double getResult(GameState state) {
        return root.interpretForResult(state); 
    }

	public int totalNodes() {
		return totalNodesRecurse(root); 
	}

	public int getHeight() {
		return getHeightRecurse(root); 
	}

	private int getHeightRecurse(GPNode n) {
		if (n==null)
			return 0; 
		if (n.numSubtrees()==0)
			return 0; 

		int max = 0; 

		for (GPNode n2: n.getSubtrees()) {
			max = Math.max(max, getHeightRecurse(n2)); 
		}

		return 1+max; 
	}

	private int totalNodesRecurse(GPNode n) {
		if (n.numSubtrees() == 0) {
			return 1; 
		}

		int sum = 0; 

		for (GPNode c: n.getSubtrees()) {
			sum += totalNodesRecurse(c); 
		}
		return sum + 1; 
	}

	public int numTermNodes() {
		return numTermNodesRecurse(root); 
	}

	private int numTermNodesRecurse(GPNode n) {
		if (n.numSubtrees() == 0) {
			return 1; 
		}

		int sum = 0; 

		for (GPNode c: n.getSubtrees()) {
			sum += numTermNodesRecurse(c); 
		}
		return sum; 
	}

	public int numFuncNodes() {
		return numFuncNodesRecurse(root); 
	}

	private int numFuncNodesRecurse(GPNode n) {
		if (n.numSubtrees() == 0) { //Terminal nodes have 0 subtrees
			return 0; 
		}

		int sum = 0; 

		for (GPNode c: n.getSubtrees()) {
			sum += numFuncNodesRecurse(c); 
		}
		return sum + 1; 
	}

	public ParentChild getRandomSubtree() {
		assert (ranGen != null) : "Random generator is null in GPTree."; 
		int numNodes = totalNodes(); 
		int randomChoice = ranGen.nextInt(numNodes) + 1; 
		MutableInt nodeNum = new MutableInt(0); 
		//System.out.println("Random subtree choice = " + randomChoice); 

		if (randomChoice == 1) 
			return new ParentChild(null, -1); 

		return getRandomRecurse(root, nodeNum, randomChoice); 
	}

	private ParentChild getRandomRecurse(GPNode n, MutableInt nodeNum, int choice) {
		nodeNum.inc(); 	
		for (int i = 0; i < n.numSubtrees(); i++) {
			if (nodeNum.toInt() + 1 == choice) {
				return new ParentChild(n, i); 
			}
			ParentChild tmp = getRandomRecurse(n.getSubtrees().get(i), nodeNum, choice); 
			if (tmp != null) 
				return tmp; 
		}

		return null; 
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
		dotStr.append("digraph \"" + graphName + "\" {" + "\n");
		 
		dotRecurse(root, dotStr, "0"); 

		dotStr.append("}"); 
        return dotStr.toString();  
    }

	private void dotRecurse(GPNode node, StringBuilder dotStr, String nodeStr) {
		String id = "\"" + nodeStr + "\""; 
		String lab = "\"" + node.label() + "\""; 
		dotStr.append(id + " [label=" + lab + "];\n"); 

		if (node.getSubtrees() == null)
			return; 

		int i = 0; 

		for (GPNode n: node.getSubtrees()) {
			String childNodeStr = nodeStr + i++; 
			dotStr.append(id + " -> \"" + childNodeStr + "\";\n");
			dotRecurse(n, dotStr, childNodeStr); 
		}
	}

	/**
	 * You know, we don't have pass by reference in Java, so we need to get
	 * the parent node and the child we want to replace. 
	 *
	 * parent == null IFF the selected node is the root, because the root of a
	 * tree is the unique node without a parent. childIndex is undefined in this
	 * case and shouldn't be used. 
	 */
	public static class ParentChild {
		public ParentChild(GPNode p, int index) {
			this.parent = p; this.childIndex = index; 
		}

		public GPNode parent; 
		public int childIndex; 
	}

	@Override
	public Object clone() {
		GPTree theClone = new GPTree(); 
		theClone.funcs = this.funcs; 
		theClone.terms = this.terms; 
		theClone.root=cloneHelper(root); 
		return theClone; 	
	}

	private GPNode cloneHelper(GPNode current) {
		GPNode cloneNode = (GPNode)current.clone(); 
		if (GPTerminal.class.isInstance(cloneNode.getClass()) 
				|| current.getSubtrees()==null)
			return cloneNode; 

		List<GPNode> cloneSubtrees = new ArrayList<GPNode>(); 
		for (GPNode n: current.getSubtrees()) {
			cloneSubtrees.add(cloneHelper(n)); 
		}
		cloneNode.attachSubtrees(cloneSubtrees); 

		return cloneNode; 
	}
}
