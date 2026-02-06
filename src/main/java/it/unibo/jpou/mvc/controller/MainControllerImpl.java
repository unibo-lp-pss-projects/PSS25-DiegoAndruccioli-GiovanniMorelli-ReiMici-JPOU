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
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import it.unibo.jpou.mvc.view.room.BathroomView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import it.unibo.jpou.mvc.view.room.InfirmaryView;
import it.unibo.jpou.mvc.view.room.KitchenView;
import it.unibo.jpou.mvc.view.room.ShopView;
import it.unibo.jpou.mvc.view.room.GameRoomView;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());

    private final PouLogic model;
    private final Inventory inventory;

    private final Supplier<ShopController> shopControllerSupplier;
    private final Supplier<InventoryController> inventoryControllerSupplier;

    private final GameLoop gameLoop;

    private final MainView mainView;
    private final BedroomView bedroomView;
    private final BathroomView bathroomView;
    private final KitchenView kitchenView;
    private final InfirmaryView infirmaryView;
    private final ShopView shopView;
    private final GameRoomView gameRoomView;

    //per scalabilita minigame
    private FruitCatcherController activeMinigame;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance.
     */
    public MainControllerImpl(final MainView view) {
        this.mainView = Objects.requireNonNull(view, "View cannot be null");

        this.model = new PouLogic();
        this.inventory = new InventoryImpl();
        this.gameLoop = new PouGameLoop();

        this.bedroomView = new BedroomView();
        this.bathroomView = new BathroomView();
        this.kitchenView = new KitchenView();
        this.infirmaryView = new InfirmaryView();
        this.gameRoomView = new GameRoomView();
        this.shopView = new ShopView();

        final ShopController shopCtrl = new ShopControllerImpl(this.model, this.inventory);
        final InventoryController invCtrl = new InventoryControllerImpl(this.model, this.inventory);

        this.shopControllerSupplier = () -> shopCtrl;
        this.inventoryControllerSupplier = () -> invCtrl;

        setupGameRoomLogic();
        setupNavigation();
        setupGameLoop();
        this.mainView.setRoom(this.bedroomView);
        //this.mainView.setRoom(this.gameRoomView);
        LOGGER.info("[MainController] Logic System initialized.");
    }

    private void setupGameRoomLogic() {
        this.gameRoomView.setOnFruitCatcherAction(e -> this.startFruitCatcher());
    }

    private void setupNavigation() {
        this.mainView.setOnRoomChange(Room.BEDROOM, _ -> changeRoom(this.bedroomView));
        this.mainView.setOnRoomChange(Room.BATHROOM, _ -> changeRoom(this.bathroomView));
        this.mainView.setOnRoomChange(Room.KITCHEN, _ -> changeRoom(this.kitchenView));
        this.mainView.setOnRoomChange(Room.INFIRMARY, _ -> changeRoom(this.infirmaryView));
        this.mainView.setOnRoomChange(Room.SHOP, _ -> changeRoom(this.shopView));
        this.mainView.setOnRoomChange(Room.KITCHEN, _ -> changeRoom(this.kitchenView));
        this.mainView.setOnRoomChange(Room.GAME_ROOM, _ -> changeRoom(this.gameRoomView));
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
        this.mainView.updateStat("hunger",
                (double) this.model.getHunger() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHunger()));
        this.mainView.updateStat("energy",
                (double) this.model.getEnergy() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getEnergy()));
        this.mainView.updateStat("fun",
                (double) this.model.getFun() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getFun()));
        this.mainView.updateStat("health",
                (double) this.model.getHealth() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHealth()));
    }

    private void changeRoom(final AbstractRoomView newRoomView) {
        if (!(newRoomView instanceof BedroomView) && this.model.getState() == PouState.SLEEPING) {
            this.model.wakeUp();
        }

        if (newRoomView instanceof KitchenView) {
            ((KitchenView) newRoomView).refreshItems(new HashMap<>(this.inventory.getConsumables()));
        } else if (newRoomView instanceof InfirmaryView) {
            ((InfirmaryView) newRoomView).refreshItems(new HashMap<>(this.inventory.getConsumables()));
        } else if (newRoomView instanceof ShopView) {
            this.shopControllerSupplier.get().populateShop((ShopView) newRoomView);
        }

        this.mainView.setRoom(newRoomView);
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

        //ferma loop principale
        this.gameLoop.shutdown();

        final FruitCatcherView minigameView = new FruitCatcherJavaFXView();

        this.activeMinigame = new FruitCatcherControllerImpl(minigameView, coins -> {
            LOGGER.info("Minigame finished. Coins won: " + coins);
            this.model.addCoins(coins);
            closeMinigame(minigameView);
        });

        this.mainView.showMinigame(minigameView.getNode());

        this.activeMinigame.start();
    }

    /**
     * Chiude il minigioco e ripristina la schermata principale.
     *
     * @param minigameView the view of the minigame to remove.
     */
    private void closeMinigame(final FruitCatcherView minigameView) {
        Platform.runLater(() -> {
            this.mainView.removeMinigame(minigameView.getNode());

            this.activeMinigame = null;

            this.gameLoop.start();

            updateGlobalStatistics();
        });
    }

    /**
     * @return the shop controller instance.
     */
    public ShopController getShopController() {
        return this.shopControllerSupplier.get();
    }

    @Override
    public InventoryController getInventoryController() {
        return this.inventoryControllerSupplier.get();
    }

    /**
     * @return a runnable that starts the fruit catcher game.
     */
    public Runnable getFruitCatcherStarter() {
        return this::startFruitCatcher;
    }
}
