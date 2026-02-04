package it.unibo.jpou.mvc.model.minigames;

/**
 * Interface defining the contract for any minigame within the application.
 * It manages the game lifecycle, including the main loop, scoring system, and game-over conditions.
 */
public interface Minigame {

    /**
     * Initializes the game state, resetting the score, player position, and other variables.
     */
    void startGame();

    /**
     * The main game loop executed by the animation timer.
     * This method updates the physics and logic of the game (e.g., movement, collisions) at every frame.
     *
     * @param now the timestamp of the current frame in nanoseconds.
     */
    void gameLoop(long now);

    /**
     * Retrieves the current raw score of the game.
     *
     * @return the player's score.
     */
    int getScore();

    /**
     * Checks if the game has ended.
     *
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Calculates the amount of currency (coins) awarded based on the score.
     *
     * @return the number of coins to be added to the player's balance.
     */
    int calculateCoins();
}
