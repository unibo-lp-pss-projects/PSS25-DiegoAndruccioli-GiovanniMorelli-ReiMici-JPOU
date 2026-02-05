package it.unibo.jpou.mvc.view.minigames;

import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FallingObject;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import java.util.List;

/**
 * Interface defining the visual component of the Fruit Catcher game.
 */
public interface FruitCatcherView {

    /**
     * Renders a single frame of the game.
     *
     * @param objects   the list of falling items (apples, bombs) to draw.
     * @param score     the current score to display.
     * @param timeLeft  the remaining time in seconds.
     * @param gameOver  true if the "Game Over" screen should be shown.
     * @param playerX   the current X position of the player (Pou/Basket).
     */
    void render(List<FallingObject> objects, int score, double timeLeft, boolean gameOver, double playerX);

    /**
     * Sets the listener for keyboard events (Left/Right arrows).
     *
     * @param handler the event handler.
     */
    void setKeyListener(EventHandler<KeyEvent> handler);

    /**
     * Gets the JavaFX root node of this view.
     * Used by the MainController to switch the scene to this game.
     *
     * @return the root Parent node.
     */
    Parent getNode();

    /**
     * Requests focus for the view to ensure keyboard input is captured.
     * This is necessary because JavaFX KeyEvents require the node to be focused.
     */
    void requestFocus();
}
