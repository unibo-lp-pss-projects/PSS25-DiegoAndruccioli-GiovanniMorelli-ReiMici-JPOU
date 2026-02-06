package it.unibo.jpou.mvc.controller.minigames;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FruitCatcherGame;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherView;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import java.util.function.Consumer;

/**
 * Concrete implementation of the Fruit Catcher controller.
 * It connects the minigame logic, the view, and communicates results via callbacks.
 */
public final class FruitCatcherControllerImpl implements FruitCatcherController {

    private final FruitCatcherGame model;
    private final FruitCatcherView view;
    private final Consumer<Integer> coinAwarder;
    private final InternalLoop internalLoop;
    private boolean coinsAwarded;
    private boolean running;

    /**
     * Creates a new controller for the Fruit Catcher game.
     *
     * @param view        the game view.
     * @param coinAwarder a function to handle the coins earned (e.g. adding them to Pou).
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "View must be stored to be updated")
    public FruitCatcherControllerImpl(final FruitCatcherView view,
                                      final Consumer<Integer> coinAwarder) {
        this.model = new FruitCatcherGame();
        this.view = view;
        this.coinAwarder = coinAwarder;
        this.internalLoop = new InternalLoop();
        this.coinsAwarded = false;
        this.running = false;

        this.view.setKeyListener(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (model.isGameOver() && !coinsAwarded) {
                    awardCoins();
                }
                shutdown();
                return;
            }
            if (!isRunning()) {
                return;
            }

            final double currentX = model.getPlayerX();
            final double speed = 40.0;

            if (event.getCode() == KeyCode.LEFT) {
                updatePlayerPosition(currentX - speed);
            } else if (event.getCode() == KeyCode.RIGHT) {
                updatePlayerPosition(currentX + speed);
            }
        });

        this.view.requestFocus();
    }

    @Override
    public void start() {
        this.model.startGame();
        this.coinsAwarded = false;
        this.running = true;
        this.view.requestFocus();
        this.internalLoop.start();
    }

    @Override
    public void shutdown() {
        this.running = false;
        this.internalLoop.stop();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void updatePlayerPosition(final double x) {
        this.model.setPlayerPosition(x);
    }

    private void awardCoins() {
        final int earnedCoins = model.calculateCoins();
        coinAwarder.accept(earnedCoins);

        coinsAwarded = true;
    }

    /**
     * Inner class extending AnimationTimer to handle the frame-by-frame updates.
     */
    private final class InternalLoop extends AnimationTimer {
        @Override
        public void handle(final long now) {
            if (model.isGameOver()) {
                shutdown();

                view.render(model.getFallingObjects(), model.getScore(), model.getTimeLeft(), true, model.getPlayerX());
                return;
            }

            model.gameLoop(now);

            view.render(model.getFallingObjects(), model.getScore(), model.getTimeLeft(), false, model.getPlayerX());
        }
    }
}
