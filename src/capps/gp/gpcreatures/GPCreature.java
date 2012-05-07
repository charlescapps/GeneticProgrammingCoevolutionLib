package capps.gp.gpcreatures; 

import capps.gp.gpexceptions.InvalidFitnessException;

import capps.gp.gptrees.GPTree; 
import capps.gp.gptrees.GPNode; 

/** Class representing a "creature" in a population of GP algorithms. 
 * This is distinct from GPTree for a number of reasons. For one, it holds
 * additional information such as fitness. Might need to hold domain-specific
 * information as well. 
 *
 * Idea here is to implement as much as we can in the root of the hierarchy.
 * So genericCrossover is implemented here. This just uniformly chooses a
 * random subtree from 2 GPCreatures and swaps them. genericGetOffspring is also
 * implemented here. It does the same thing, but without modifying the original
 * parents returns the first parent after it is modified by crossover.
 *
 * Not sure <b>where</b> fitness calculation should take place. In Population
 * class? In creature class? Decided on the following paradigm: 
 *
 * --Pass a creature the data it needs to get its fitness. This could be a list
 *  of opponents for a tournament, or whatever. 
 *
 *  	-For example, void setOpponents(List<GPCreature>)
 *
 * --Creature must implement function computeFitness()
 * --Then the Population class can call getFitness()
 */

public abstract class GPCreature implements Cloneable, Comparable<GPCreature>{
	protected GPTree myGpTree; 
	protected boolean p_isFitnessValid; 
	protected double fitness; 

	public abstract void mutate(); 
	public abstract void crossover(GPCreature mate); //modifies both creatures

	/**without modifying the parents, produce offspring in some manner*/
	public abstract GPCreature getOffspring(GPCreature mate); 

	public GPTree getTree() {
		return myGpTree; 
	}

	public boolean isFitnessValid() {
		return p_isFitnessValid; 
	}

	public double getFitness() throws InvalidFitnessException {
		if (!p_isFitnessValid) 
			throw new InvalidFitnessException(); 

		return fitness; 
	}

	@Override
	public int compareTo(GPCreature c2) {
		return (this.fitness > c2.fitness ? 1 : 
				(this.fitness == c2.fitness ? 0 : -1)); 
	}

	public abstract void computeFitness();

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

	/**Default implementation of producing offspring. 
	 * Make clones of parents, then return result of crossover*/
	public GPCreature genericGetOffspring(GPCreature mate) {
		GPCreature clone1 = (GPCreature)this.clone();
		GPCreature clone2 = (GPCreature)mate.clone();
		clone1.genericCrossover(clone2); 
		return clone1;
	}

	@Override
	public abstract Object clone(); 
}
