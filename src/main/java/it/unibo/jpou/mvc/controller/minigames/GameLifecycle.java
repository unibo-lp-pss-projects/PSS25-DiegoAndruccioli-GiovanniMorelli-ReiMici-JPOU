package it.unibo.jpou.mvc.controller.minigames;

/**
 * Common interface for any game loop or minigame logic.
 * Defines the standard lifecycle methods.
 */
public interface GameLifecycle {

    /**
     * Starts the game loop.
     */
    void startGame();

    /**
     * Stops the game loop and cleans up resources.
     */
    void stopGame();

    /**
     * Checks if the game is currently active.
     *
     * @return true if the game is running, false otherwise.
     */
    boolean isRunning();
}
