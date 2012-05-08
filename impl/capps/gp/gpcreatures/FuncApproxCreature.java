package capps.gp.gpcreatures; 

import java.util.List; 
import java.util.ArrayList; 

import capps.gp.gpglobal.GPConfig;

import capps.gp.gptrees.*;
import static capps.gp.gptrees.VarNode.VarValues; 

public abstract class FuncApproxCreature extends GenericCreature 
		implements Cloneable{	

	protected double diffSquaredFitness; //Cache this result.
	protected double xInc; 

	public static final String X_VAR = "x"; 

	public abstract double func(double x); 
	protected abstract double xMin(); 
	protected abstract double xMax(); 
	protected abstract int numPoints(); 

	public FuncApproxCreature() {
		List<Class<? extends GPTerminal>> terms
			= new ArrayList<Class<? extends GPTerminal>>();

		List<Class<? extends GPFunction>> funcs
			= new ArrayList<Class<? extends GPFunction>>() ; 

		terms.add(ERCNode.class);
		terms.add(VarNode.class); 
		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		funcs.add(MaxFunc.class); 
		funcs.add(MinFunc.class); 

		this.xInc = (xMax() - xMin())/numPoints(); 
		this.myGpTree = new GPTree(GPConfig.getGpDepth(), GPTree.METHOD.FULL, funcs, terms); 
	}

	public FuncApproxCreature(boolean makeTree) {

		List<Class<? extends GPTerminal>> terms
			= new ArrayList<Class<? extends GPTerminal>>();

		List<Class<? extends GPFunction>> funcs
			= new ArrayList<Class<? extends GPFunction>>() ; 

		terms.add(ERCNode.class);
		terms.add(VarNode.class); 
		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		funcs.add(MaxFunc.class); 
		funcs.add(MinFunc.class); 

		this.xInc = (xMax() - xMin())/numPoints(); 
		if (makeTree) 
			this.myGpTree = new GPTree(GPConfig.getGpDepth(), GPTree.METHOD.FULL, funcs, terms); 
	}

	public double getValueOnX(double x) {
		VarValues xVal = new VarValues(); 
		xVal.addVarValue(X_VAR, x);  
		double gpTreeResult = myGpTree.getResult(xVal); 
		return gpTreeResult; 
	}

	public double getDiffSquared() {
		return diffSquaredFitness; 
	}

	public String getFuncGraph() {
		StringBuilder sb = new StringBuilder(); 
		double minX = xMin(); 
		double maxX = xMax(); 
		sb.append("Graph of target function in ["
				+ String.format("%1$.3f",minX) + "," 
				+ String.format("%1$.3f",maxX) +"]\n"); 

		for (double x = minX; x <= maxX; x+=xInc) {
			long graphHt = Math.round(func(x)*15.0); 
			for (int i = 0; i < graphHt; i++)
				sb.append('*'); 
			sb.append('\n'); 
		}
		return sb.toString(); 
	}

	public String getGraph() {
		StringBuilder sb = new StringBuilder(); 
		double minX = xMin(); 
		double maxX = xMax(); 
		sb.append("Graph of creature function in ["
				+ String.format("%1$.3f",minX) + "," 
				+ String.format("%1$.3f",maxX) +"]\n"); 

		for (double x = minX; x <= maxX; x+=xInc) {
			long graphHt = Math.round(getValueOnX(x)*15.0); 
			for (int i = 0; i < graphHt; i++)
				sb.append('*'); 
			sb.append('\n'); 
		}
		return sb.toString(); 
	}

	@Override
	public void computeFitness() {
		double diffSquared = 0.0; 
		double minX = xMin(); 
		double maxX = xMax(); 

		for (double x = minX; x < maxX; x+=xInc) {
			double gpTreeResult = getValueOnX(x); 
			double diff = gpTreeResult - func(x); 
			diffSquared += diff*diff; 
		}

		this.diffSquaredFitness = diffSquared; 
		this.fitness = 1.0/diffSquared; 
		int ht = myGpTree.getHeight(); 
		if (ht > 10) {
			double factor = 10./(double)ht; 
			fitness*=factor; 
		}
		this.p_isFitnessValid = true; 
	}

	public String printStats() {
		StringBuffer sb = new StringBuffer(); 
		sb.append("Evaluating on 20 datapoints.\n"); 
		final double minX = xMin(); 
		final double maxX = xMax(); 
		double diffSquared = 0.0; 

		for (double x = minX; x < maxX; x+=xInc) {
			double gpTreeResult = getValueOnX(x); 
			double diff = gpTreeResult - func(x); 
			diffSquared += diff*diff; 
			sb.append("X=" + String.format("%1$,.4f",x)); 
			sb.append("\tF(x)=" + String.format("%1$,.4f",gpTreeResult) + "\n"); 
			sb.append("\tTarget(x)=" + String.format("%1$,.4f",func(x)) + "\n"); 
			sb.append("\tDiff squared=" + String.format("%1$,.4f",diff*diff) + "\n"); 
		}
		sb.append("TOTAL = " + diffSquared); 

		return sb.toString(); 
	}

	@Override
	public abstract Object clone();
}
