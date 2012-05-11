package capps.gp.gpcreatures; 

import capps.gp.gptrees.*;

public class SinCreature extends FuncApproxCreature 
		implements Cloneable{	

	@Override
	public double func(double x) {
		return 1.0 + Math.sin(x); 
	}

	public SinCreature() {
		super(); 
	}

	public SinCreature(boolean makeTree) {
		super(makeTree); 
	}

	@Override
	public Object clone() {
		SinCreature cloneC = new SinCreature(false); 
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
