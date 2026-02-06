package it.unibo.jpou.mvc.controller.overlay;

/**
 * Controller specifically for the pause overlay, handle pausing, resuming and quitting the game.
 */
public interface PauseController extends OverlayController {

    /**
     * Pauses the game loop and displays the pause overlay.
     */
    void pause();

    /**
     * Resumes the game loop and hides the pause overlay.
     */
    void resume();

}
