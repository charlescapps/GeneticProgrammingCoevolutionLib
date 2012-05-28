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

public class GPParseTest {
	List<Class<? extends GPTerminal>> terms; 
	List<Class<? extends GPFunction>> funcs; 

	public GPParseTest() {
		terms = new ArrayList<Class<? extends GPTerminal>>(); 
		funcs = new ArrayList<Class<? extends GPFunction>>(); 

		terms.add(ERCNode.class);

		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		funcs.add(MaxFunc.class); 
		funcs.add(MinFunc.class); 
		funcs.add(IfLessThanFunc.class); 

		try{
			GPConfig.loadConfig(new BufferedReader(
					new FileReader("gp_configs/sinsqrd_spatial.gp"))); 
		}
		catch (Exception e) {
			System.err.println("Exception creating GPTreeTest class."); 
            e.printStackTrace(); 
		}
	}

    @Test 
    public void writeRandomTree() throws IOException {
        FileWriter fw = new FileWriter("test/random_tree.txt"); 
		GPTree testTree = new GPTree(3, GPTree.METHOD.FULL, funcs, terms); 

        fw.write(testTree.toString()); 
        fw.close(); 

        fw = new FileWriter("test/random_tree.dot"); 
        fw.write(testTree.toDot("some test tree")); 
        fw.close(); 


    }

}
