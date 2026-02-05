package it.unibo.jpou.mvc.controller;

/**
 * Interface for Game loop engine managing the time cycle.
 */
public interface GameLoop {

    /**
     * Starts the game engine execution.
     */
    void start();

    /**
     * Stops the engine and releases resources.
     */
    void shutdown();

    /**
     * @return true if the game is running
     */
    boolean isRunning();
}
