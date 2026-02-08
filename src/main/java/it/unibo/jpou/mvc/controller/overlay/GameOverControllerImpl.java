package it.unibo.jpou.mvc.controller.overlay;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.controller.persistence.PersistenceController;
import it.unibo.jpou.mvc.model.PouLogic;
import javafx.application.Platform;

import java.util.Objects;

/**
 * Implementation of the GameOverController.
 */
public final class GameOverControllerImpl implements GameOverController {

    private final PouLogic model;
    private final PersistenceController persistenceController;

    /**
     * @param model the model to reset
     * @param persistenceController the controller for data persistence
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency injection requires mutable references.")
    public GameOverControllerImpl(final PouLogic model,
            final PersistenceController persistenceController) {
        this.model = Objects.requireNonNull(model);
        this.persistenceController = Objects.requireNonNull(persistenceController);
    }

    @Override
    public void restart() {
        this.model.reset();
        this.persistenceController.saveGame(it.unibo.jpou.mvc.model.Room.BEDROOM);
        Platform.exit();
    }

    @Override
    public void quit() {
        Platform.exit();
    }
}
