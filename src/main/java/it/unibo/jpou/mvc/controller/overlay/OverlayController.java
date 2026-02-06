package it.unibo.jpou.mvc.controller.overlay;

/**
 * Common interface for overlay controllers, defines the contract for game session termination.
 */
@FunctionalInterface
public interface OverlayController {

    /**
     * Shuts down the game session by saving.
     */
    void quit();
}
