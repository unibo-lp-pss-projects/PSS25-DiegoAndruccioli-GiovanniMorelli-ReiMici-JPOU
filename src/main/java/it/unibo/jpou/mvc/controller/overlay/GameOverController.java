package it.unibo.jpou.mvc.controller.overlay;

/**
 * Controller specifically for the Game Over Overlay, handle restarting the game after death.
 */
public interface GameOverController extends OverlayController {

    /**
     * Restart the game session.
     */
    void restart();
}
