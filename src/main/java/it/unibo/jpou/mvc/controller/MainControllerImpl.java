package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.controller.minigames.FruitCatcherController;
import it.unibo.jpou.mvc.controller.minigames.FruitCatcherControllerImpl;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherJavaFXView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherView;
import javafx.application.Platform;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 * This class is responsible for wiring the Business Logic components together.
 * It manages the lifecycle of the application and holds references to the core controllers.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());

    private final MainView view;
    private final PouLogic model;
    private final Inventory inventory;

    private final ShopController shopController;
    private final InventoryController inventoryController;

    private final GameLoop gameLoop;

    //per scalabilita minigame
    private FruitCatcherController activeMinigame;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance.
     */
    public MainControllerImpl(final MainView view) {
        this.view = Objects.requireNonNull(view, "View cannot be null.");

        this.model = new PouLogic();
        this.inventory = new InventoryImpl();

        this.shopController = new ShopControllerImpl(this.model, this.inventory);
        this.inventoryController = new InventoryControllerImpl(this.model, this.inventory);

        final PouGameLoop loop = new PouGameLoop();
        this.gameLoop = loop;

        LOGGER.info("[MainController] Logic System initialized.");
    }

    @Override
    public void start() {
        this.gameLoop.start();
        LOGGER.info("[MainController] GameLoop started.");
    }

    @Override
    public void stop() {
        this.gameLoop.shutdown();
        if (this.activeMinigame != null) {
            this.activeMinigame.shutdown();
        }
        LOGGER.info("[MainController] GameLoop stopped.");
    }

    /**
     * Avvia il minigioco Fruit Catcher.
     * Questo metodo deve essere chiamato quando si preme il bottone nella GameRoom.
     */
    @Override
    public void startFruitCatcher() {
        LOGGER.info("[MainController] Starting Fruit Catcher...");

        this.gameLoop.shutdown();

        final FruitCatcherView minigameView = new FruitCatcherJavaFXView();

        this.activeMinigame = new FruitCatcherControllerImpl(minigameView, coins -> {
            LOGGER.info("Minigame finished. Coins won: " + coins);

            this.model.addCoins(coins);

            closeMinigame(minigameView);
        });

        this.view.showMinigame(minigameView.getNode());

        this.activeMinigame.start();
    }

    /**
     * Chiude il minigioco e ripristina la schermata principale.
     *
     * @param minigameView the view of the minigame to remove.
     */
    private void closeMinigame(final FruitCatcherView minigameView) {
        Platform.runLater(() -> {
            this.view.removeMinigame(minigameView.getNode());

            this.activeMinigame = null;

            this.gameLoop.start();
        });
    }

    /**
     * @return the shop controller instance.
     */
    public ShopController getShopController() {
        return this.shopController;
    }

    /**
     * @return the inventory controller instance.
     */
    public InventoryController getInventoryController() {
        return this.inventoryController;
    }

    /**
     * @return a runnable that starts the fruit catcher game.
     */
    public Runnable getFruitCatcherStarter() {
        return this::startFruitCatcher;
    }
}
