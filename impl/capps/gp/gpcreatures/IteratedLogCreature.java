package capps.gp.gpcreatures; 

import java.util.List; 
import java.util.ArrayList; 

import capps.gp.gpglobal.GPConfig;

import capps.gp.gptrees.*;
import static capps.gp.gptrees.VarNode.VarValues; 

public class IteratedLogCreature extends FuncApproxCreature 
		implements Cloneable{	
	private double diffSquaredFitness; 

	public IteratedLogCreature() {
		super(); 
	}

	public IteratedLogCreature(boolean makeTree) {
		super(makeTree); 
	}

	@Override
	public double func(double x) {
		return Math.log(Math.log(x));  
	}

	@Override
	public Object clone() {
		IteratedLogCreature cloneC = new IteratedLogCreature(false); 
		cloneC.myGpTree = (GPTree)this.myGpTree.clone(); 
		
		return cloneC; 
	}


	@Override
	protected double xMin() {
		return 10.0;
	}

	@Override
	protected double xMax() {
		return 30.0;
	}

	@Override
	protected int numPoints() {
		return 40;
	}
}
