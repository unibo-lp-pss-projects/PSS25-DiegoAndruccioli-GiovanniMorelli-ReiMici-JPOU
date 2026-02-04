package it.unibo.jpou.mvc.controller.minigames;

/**
 * Controller interface for the Fruit Catcher minigame.
 * Handles the game loop and user input.
 */
public interface FruitCatcherController {

    /**
     * Starts the game loop.
     */
    void startGame();

    /**
     * Stops the game loop.
     */
    void stopGame();

    /**
     * Updates the player's position based on input.
     *
     * @param x the new horizontal position (e.g. from mouse).
     */
    void updatePlayerPosition(double x);
}
