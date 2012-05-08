package capps.gp.gpcreatures; 

import java.util.List; 
import java.util.ArrayList; 

import capps.gp.gpglobal.GPConfig;

import capps.gp.gptrees.*;
import static capps.gp.gptrees.VarNode.VarValues; 

public class IteratedLogCreature extends GenericCreature 
		implements Cloneable{	
	private double diffSquaredFitness; 
	public static final String X_VAR = "x"; 

	public static final double xLower = 10.0; 
	public static final double xUpper = 100.0; 
	public static final double xInc = 90.0/20.0;
	public static final int numPoints = 20; 

	public static double func(double x) {
		return Math.log(Math.log(x));  
	}

	public IteratedLogCreature() {
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

		this.myGpTree = new GPTree(GPConfig.getGpDepth(), GPTree.METHOD.FULL, funcs, terms); 
	}

	public IteratedLogCreature(boolean makeTree) {

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

	@Override
	public void computeFitness() {
		double diffSquared = 0.0; 

		for (double x = xLower; x < xUpper; x+=xInc) {
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
		final int numPoints = 20; 
		final double minX = -6.28; 
		final double maxX = 6.28; 
		final double inc = (maxX - minX) / (double) numPoints; 
		double diffSquared = 0.0; 

		for (double x = minX; x < maxX; x+=inc) {
			double gpTreeResult = getValueOnX(x); 
			double diff = gpTreeResult - func(x); 
			diffSquared += diff*diff; 
			sb.append("X=" + String.format("%1$,.4f",x)); 
			sb.append("\tF(x)=" + String.format("%1$,.4f",gpTreeResult) + "\n"); 
			sb.append("\t[sin(x)]^2=" + String.format("%1$,.4f",func(x)) + "\n"); 
			sb.append("\tDiff squared=" + String.format("%1$,.4f",diff*diff) + "\n"); 
		}
		sb.append("TOTAL = " + diffSquared); 

		return sb.toString(); 
	}

	@Override
	public Object clone() {
		IteratedLogCreature cloneC = new IteratedLogCreature(false); 
		cloneC.myGpTree = (GPTree)this.myGpTree.clone(); 
		
		return cloneC; 
	}
}
