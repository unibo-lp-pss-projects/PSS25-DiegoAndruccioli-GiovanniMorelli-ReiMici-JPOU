package it.unibo.jpou.mvc.controller.minigames;

import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FruitCatcherGame;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherView;
import javafx.animation.AnimationTimer;
import java.util.function.Consumer;

/**
 * Concrete implementation of the Fruit Catcher controller.
 * It connects the minigame logic, the view, and communicates results via callbacks.
 */
public final class FruitCatcherControllerImpl implements FruitCatcherController {

    private final FruitCatcherGame model;
    private final FruitCatcherView view;
    private final Consumer<Integer> coinAwarder;
    private final GameLoop gameLoop;
    private boolean coinsAwarded;

    /**
     * Creates a new controller for the Fruit Catcher game.
     *
     * @param view        the game view.
     * @param coinAwarder a function to handle the coins earned (e.g. adding them to Pou).
     */
    public FruitCatcherControllerImpl(final FruitCatcherView view,
                                      final Consumer<Integer> coinAwarder) {
        this.model = new FruitCatcherGame(); //fix spotbugs
        this.view = view;
        this.coinAwarder = coinAwarder;
        this.gameLoop = new GameLoop();
        this.coinsAwarded = false;
    }

    @Override
    public void startGame() {
        this.model.startGame();
        this.coinsAwarded = false;
        this.gameLoop.start();
    }

    @Override
    public void stopGame() {
        this.gameLoop.stop();
    }

    @Override
    public void updatePlayerPosition(final double x) {
        this.model.setPlayerPosition(x);
    }

    /**
     * Inner class extending AnimationTimer to handle the frame-by-frame updates.
     */
    private final class GameLoop extends AnimationTimer {
        @Override
        public void handle(final long now) {
            // logica di Game Over
            if (model.isGameOver()) {
                stopGame();

                if (!coinsAwarded) {
                    awardCoins();
                }

                view.render(model.getFallingObjects(), model.getScore(), true, model.getPlayerX());
                return;
            }

            // fisica
            model.gameLoop(now);

            // grafica
            view.render(model.getFallingObjects(), model.getScore(), false, model.getPlayerX());
        }

        private void awardCoins() {
            final int earnedCoins = model.calculateCoins();
            if (earnedCoins > 0) {
                coinAwarder.accept(earnedCoins);
            }
            coinsAwarded = true;
        }
    }
}
