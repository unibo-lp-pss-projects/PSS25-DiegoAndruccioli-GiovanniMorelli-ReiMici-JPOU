package it.unibo.jpou.mvc.view.minigames;

import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FallingObject;
import java.util.List;

/**
 * Interface for the visual representation of the Fruit Catcher minigame.
 */
@FunctionalInterface //necessario per interfacce con un solo metood astratto
public interface FruitCatcherView {

    /**
     * Renders the current state of the game.
     *
     * @param objects  the list of falling objects to draw.
     * @param score    the current score.
     * @param gameOver true if the game is over (to show end screen), false otherwise.
     */
    void render(List<FallingObject> objects, int score, boolean gameOver);
}
