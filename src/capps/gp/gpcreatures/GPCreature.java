package capps.gp.gpcreatures; 

import capps.gp.gpexceptions.FitnessNotComputed;
import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gptrees.GPTree; 
import capps.gp.gptrees.GPNode; 

public abstract class GPCreature {
	protected GPTree myGpTree; 
	protected boolean p_isFitnessValid; 
	protected int fitness; 

	public abstract void mutate(); 
	public abstract void crossover(GPCreature mate); //modifies both creatures

	public GPTree getTree() {
		return myGpTree; 
	}

	public boolean isFitnessValid() {
		return p_isFitnessValid; 
	}

	public int getFitness() throws InvalidFitnessException {
		if (!p_isFitnessValid) 
			throw new InvalidFitnessException(); 

		return fitness; 
	}

	public void setFitness(int fitness) {
		this.fitness = fitness; 
		this.p_isFitnessValid = true; 
	}

	public void invalidateFitness() {
		this.p_isFitnessValid = false; 
	}

	/**
	 * This method is the default way to perform a crossover. It randomly
	 * selects subtrees from each GPTree with uniform probability, then
	 * performs the crossover. 
	 * @param mate the creature to "mate" with
	 */
	public void genericCrossover(GPCreature mate) {
		GPTree t1 = this.myGpTree; 
		GPTree t2 = mate.myGpTree; 

		GPTree.ParentChild pc1 = t1.getRandomSubtree(); 
		GPTree.ParentChild pc2 = t2.getRandomSubtree(); 

		/**Swap the entire trees if the root was randomly selected for both.*/
		if (pc1.parent == null && pc2.parent == null) {
			GPNode tmp = t1.getRoot(); 
			t1.setRoot(t2.getRoot()); 
			t2.setRoot(tmp); 
			return;
		}
		/**Swap root of t1 with subtree of t2.*/
		if (pc1.parent == null) {
			GPNode tmp = t1.getRoot(); 
			t1.setRoot(pc2.parent.getSubtree(pc2.childIndex)); 
			pc2.parent.getSubtrees().set(pc2.childIndex, tmp); 
			return; 
		}
		/**Swap root of t2 with subtree of t1.*/
		if (pc2.parent == null) {
			GPNode tmp = t2.getRoot(); 
			t2.setRoot(pc1.parent.getSubtree(pc1.childIndex)); 
			pc1.parent.getSubtrees().set(pc1.childIndex, tmp); 
			return; 
		}
		/**Just swap 2 subtrees, no need to modify the root*/
		GPNode tmp = pc1.parent.getSubtree(pc1.childIndex);  

		pc1.parent.getSubtrees().set(pc1.childIndex, 
				pc2.parent.getSubtree(pc2.childIndex)); 
		pc2.parent.getSubtrees().set(pc2.childIndex,
				tmp); 
	}
}
