package capps.gp.gpcreatures; 

import java.util.List;

import capps.gp.gptrees.*; 

public class GenericCreature extends GPCreature {

	public GenericCreature(int maxDepth, 
			List<Class<? extends GPFunction>> funcs, 
			List<Class<? extends GPTerminal>> terms) {
		this.myGpTree = new GPTree(maxDepth, GPTree.METHOD.FULL, funcs, terms); 
	}

	public GenericCreature() { //Uninitialized creature
		//No way to instantiate a generic creature without a list of nodes
	}

	@Override
	public void computeFitness() {
		this.fitness = 0; 
		System.err.println("GenericCreature's fitness function shouldn't be used."); 
	}


	@Override
	public void mutate() {
        this.genericMutate();
	}

	@Override
	public void crossover(GPCreature mate) {
		this.genericCrossover(mate); 
	}

	@Override
	public GPCreature getOffspring(GPCreature c) {
		return genericGetOffspring(c); 
	}

	@Override
	public Object clone() {
		GenericCreature cloneC = new GenericCreature(); 
		cloneC.myGpTree = (GPTree)this.myGpTree.clone(); 
		
		return cloneC; 
	}

}
