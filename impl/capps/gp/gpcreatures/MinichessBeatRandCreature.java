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

public class MinichessBeatRandCreature 
    extends MinichessCreature {

    protected final static AiInterface globalRandomAi = new RandomAi(); 

    /** Constructs a new MinichessBeatRandCreature. 
     *  Adds terminal nodes that are specific to Minichess. 
     */
    public MinichessBeatRandCreature() {
        super(); 
    }

    public MinichessBeatRandCreature( MinichessBeatRandCreature c1) {
        super(c1); 
    }

	@Override
	public Object clone() {
		return new MinichessBeatRandCreature(this); 
	}

	@Override
    public void computeFitness() {

        this.fitness = 0.0; 

        AiInterface opponent = MinichessBeatRandCreature.globalRandomAi; 
        boolean playAsWhite = GPConfig.getRandGen().nextBoolean(); 
        int numOpponents = GPConfig.getTournySize() - 1; 

        for (int i = 0; i < numOpponents; i++) {

            COLOR winner = null;
            if (playAsWhite) {
                winner = SimplePlay.playGame(this.myMinichessAi, opponent); 
            }
            else {
                winner = SimplePlay.playGame(opponent, this.myMinichessAi); 
            }

            if (winner == null)
                continue;
            else if (playAsWhite && winner==COLOR.WHITE)
                fitness+=1.0; 
            else if (!playAsWhite && winner == COLOR.BLACK)
                fitness+=1.0; 
            else
                fitness-=1.0; 

        }

        this.p_isFitnessValid = true; 
    }


	@Override
	public double evaluateBoard(Board b) {
		return myGpTree.getResult(new MinichessState(b));
	}

}
