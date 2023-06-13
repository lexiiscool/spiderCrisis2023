package Main;

/**
 * Date: June 6, 2023
 * <p>
 * Version 1.0 of the Score Class of "Spider Crisis". Used to control the user's score in the game.
 * </p>
 * <h2>Course: ICS4U0 with V. Krasteva</h2>
 *
 * @author Ethan Andrew
 * @version 23.06.06
 */
public class Score {
    /**
     * The user's score.
     */
    private static int totalScore = 0;

    /**
     * Adds to the score.
     * @param add The amount to add.
     */
    public void addScore(int add) {
    	//Modified by Lexi Han: Made sure that score would remain non-negative.
        totalScore = Math.max(0,  totalScore + add);
    }
    
    /**
     * Method to reset the score. Added by Lexi Han on June 7, 2023
     */
    public void resetScore() {
    	totalScore = 0;
    }

    /**
     * Provides the score.
     * @return The current score.
     */
    public int getScore() {
        return totalScore;
    }
}