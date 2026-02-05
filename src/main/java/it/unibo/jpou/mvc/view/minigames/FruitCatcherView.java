package it.unibo.jpou.mvc.view.minigames;

import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FallingObject;
import javafx.scene.Parent;
import java.util.List;

/**
 * Interface for the visual representation of the Fruit Catcher minigame.
 */
public interface FruitCatcherView {

    /**
     * Renders the current state of the game.
     *
     * @param objects   the list of falling objects (apples, bombs) to draw.
     * @param score     the current score to display.
     * @param gameOver  true if the game is over.
     * @param playerX   the current X position of the player.
     */
    void render(List<FallingObject> objects, int score, boolean gameOver, double playerX);

    /**
     * Gets the JavaFX root node of this view.
     * Used by the MainController to switch the scene to this game.
     *
     * @return the root Parent node.
     */
    Parent getNode();
}
