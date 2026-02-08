package it.unibo.jpou.mvc.controller.room;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.controller.GameLoop;
import it.unibo.jpou.mvc.controller.minigames.FruitCatcherController;
import it.unibo.jpou.mvc.controller.minigames.FruitCatcherControllerImpl;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherJavaFXView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherView;
import it.unibo.jpou.mvc.view.room.GameRoomView;
import javafx.application.Platform;

import java.util.logging.Logger;

/**
 * Controller responsible for the GameRoom and starting minigames.
 */
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Controller must store references to Model and View passed by DI."
)
public final class GameRoomController {

    private static final Logger LOGGER = Logger.getLogger(GameRoomController.class.getName());

    private final PouLogic model;
    private final MainView mainView;
    private final GameLoop mainGameLoop;
    private final Runnable globalStatsUpdater;

    private FruitCatcherController activeMinigame;

    /**
     * @param model The main logic model (to add coins).
     * @param view The room view (to set the listener on the button).
     * @param mainView The main view (to display the minigame overlay).
     * @param mainGameLoop The main loop (to stop when the minigame starts).
     * @param globalStatsUpdater The callback to update the stats in the top bar upon return.
     */
    public GameRoomController(final PouLogic model,
                              final GameRoomView view,
                              final MainView mainView,
                              final GameLoop mainGameLoop,
                              final Runnable globalStatsUpdater) {
        this.model = model;
        this.mainView = mainView;
        this.mainGameLoop = mainGameLoop;
        this.globalStatsUpdater = globalStatsUpdater;

        // delega l'azione al metodo interno
        view.setOnFruitCatcherAction(e -> startFruitCatcher());
    }

    private void startFruitCatcher() {
        LOGGER.info("[GameRoomController] Starting Fruit Catcher...");

        this.mainGameLoop.shutdown();

        final PouCharacterView pouView = this.mainView.getPouCharacterView();
        final FruitCatcherView minigameView = new FruitCatcherJavaFXView(pouView);

        this.activeMinigame = new FruitCatcherControllerImpl(minigameView, coins -> {
            LOGGER.info("Minigame finished. Coins won: " + coins);
            this.model.addCoins(coins);
            this.model.play();
            closeMinigame(minigameView);
        });

        this.mainView.showMinigame(minigameView.getNode());
        this.activeMinigame.start();
    }

    private void closeMinigame(final FruitCatcherView minigameView) {
        Platform.runLater(() -> {
            this.mainView.removeMinigame(minigameView.getNode());

            this.mainView.restoreCharacter();

            this.activeMinigame = null;

            this.mainGameLoop.start();
            this.globalStatsUpdater.run();
        });
    }

    /**
     * Safety method to stop the minigame if the app is closed.
     */
    public void shutdown() {
        if (this.activeMinigame != null && this.activeMinigame.isRunning()) {
            this.activeMinigame.shutdown();
        }
    }
}
