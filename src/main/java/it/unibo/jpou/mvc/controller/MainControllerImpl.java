package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.controller.overlay.GameOverController;
import it.unibo.jpou.mvc.controller.overlay.GameOverControllerImpl;
import it.unibo.jpou.mvc.controller.overlay.PauseController;
import it.unibo.jpou.mvc.controller.overlay.PauseControllerImpl;
import it.unibo.jpou.mvc.controller.room.BathroomController;
import it.unibo.jpou.mvc.controller.room.BedroomController;
import it.unibo.jpou.mvc.controller.room.GameRoomController;
import it.unibo.jpou.mvc.controller.room.InfirmaryController;
import it.unibo.jpou.mvc.controller.room.KitchenController;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import it.unibo.jpou.mvc.view.room.BathroomView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import it.unibo.jpou.mvc.view.room.InfirmaryView;
import it.unibo.jpou.mvc.view.room.KitchenView;
import it.unibo.jpou.mvc.view.room.ShopView;
import it.unibo.jpou.mvc.view.room.GameRoomView;
import javafx.application.Platform;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());
    private static final int INITIAL_COINS = 1000;

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

    private final GameRoomController gameRoomController;
    private final KitchenController kitchenController;
    private final InfirmaryController infirmaryController;
    private final BedroomController bedroomController;

    @SuppressWarnings("PMD.UnusedPrivateField")
    private final BathroomController bathroomController;

    private final PersistenceController persistenceController;
    private final PauseController pauseController;
    private final GameOverController gameOverController;

    private Room currentRoom = Room.BEDROOM;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance.
     */
    public MainControllerImpl(final MainView view) {
        this.mainView = Objects.requireNonNull(view, "View cannot be null");

        this.model = new PouLogic();
        this.model.setCoins(INITIAL_COINS);
        this.inventory = new InventoryImpl();
        this.gameLoop = new PouGameLoop();

        this.persistenceController = new PersistenceControllerImpl(this.model, this.inventory);

        final String startingRoomName = this.persistenceController.loadGame();

        this.pauseController = new PauseControllerImpl(
                this.gameLoop,
                this.mainView,
                this.persistenceController,
                () -> this.currentRoom);
        this.gameOverController = new GameOverControllerImpl(
                this.gameLoop,
                this.mainView,
                this.model
        );
        this.bedroomView = new BedroomView();
        this.bathroomView = new BathroomView();
        this.kitchenView = new KitchenView();
        this.infirmaryView = new InfirmaryView();
        this.gameRoomView = new GameRoomView();
        this.shopView = new ShopView();

        this.bedroomController = new BedroomController(
                this.model,
                this.bedroomView,
                this.mainView,
                this.inventory);

        this.bathroomController = new BathroomController(
                this.model,
                this.bathroomView,
                () -> Platform.runLater(this::updateGlobalStatistics));

        this.kitchenController = new KitchenController(
                this.model,
                this.inventory,
                this.kitchenView,
                () -> Platform.runLater(this::updateGlobalStatistics));

        this.infirmaryController = new InfirmaryController(
                this.model,
                this.inventory,
                this.infirmaryView,
                () -> Platform.runLater(this::updateGlobalStatistics));

        this.gameRoomController = new GameRoomController(
                this.model,
                this.gameRoomView,
                this.mainView,
                this.gameLoop,
                () -> Platform.runLater(this::updateGlobalStatistics));

        final ShopController shopCtrl = new ShopControllerImpl(this.model, this.inventory);
        final InventoryController invCtrl = new InventoryControllerImpl(this.model, this.inventory);
        this.shopControllerSupplier = () -> shopCtrl;
        this.inventoryControllerSupplier = () -> invCtrl;

        setupNavigation();
        setupGameLoop();
        setupOverlayActions();

        this.mainView.bindPouAge(this.model.ageProperty());
        this.mainView.setPouSkinColor(this.model.getSkin().getColorHex());
        this.model.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                this.mainView.setPouSkinColor(newSkin.getColorHex());
            }
        });

        this.model.stateProperty().addListener((_, _, newState) -> {
            if (newState == PouState.DEAD) {
                handleDeath();
            }
        });

        AbstractRoomView startView = this.bedroomView;
        if (startingRoomName != null) {

            try {
                final Room savedRoom = Room.valueOf(startingRoomName);
                startView = mapViewFromRoom(savedRoom);
            } catch (final IllegalArgumentException e) {
                LOGGER.warning("La stanza salvata non è valida " + startingRoomName);
            }
        }

        changeRoom(startView);
        if (startView instanceof BedroomView) {
            this.bedroomView.updateView(this.model.getState());

        }
        LOGGER.info("Sistema logico inizializzato correttamente");
    }

    private void setupOverlayActions() {
        this.mainView.setOnSettingsAction(_ -> this.pauseController.pause());
        this.mainView.setOnResumeAction(_ -> this.pauseController.resume());
        this.mainView.setOnQuitAction(_ -> this.pauseController.quit());

        this.mainView.setOnRestartAction(_ -> this.gameOverController.restart());
    }

    private void handleDeath() {
        this.gameLoop.shutdown();
        Platform.runLater(() -> this.mainView.setGameOverVisible(true));
    }

    private void setupNavigation() {
        this.mainView.setOnRoomChange(Room.BEDROOM, _ -> changeRoom(this.bedroomView));
        this.mainView.setOnRoomChange(Room.BATHROOM, _ -> changeRoom(this.bathroomView));
        this.mainView.setOnRoomChange(Room.KITCHEN, _ -> changeRoom(this.kitchenView));
        this.mainView.setOnRoomChange(Room.INFIRMARY, _ -> changeRoom(this.infirmaryView));
        this.mainView.setOnRoomChange(Room.SHOP, _ -> changeRoom(this.shopView));
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
            this.bedroomView.updateView(PouState.AWAKE);
        }

        this.currentRoom = mapRoomFromView(newRoomView);
        this.mainView.setCharacterVisible(!(newRoomView instanceof ShopView));

        if (newRoomView instanceof ShopView) {
            this.shopControllerSupplier.get().populateShop((ShopView) newRoomView);
        } else {
            updateRoomData(newRoomView);
        }

        this.mainView.setRoom(newRoomView);
    }

    /**
     * It is used when reloading data to understand which room Pou was at the time of closing the previous session.
     *
     * @param room the enum taken from the save file
     * @return the room that corresponds to the enum
     */
    private AbstractRoomView mapViewFromRoom(final Room room) {
        return switch (room) {
            case BATHROOM -> this.bathroomView;
            case KITCHEN -> this.kitchenView;
            case INFIRMARY -> this.infirmaryView;
            case GAME_ROOM -> this.gameRoomView;
            case SHOP -> this.shopView;
            default -> this.bedroomView;
        };
    }

    /**
     * It is used when navigating within the game to understand which room Pou is in.
     *
     * @param view the room where it is located
     * @return the enum of corresponding room
     */
    private Room mapRoomFromView(final AbstractRoomView view) {
        if (view instanceof BedroomView) {
            return Room.BEDROOM;
        } else if (view instanceof BathroomView) {
            return Room.BATHROOM;
        } else if (view instanceof GameRoomView) {
            return Room.GAME_ROOM;
        } else if (view instanceof InfirmaryView) {
            return Room.INFIRMARY;
        } else if (view instanceof KitchenView) {
            return Room.KITCHEN;
        } else if (view instanceof ShopView) {
            return Room.SHOP;
        }
        return Room.BEDROOM;
    }

    private void updateRoomData(final AbstractRoomView currentView) {
        if (currentView instanceof KitchenView) {
            this.kitchenController.refreshView();
        } else if (currentView instanceof InfirmaryView) {
            this.infirmaryController.refreshView();
        } else if (currentView instanceof BedroomView) {
            this.bedroomController.refreshView();
        }
    }

    @Override
    public void start() {
        this.gameLoop.start();
        LOGGER.info("[MainController] GameLoop started.");
    }

    @Override
    public void stop() {
        this.gameLoop.shutdown();
        if (this.gameRoomController != null) {
            this.gameRoomController.shutdown();
        }
        LOGGER.info("[MainController] GameLoop stopped.");
    }

    @Override
    public ShopController getShopController() {
        return this.shopControllerSupplier.get();
    }

    @Override
    public InventoryController getInventoryController() {
        return this.inventoryControllerSupplier.get();
    }
}
