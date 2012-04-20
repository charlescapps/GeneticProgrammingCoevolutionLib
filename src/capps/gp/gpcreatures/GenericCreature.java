package capps.gp.gpcreatures; 

import java.util.List;

import capps.gp.gptrees.*; 

public class GenericCreature extends GPCreature{

	public GenericCreature(int maxDepth, 
			List<Class<? extends GPFunction>> funcs, 
			List<Class<? extends GPTerminal>> terms) {
		this.myGpTree = new GPTree(maxDepth, GPTree.METHOD.FULL, funcs, terms); 
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void crossover(GPCreature mate) {
		this.genericCrossover(mate); 
	}


}
