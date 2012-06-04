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

public class MinichessBeatAB 
    extends MinichessCreature {

    protected final static AiInterface globalAbAi = new AbNegaMaxID(); 

    /** Constructs a new MinichessBeatAB. 
     *  Adds terminal nodes that are specific to Minichess. 
     */
    public MinichessBeatAB() {
        super(); 
    }

    public MinichessBeatAB( MinichessBeatAB c1) {
        super(c1); 
    }

	@Override
	public Object clone() {
		return new MinichessBeatAB(this); 
	}

	@Override
    public void computeFitness() {

        this.fitness = 0.0; 

        AiInterface opponent = MinichessBeatAB.globalAbAi; 

        //First play as white
        COLOR winner; 
        winner = SimplePlay.playGame(this.myMinichessAi, opponent); 
        if (winner == null) ;
        else if (winner == COLOR.WHITE)
            fitness += 1.0; 
        else 
            fitness -= 1.0; 

        winner = SimplePlay.playGame(opponent, this.myMinichessAi); 

        if (winner == null) ;
        else if (winner == COLOR.WHITE)
            fitness -= 1.0; 
        else 
            fitness += 1.0; 

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
