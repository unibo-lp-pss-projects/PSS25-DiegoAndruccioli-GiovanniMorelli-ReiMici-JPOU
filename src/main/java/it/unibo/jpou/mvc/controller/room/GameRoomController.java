package it.unibo.jpou.mvc.controller.room;

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
 * Controller responsabile della GameRoom e dell'avvio dei minigiochi.
 */
public final class GameRoomController {

    private static final Logger LOGGER = Logger.getLogger(GameRoomController.class.getName());

    private final PouLogic model;
    private final MainView mainView;
    private final GameLoop mainGameLoop;
    private final Runnable globalStatsUpdater;

    // Manteniamo qui il riferimento al minigioco attivo
    private FruitCatcherController activeMinigame;

    /**
     * @param model              il modello logico principale (per aggiungere le monete).
     * @param view               la view della stanza (per settare il listener sul bottone).
     * @param mainView           la view principale (per mostrare l'overlay del minigioco).
     * @param mainGameLoop       il loop principale (da fermare quando parte il minigioco).
     * @param globalStatsUpdater callback per aggiornare le statistiche nella top bar al ritorno.
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

        // Setup del listener: delega l'azione al metodo interno
        view.setOnFruitCatcherAction(e -> startFruitCatcher());
    }

    private void startFruitCatcher() {
        LOGGER.info("[GameRoomController] Starting Fruit Catcher...");

        // 1. Ferma il loop principale (il minigioco ha il suo loop interno)
        this.mainGameLoop.shutdown();

        // 2. Prepara la view del minigioco
        final PouCharacterView pouView = this.mainView.getPouCharacterView();
        final FruitCatcherView minigameView = new FruitCatcherJavaFXView(pouView);

        // 3. Istanzia il controller del minigioco
        this.activeMinigame = new FruitCatcherControllerImpl(minigameView, coins -> {
            LOGGER.info("Minigame finished. Coins won: " + coins);
            this.model.addCoins(coins);
            closeMinigame(minigameView);
        });

        // 4. Mostra il minigioco e avvialo
        this.mainView.showMinigame(minigameView.getNode());
        this.activeMinigame.start();
    }

    private void closeMinigame(final FruitCatcherView minigameView) {
        Platform.runLater(() -> {
            // Rimuovi la view del minigioco
            this.mainView.removeMinigame(minigameView.getNode());

            // Ripristina il personaggio nella schermata principale
            this.mainView.restoreCharacter();

            this.activeMinigame = null;

            // Riavvia il loop principale e aggiorna la UI
            this.mainGameLoop.start();
            this.globalStatsUpdater.run();
        });
    }

    /**
     * Metodo di sicurezza per fermare il minigioco se l'app viene chiusa.
     */
    public void shutdown() {
        if (this.activeMinigame != null && this.activeMinigame.isRunning()) {
            this.activeMinigame.shutdown();
        }
    }
}