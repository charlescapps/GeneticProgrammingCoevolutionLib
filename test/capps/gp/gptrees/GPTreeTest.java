package capps.gp.gptrees; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
//		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
	}

    @Test
    public void testDotFile() {
		GPTree testTree = new GPTree(3, GPTree.METHOD.FULL, funcs, terms); 
		String dotStr = testTree.toDot("Test tree"); 
		System.out.println(dotStr); 
		BufferedWriter fileOut; 
		try {
			fileOut = new BufferedWriter(new FileWriter("testdot.dot")); 
			fileOut.write(dotStr); 
			fileOut.close(); 
		}
		catch (IOException e) {
			System.out.println("Exception writing dot file\n"); 
		}

		System.out.println("Result=" + testTree.getResult(null)); 

    }

  

}
