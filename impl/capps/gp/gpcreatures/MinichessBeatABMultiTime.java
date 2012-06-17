package capps.gp.gpcreatures; 

import capps.gp.gpgamestates.MinichessState;

import capps.gp.gpglobal.GPConfig;

import minichess.play.SimplePlay;
import minichess.COLOR; 
import minichess.ai.*; 
import minichess.boards.Board;

/**A creature whose fitness is determined by playing games against a 
 * random minichess AI. To improve performance there's a static
 * RandomAi class. 
 *
 * The config parameter TOURNY_SIZE determines how many games are played. */

public class MinichessBeatABMultiTime 
    extends MinichessCreature {

    protected final static AiInterface ab50ms = new AbNegaMaxID(50, 4); 
    protected final static AiInterface ab100ms = new AbNegaMaxID(100, 4); 
    protected final static AiInterface ab200ms = new AbNegaMaxID(200, 4); 
    protected final static AiInterface ab400ms = new AbNegaMaxID(400, 4); 
    protected final static AiInterface ab800ms = new AbNegaMaxID(800, 4); 

    /** Constructs a new MinichessBeatABMultiTime. 
     *  Adds terminal nodes that are specific to Minichess. 
     */
    public MinichessBeatABMultiTime() {
        super(); 
    }

    public MinichessBeatABMultiTime( MinichessBeatABMultiTime c1) {
        super(c1); 
    }

	@Override
	public Object clone() {
		return new MinichessBeatABMultiTime(this); 
	}

    private void playOneOpponent(AiInterface opponent) {
        COLOR winner; 
        //First play as white
        winner = SimplePlay.playGame(this.myMinichessAi, opponent); 
        if (winner == null) 
            ;
        else if (winner == COLOR.WHITE)
            fitness += 1.0; 
        else 
            fitness -= 1.0; 

        //Play as black
        winner = SimplePlay.playGame(opponent, this.myMinichessAi); 
        if (winner == null) 
            ;
        else if (winner == COLOR.BLACK)
            fitness += 1.0; 
        else 
            fitness -= 1.0; 

    }

	@Override
    public void computeFitness() {

        this.fitness = 0.0; 

        System.out.println("Computing fitness against Alpha-beta, 50ms / turn."); 
        ((AbNegaMaxID)myMinichessAi).setTimePerMove(50); 
        playOneOpponent(ab50ms); 
        System.out.println("Computing fitness against Alpha-beta, 100ms / turn."); 
        ((AbNegaMaxID)myMinichessAi).setTimePerMove(100); 
        playOneOpponent(ab100ms); 
        System.out.println("Computing fitness against Alpha-beta, 200ms / turn."); 
        ((AbNegaMaxID)myMinichessAi).setTimePerMove(200); 
        playOneOpponent(ab200ms); 
        System.out.println("Computing fitness against Alpha-beta, 400ms / turn."); 
        ((AbNegaMaxID)myMinichessAi).setTimePerMove(400); 
        playOneOpponent(ab400ms); 
        System.out.println("Computing fitness against Alpha-beta, 800ms / turn."); 
        ((AbNegaMaxID)myMinichessAi).setTimePerMove(800); 
        playOneOpponent(ab800ms); 

        this.p_isFitnessValid = true; 
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

}
