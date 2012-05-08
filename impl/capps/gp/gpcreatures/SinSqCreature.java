package capps.gp.gpcreatures; 

import capps.gp.gptrees.*;

public class SinSqCreature extends FuncApproxCreature 
		implements Cloneable{	

	@Override
	public double func(double x) {
		return Math.sin(x)*Math.sin(x); 
	}

	public SinSqCreature() {
		super(); 
	}

	public SinSqCreature(boolean makeTree) {
		super(makeTree); 
	}

	@Override
	public Object clone() {
		SinSqCreature cloneC = new SinSqCreature(false); 
		cloneC.myGpTree = (GPTree)this.myGpTree.clone(); 
		
		return cloneC; 
	}

	@Override
	protected double xMin() {
		return -6.28;
	}

	@Override
	protected double xMax() {
		return 6.28;
	}

	@Override
	protected int numPoints() {
		return 40;
	}
}
