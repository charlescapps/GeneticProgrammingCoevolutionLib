package capps.gp.gptrees; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import org.junit.*; 
import static org.junit.Assert.*; 

import java.util.List; 
import java.util.ArrayList; 

import capps.gp.gpcreatures.GenericCreature;
import capps.gp.gpglobal.GPConfig;

public class GPTreeTest {
	List<Class<? extends GPTerminal>> terms; 
	List<Class<? extends GPFunction>> funcs; 

	public GPTreeTest() {
		terms = new ArrayList<Class<? extends GPTerminal>>(); 
		funcs = new ArrayList<Class<? extends GPFunction>>(); 

		terms.add(ERCNode.class);

		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		try{
			GPConfig.loadConfig(new BufferedReader(
					new FileReader("configs/nonspatial.gp"))); 
		}
		catch (Exception e) {
			System.err.println("Exception creating GPTreeTest class."); 
		}
	}

    @Test
    public void testDotFile() {
		GPTree testTree = new GPTree(3, GPTree.METHOD.FULL, funcs, terms); 
		String dotStr = testTree.toDot("Test tree"); 
		System.out.println(dotStr); 
		BufferedWriter fileOut; 
		try {
			fileOut = new BufferedWriter(new FileWriter("dots/testdot.dot")); 
			fileOut.write(dotStr); 
			fileOut.close(); 
		}
		catch (IOException e) {
			System.out.println("Exception writing dot file\n"); 
		}

		System.out.println("Total nodes=" + testTree.totalNodes()); 
		System.out.println("Total funcs=" + testTree.numFuncNodes()); 
		System.out.println("Total terms=" + testTree.numTermNodes()); 
		System.out.println("Result=" + testTree.getResult(null)); 
    }

	@Test
	public void testCrossover() {
		GenericCreature c1 = new GenericCreature(3, funcs, terms); 
		GenericCreature c2 = new GenericCreature(3, funcs, terms); 

		FileWriter t1File;
		FileWriter t2File;
		FileWriter t1Crossed;
		FileWriter t2Crossed;
		
		try {	
			t1File = new FileWriter("dots/t1.dot"); 
			t2File = new FileWriter("dots/t2.dot"); 
			
			t1File.write(c1.getTree().toDot("T1")); 
			t2File.write(c2.getTree().toDot("T2")); 
			
			c1.crossover(c2); 

			t1Crossed = new FileWriter("dots/t1cross.dot"); 
			t2Crossed = new FileWriter("dots/t2cross.dot"); 

			t1Crossed.write(c1.getTree().toDot("T1 Crossed")); 
			t2Crossed.write(c2.getTree().toDot("T2 Crossed")); 

			t1File.close(); 
			t2File.close(); 

			t1Crossed.close(); 
			t2Crossed.close(); 
		}
		catch (IOException e) {

		}
	}

	@Test
	public void testCloning() throws IOException {
		GenericCreature c1 = new GenericCreature(3, funcs, terms); 
		GenericCreature cloneC1 = (GenericCreature)c1.clone(); 
		
		FileWriter f1 = new FileWriter("dots/original.dot"); 
		f1.write(c1.getTree().toDot("Original tree"));
		f1.close(); 
		FileWriter f2 = new FileWriter("dots/clone.dot"); 
		f2.write(cloneC1.getTree().toDot("Clone tree"));
		f2.close(); 
		
	}
  

}
