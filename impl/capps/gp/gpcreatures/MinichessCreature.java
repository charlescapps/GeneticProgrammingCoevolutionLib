package capps.gp.gpcreatures; 

import java.util.ArrayList;
import java.util.List;

import capps.gp.gpgamestates.MinichessState;

import capps.gp.gpglobal.GPConfig;

import capps.gp.gpparsers.GPTreeParser;
import capps.gp.gptrees.*; 

import minichess.play.SimplePlay;
import minichess.COLOR; 
import minichess.ai.*; 
import minichess.boards.Board;
import minichess.heuristic.*; 
import minichess.config.MinichessConfig;

public class MinichessCreature 
    extends GameCreature implements HeuristicInterface {

    protected AiInterface myMinichessAi; 

    /** Constructs a new MinichessCreature. 
     *  Adds terminal nodes that are specific to Minichess. 
     */
    public MinichessCreature() {
		List<Class<? extends GPTerminal>> terms
			= new ArrayList<Class<? extends GPTerminal>>();

		List<Class<? extends GPFunction>> funcs
			= new ArrayList<Class<? extends GPFunction>>() ; 

		terms.add(ERCNode.class);
        terms.add(NumWhitePawns.class); 
        terms.add(NumBlackPawns.class); 
        terms.add(NumWhiteKnights.class); 
        terms.add(NumBlackKnights.class); 
        terms.add(NumWhiteBishops.class); 
        terms.add(NumBlackBishops.class); 
        terms.add(NumWhiteQueens.class); 
        terms.add(NumBlackQueens.class); 
        terms.add(WhitePawnsInCenter.class); 
        terms.add(BlackPawnsInCenter.class); 
        terms.add(WhiteBishopsOffBack.class);
        terms.add(BlackBishopsOffBack.class);
        terms.add(WhiteRookOpenFile.class);
        terms.add(BlackRookOpenFile.class);

		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		funcs.add(MaxFunc.class); 
		funcs.add(MinFunc.class); 
		funcs.add(IfLessThanFunc.class); 

		this.myGpTree = new GPTree(GPConfig.getGpDepth(), GPTree.METHOD.FULL, funcs, terms); 

        this.myMinichessAi = new AbNegaMaxID(MinichessConfig.getTimePerMove(), 
                MinichessConfig.getIterativeDeepMinDepth()); 

        this.myMinichessAi.setHeuristic(this); 

    }

    public MinichessCreature( MinichessCreature c1) {
        this.myGpTree = (GPTree)c1.myGpTree.clone(); 
        this.myMinichessAi = new AbNegaMaxID(MinichessConfig.getTimePerMove(), 
                MinichessConfig.getIterativeDeepMinDepth()); 
        this.myMinichessAi.setHeuristic(this); 
    }

    public MinichessCreature(String textCreature) throws Exception {
		List<Class<? extends GPTerminal>> terms
			= new ArrayList<Class<? extends GPTerminal>>();

		List<Class<? extends GPFunction>> funcs
			= new ArrayList<Class<? extends GPFunction>>() ; 

		terms.add(ERCNode.class);
        terms.add(NumWhitePawns.class); 
        terms.add(NumBlackPawns.class); 
        terms.add(NumWhiteKnights.class); 
        terms.add(NumBlackKnights.class); 
        terms.add(NumWhiteBishops.class); 
        terms.add(NumBlackBishops.class); 
        terms.add(NumWhiteQueens.class); 
        terms.add(NumBlackQueens.class); 
        terms.add(WhitePawnsInCenter.class); 
        terms.add(BlackPawnsInCenter.class); 
        terms.add(WhiteBishopsOffBack.class);
        terms.add(BlackBishopsOffBack.class);
        terms.add(WhiteRookOpenFile.class);
        terms.add(BlackRookOpenFile.class);

		funcs.add(PlusFunc.class); 
		funcs.add(MultFunc.class); 
		funcs.add(DivideFunc.class); 
		funcs.add(MinusFunc.class); 
		funcs.add(MaxFunc.class); 
		funcs.add(MinFunc.class); 
		funcs.add(IfLessThanFunc.class); 

        GPTreeParser parser = new GPTreeParser(funcs, terms); 

        this.myMinichessAi = new AbNegaMaxID(MinichessConfig.getTimePerMove(), 
                MinichessConfig.getIterativeDeepMinDepth()); 

        this.myMinichessAi.setHeuristic(this); 

		this.myGpTree = parser.parseStringToTree(textCreature); 
    }

	@Override
	public Object clone() {
		return new MinichessCreature(this); 
	}

	@Override
	public RESULT play(GameCreature c) {
        MinichessCreature opponent = (MinichessCreature) c; 
        boolean playAsWhite = GPConfig.getRandGen().nextBoolean(); 

        COLOR winner = null;
        if (playAsWhite) {
            winner = SimplePlay.playGame(this.myMinichessAi, opponent.myMinichessAi); 
        }
        else {
            winner = SimplePlay.playGame(opponent.myMinichessAi, this.myMinichessAi); 
        }

        if (winner == null)
            return RESULT.DRAW; 
        else if (playAsWhite && winner==COLOR.WHITE)
            return RESULT.WIN; 
        else if (!playAsWhite && winner==COLOR.BLACK)
            return RESULT.WIN; 
        else 
            return RESULT.LOSS;
	}

	@Override
	public double evaluateBoard(Board b) {
        if (b.isGameOver()) {
            COLOR winner = b.getWinner(); 
            if (winner == null)
                return 0.0; 
            if (winner == COLOR.BLACK)
                return AiInterface.MIN_SCORE; 
            if (winner == COLOR.WHITE)
                return AiInterface.MAX_SCORE; 
        }
		return myGpTree.getResult(new MinichessState(b));
	}

    @Override
    public String toString() {
        return myGpTree.toString(); 
    }

    public String toDot() {
        return myGpTree.toDot("Minichess creature"); 
    }

}
