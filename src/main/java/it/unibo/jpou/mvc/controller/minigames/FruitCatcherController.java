package it.unibo.jpou.mvc.controller.minigames;

/**
 * Interface for the Fruit Catcher minigame controller.
 * Extends the basic lifecycle with input handling specific to this game.
 */
public interface FruitCatcherController extends GameLifecycle {

    /**
     * Updates the player's horizontal position based on input.
     *
     * @param x the new X coordinate.
     */
    void updatePlayerPosition(double x);
}
