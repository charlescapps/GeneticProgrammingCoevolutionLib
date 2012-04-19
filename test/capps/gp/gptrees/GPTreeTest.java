package capps.gp.gptrees; 

import org.junit.*; 
import static org.junit.Assert.*; 

import java.util.List; 
import java.util.ArrayList; 

public class GPTreeTest {
	List<Class<? extends GPTerminal>> terms; 
	List<Class<? extends GPFunction>> funcs; 

	public GPTreeTest() {
		terms = new ArrayList<Class<? extends GPTerminal>>(); 
		funcs = new ArrayList<Class<? extends GPFunction>>(); 

		terms.add(ERCNode.class);
	}

    @Test
    public void testDotFile() {
		GPTree testTree = new GPTree(4, GPTree.METHOD.FULL, funcs, terms); 
    }

  

}
