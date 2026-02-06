package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.controller.minigames.FruitCatcherController;
import it.unibo.jpou.mvc.controller.minigames.FruitCatcherControllerImpl;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherJavaFXView;
import it.unibo.jpou.mvc.view.minigames.FruitCatcherView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import it.unibo.jpou.mvc.view.room.BathroomView;
import it.unibo.jpou.mvc.view.room.GameRoomView;
import it.unibo.jpou.mvc.view.room.KitchenView;
import javafx.application.Platform;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 * This class is responsible for wiring the Business Logic components together.
 * It manages the lifecycle of the application and holds references to the core
 * controllers.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());

    private final MainView view;
    private final PouLogic model;
    private final Inventory inventory;

    private final ShopController shopController;
    private final InventoryController inventoryController;

    private final GameLoop gameLoop;

    private final BedroomView bedroomView;
    private final BathroomView bathroomView;
    private final KitchenView kitchenView;
    private final GameRoomView gameRoomView;

    //per scalabilita minigame
    private FruitCatcherController activeMinigame;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance (Present for compatibility, but currently
     *             ignored).
     */
    public MainControllerImpl(final MainView view) {

        this.view = Objects.requireNonNull(view, "View cannot be null.");

        this.model = new PouLogic();
        this.inventory = new InventoryImpl();

        this.shopController = new ShopControllerImpl(this.model, this.inventory);
        this.inventoryController = new InventoryControllerImpl(this.model, this.inventory);
        this.gameLoop = new PouGameLoop();

        this.bedroomView = new BedroomView();
        this.bathroomView = new BathroomView();
        this.kitchenView = new KitchenView();
        this.gameRoomView = new GameRoomView();

        setupBedroomLogic();
        setupBathroomLogic();
        setupGameRoomLogic();
        setupNavigation();
        setupGameLoop();

        this.view.setRoom(this.bedroomView);

        LOGGER.info("[MainController] Logic System initialized.");
    }

    private void setupBedroomLogic() {
        this.model.stateProperty().addListener((_, _, newState) ->
                Platform.runLater(() -> this.bedroomView.updateView(newState)));

        this.bedroomView.setOnActionHandler(_ -> {
            if (this.model.getState() == PouState.SLEEPING) {
                this.model.wakeUp();
            } else {
                this.model.sleep();
            }
        });

        this.bedroomView.setOnInventoryHandler(_ -> {
            LOGGER.info("Open wardrobe devi mettere l'azione di guardarobe");
        });
    }

    private void setupBathroomLogic() {
        this.bathroomView.setOnWashHandler(_ -> {
            this.model.wash();
            updateGlobalStatistics();
        });
    }

    private void setupGameRoomLogic() {
        this.gameRoomView.setOnFruitCatcherAction(e -> this.startFruitCatcher());
    }

    private void setupNavigation() {
        this.view.setOnRoomChange(Room.BEDROOM, e -> this.view.setRoom(this.bedroomView));
        this.view.setOnRoomChange(Room.BATHROOM, e -> this.view.setRoom(this.bathroomView));
        this.view.setOnRoomChange(Room.KITCHEN, e -> this.view.setRoom(this.kitchenView));
        this.view.setOnRoomChange(Room.GAME_ROOM, e -> this.view.setRoom(this.gameRoomView));
    }

    private void setupGameLoop() {
        if (this.gameLoop instanceof PouGameLoop) {
            ((PouGameLoop) this.gameLoop).addTickListener(() -> {
                this.model.applyDecay();
                Platform.runLater(this::updateGlobalStatistics);
            });
        }
    }

    private void updateGlobalStatistics() {
        this.view.updateStat("hunger",
                (double) this.model.getHunger() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHunger()));
        this.view.updateStat("energy",
                (double) this.model.getEnergy() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getEnergy()));
        this.view.updateStat("fun",
                (double) this.model.getFun() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getFun()));
        this.view.updateStat("health",
                (double) this.model.getHealth() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHealth()));
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

            updateGlobalStatistics();
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
