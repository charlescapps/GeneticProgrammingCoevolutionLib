package minichess.ai;

import java.util.List;

import minichess.*;
import minichess.boards.*;
import minichess.heuristic.HeuristicInterface;

public class RandomAi implements AiInterface {
	private COLOR myColor;
    private java.util.Random myRandomGen; 
	
	public RandomAi() {
        this.myRandomGen = new java.util.Random(); 
        this.myColor = COLOR.WHITE; 
	}

	public RandomAi(COLOR c) {
        this.myRandomGen = new java.util.Random(); 
		this.myColor = c; 
	}

	public void setColor(COLOR c) {
		this.myColor = c; 
	}
	
	@Override
	public Move makeMove(Board b) {
        List<Move> allValidMoves = b.getAllValidMoves(); 
        int choice = myRandomGen.nextInt(allValidMoves.size()); 
        return allValidMoves.get(choice); 
    }


	@Override
	public COLOR getColor() {
		return myColor;
	}

	@Override
	public HeuristicInterface getHeuristic() {
		// TODO Auto-generated method stub
		return null; 
	}

	@Override
	public void setHeuristic(HeuristicInterface hi) {
		// TODO Auto-generated method stub
		
	}

	
}
