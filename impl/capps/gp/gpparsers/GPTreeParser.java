package capps.gp.gpparsers; 

import java.util.ArrayList;
import java.util.List;

import capps.gp.gptrees.ERCNode;
import capps.gp.gptrees.GPFunction;
import capps.gp.gptrees.GPNode;
import capps.gp.gptrees.GPTerminal;
import capps.gp.gptrees.GPTree;

import capps.misc.MutableInt;

public class GPTreeParser {

    private static String L_PAR = "("; 
    private static String R_PAR = ")"; 

    //Functions and terms to look for when parsing
    private List<Class<? extends GPFunction>> funcs; 
    private List<Class<? extends GPTerminal>> terms; 

    //Need these instances to get the labels and
    //number of subtrees for each type of function/ terminal
    private List<GPFunction> funcInstances; 
    private List<GPTerminal> termInstances; 

    public GPTreeParser(GPTree t) throws Exception {
        this.funcs = t.getFuncs();
        this.terms = t.getTerms(); 

        this.funcInstances = new ArrayList<GPFunction>(); 
        this.termInstances = new ArrayList<GPTerminal>(); 

        for (int n = 0; n < funcs.size(); n++) {
            funcInstances.add(funcs.get(n).newInstance()); 
        }

        for (int n = 0; n < terms.size(); n++) {
            termInstances.add(terms.get(n).newInstance()); 
        }
    }

    public GPTreeParser(List<Class<? extends GPFunction>> funcs, 
        List<Class<? extends GPTerminal>> terms) 
            throws Exception {

        this.funcs = funcs; 
        this.terms = terms; 

        this.funcInstances = new ArrayList<GPFunction>(); 
        this.termInstances = new ArrayList<GPTerminal>(); 

        for (int n = 0; n < funcs.size(); n++) {
            funcInstances.add(funcs.get(n).newInstance()); 
        }

        for (int n = 0; n < terms.size(); n++) {
            termInstances.add(terms.get(n).newInstance()); 
        }
    }

    public GPTree parseStringToTree(String treeStr) throws Exception {
        String[] tokens = treeStr.split("\\s+"); 

        GPNode root = parseRecurse(tokens, new MutableInt(0)); 
        GPTree result = new GPTree(funcs, terms); 
        result.setRoot(root); 
        return result; 
    }

    /**Recursively parse a text representation of a GPTree. 
     * Note that since a GPFunction class gives us the number of arguments,
     * there's no ambiguity so this format doesn't even need parentheses.*/

    private GPNode parseRecurse(String[] tokens, MutableInt index) throws Exception {
        GPNode match = findMatch(tokens[index.toInt()]); 

        if (match == null)
            throw new Exception("GP Parse error. Token '" 
                    + tokens[index.toInt()] + "', #" 
                    + index.toInt() + " is invalid"); 

        index.inc(); 

        if (GPTerminal.class.isInstance(match))
            return match; 

        int numSubtrees = match.numSubtrees();
        List<GPNode> subtreesToAttach = new ArrayList<GPNode>(); 

        for (int n = 0; n < numSubtrees; n++) {

            subtreesToAttach.add(parseRecurse(tokens, index));

        }

        match.attachSubtrees(subtreesToAttach); 

        return match; 

    }

    private GPNode findMatch(String token) throws Exception {

        for (int n = 0; n < funcs.size(); n++) {
            if (token.equals(funcInstances.get(n).label()))
                return funcs.get(n).newInstance(); 
        }

        for (int n = 0; n < terms.size(); n++) {
            //Special case for ERC nodes. 
            //Otherwise, we'd need something general like have every single
            //node class implement a 'fromString' function. 
            //It seems more reasonable to have a special case here, since
            //ERC nodes are the only ones that aren't all identical
            if (token.contains("ERC=")) {
                String numStr = token.substring(4); 
                return new ERCNode(Double.parseDouble(numStr)); 
            }
            if (token.equals(termInstances.get(n).label()))
                return terms.get(n).newInstance(); 
        }

        return null; 
    }

}
