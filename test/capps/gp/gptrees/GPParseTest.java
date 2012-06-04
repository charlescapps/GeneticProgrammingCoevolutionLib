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
import capps.gp.gpcreatures.MinichessCreature;
import capps.gp.gpglobal.GPConfig;

import capps.gp.gpparsers.GPTreeParser;

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
    public void parseRandomTree() throws Exception {
        FileWriter fw = new FileWriter("text_creatures/tree_to_parse.txt"); 
		GPTree testTree = new GPTree(3, GPTree.METHOD.FULL, funcs, terms); 

        fw.write(testTree.toString()); 
        fw.close(); 

        fw = new FileWriter("dots/tree_to_parse.dot"); 
        fw.write(testTree.toDot("tree to parse")); 
        fw.close(); 

        GPTreeParser parser = new GPTreeParser(testTree); 
        
        GPTree parsedTree = parser.parseStringToTree(testTree.toString()); 

        fw = new FileWriter("dots/parsed_tree.dot"); 
        fw.write(parsedTree.toDot("parsed")); 
        fw.close(); 


    }

    @Test 
    public void parseRandomMinichessCreature() throws Exception {
        FileWriter fw = new FileWriter("text_creatures/mc_to_parse.txt"); 
        MinichessCreature mc = new MinichessCreature(); 

        fw.write(mc.toString()); 
        fw.close(); 

        fw = new FileWriter("dots/mc_to_parse.dot"); 
        fw.write(mc.toDot()); 
        fw.close(); 

        MinichessCreature parsedCreature = new MinichessCreature(mc.toString()); 

        fw = new FileWriter("dots/parsed_mc.dot"); 
        fw.write(parsedCreature.toDot()); 
        fw.close(); 

    }
}
