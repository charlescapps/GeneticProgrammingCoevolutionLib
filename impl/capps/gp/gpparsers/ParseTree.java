package capps.gp.gpparsers; 

import java.util.ArrayList;
import java.util.List;

import capps.gp.gptrees.GPFunction;
import capps.gp.gptrees.GPTerminal;

public class ParseTree {
    //Functions and terms to look for when parsing
    private List<Class<? extends GPFunction>> funcs; 
    private List<Class<? extends GPTerminal>> terms; 

    //Need these instances to get the labels and
    //number of subtrees for each type of function/ terminal
    private List<GPFunction> funcInstances; 
    private List<GPTerminal> termInstances; 

    public ParseTree(List<Class<? extends GPFunction>> funcs, 
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

}
