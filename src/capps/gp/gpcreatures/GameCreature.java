package capps.gp.gpcreatures; 

import java.util.List;

/**
 * Simple Creature that computes fitness by playing some 'game' to be defined 
 * by subclass against a list of opponents. 
 */
public abstract class GameCreature extends GenericCreature {
	public static enum RESULT {WIN, LOSS, DRAW}; 

	private List<GameCreature> opponents; 

	public abstract RESULT play(GameCreature c); 

	/**Must call this function before attempting to compute the fitness.*/
	public void setOpponents(List<GameCreature> opps) {
		opponents = opps; 
	}

    public List<GameCreature> getOpponents() {
        return opponents;
    }

	@Override
	public void computeFitness() {
		assert (opponents != null && opponents.size() > 0); 
		this.fitness = 0.0; 
		for (GameCreature c: opponents) {
			RESULT r = this.play(c); 
			if (r == RESULT.WIN)
				fitness+=1.0;
			else if (r== RESULT.LOSS)
				fitness-=1.0; 
		}
		p_isFitnessValid = true; 
	}
}
