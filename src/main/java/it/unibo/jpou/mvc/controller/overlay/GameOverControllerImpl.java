package it.unibo.jpou.mvc.controller.overlay;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.controller.GameLoop;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.view.MainView;
import javafx.application.Platform;

import java.util.Objects;

/**
 * Implementation of the GameOverController.
 */
public final class GameOverControllerImpl implements GameOverController {

    private final GameLoop gameLoop;
    private final MainView mainView;
    private final PouLogic model;

    /**
     * @param gameLoop the game loop to restart
     * @param mainView the view to update
     * @param model    the model to reset
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Dependency injection requires mutable references.")
    public GameOverControllerImpl(final GameLoop gameLoop, final MainView mainView, final PouLogic model) {
        this.gameLoop = Objects.requireNonNull(gameLoop);
        this.mainView = Objects.requireNonNull(mainView);
        this.model = Objects.requireNonNull(model);
    }

    @Override
    public void restart() {
        this.model.reset();
        this.mainView.setGameOverVisible(false);
        this.gameLoop.start();
    }

    @Override
    public void quit() {
        Platform.exit();
    }
}
