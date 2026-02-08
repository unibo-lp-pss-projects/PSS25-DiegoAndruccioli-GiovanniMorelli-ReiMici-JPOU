package it.unibo.jpou.mvc.controller.overlay;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.controller.gameloop.GameLoop;
import it.unibo.jpou.mvc.controller.persistence.PersistenceController;
import it.unibo.jpou.mvc.controller.gameloop.PouGameLoop;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.view.MainView;
import javafx.application.Platform;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of the PauseController.
 */
public final class PauseControllerImpl implements PauseController {

    private final GameLoop gameLoop;
    private final MainView mainView;
    private final PersistenceController persistenceController;
    private final Supplier<Room> currentRoomSupplier;

    /**
     * @param gameLoop              the game loop to control
     * @param mainView              the view to update
     * @param persistenceController the persistence controller for saving
     * @param currentRoomSupplier   supplier to get the current room for saving
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Dependency injection requires mutable references.")
    public PauseControllerImpl(final GameLoop gameLoop,
            final MainView mainView,
            final PersistenceController persistenceController,
            final Supplier<Room> currentRoomSupplier) {
        this.gameLoop = Objects.requireNonNull(gameLoop);
        this.mainView = Objects.requireNonNull(mainView);
        this.persistenceController = Objects.requireNonNull(persistenceController);
        this.currentRoomSupplier = Objects.requireNonNull(currentRoomSupplier);
    }

    @Override
    public void pause() {
        if (this.gameLoop instanceof PouGameLoop) {
            ((PouGameLoop) this.gameLoop).pause();
        } else if (this.gameLoop.isRunning()) {
            this.gameLoop.shutdown();
        }
        this.mainView.setPauseVisible(true);
    }

    @Override
    public void resume() {
        this.mainView.setPauseVisible(false);
        this.gameLoop.start();
    }

    @Override
    public void quit() {
        this.persistenceController.saveGame(this.currentRoomSupplier.get());
        this.gameLoop.shutdown();
        Platform.exit();
    }
}
