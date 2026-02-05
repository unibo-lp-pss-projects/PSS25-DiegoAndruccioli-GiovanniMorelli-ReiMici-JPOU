package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.room.BathroomView;
import it.unibo.jpou.mvc.view.room.BedroomView;
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

    private final PouLogic model;
    private final Inventory inventory;

    private final ShopController shopController;
    private final InventoryController inventoryController;

    private final GameLoop gameLoop;
    private final MainView mainView;

    private final BedroomView bedroomView;
    private final BathroomView bathroomView;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance (Present for compatibility, but currently
     *             ignored).
     */
    public MainControllerImpl(final MainView view) {

        this.mainView = Objects.requireNonNull(view, "View cannot be null");

        this.model = new PouLogic();
        this.inventory = new InventoryImpl();

        this.shopController = new ShopControllerImpl(this.model, this.inventory);
        this.inventoryController = new InventoryControllerImpl(this.model, this.inventory);
        this.gameLoop = new PouGameLoop();

        this.bedroomView = new BedroomView();
        this.bathroomView = new BathroomView();

        setupBedroomLogic();
        setupBathroomLogic();
        setupNavigation();
        setupGameLoop();

        this.mainView.setRoom(this.bedroomView);

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

    private void setupNavigation() {
        this.mainView.setOnRoomChange(Room.BEDROOM, _ -> this.mainView.setRoom(this.bedroomView));
        this.mainView.setOnRoomChange(Room.BATHROOM, _ -> this.mainView.setRoom(this.bathroomView));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.gameLoop.start();
        LOGGER.info("[MainController] GameLoop started.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.gameLoop.shutdown();
        LOGGER.info("[MainController] GameLoop stopped.");
    }

    /**
     * Retrieves the shop controller instance.
     * Needed to expose business logic to tests or future view components.
     *
     * @return the shop controller.
     */
    public ShopController getShopController() {
        return this.shopController;
    }

    /**
     * Retrieves the inventory controller instance.
     * Needed to expose business logic to tests or future view components.
     *
     * @return the inventory controller.
     */
    public InventoryController getInventoryController() {
        return this.inventoryController;
    }
}
