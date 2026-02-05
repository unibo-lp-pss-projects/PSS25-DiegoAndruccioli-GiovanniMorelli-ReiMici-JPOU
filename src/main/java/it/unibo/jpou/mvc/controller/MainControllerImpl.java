package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.view.MainView;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 * This class is responsible for wiring the Business Logic components together.
 * It manages the lifecycle of the application and holds references to the core controllers.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());

    private final PouLogic model;
    private final Inventory inventory;

    private final ShopController shopController;
    private final InventoryController inventoryController;

    private final GameLoop gameLoop;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance (Present for compatibility, but currently ignored).
     */
    public MainControllerImpl(final MainView view) {
        Objects.requireNonNull(view, "View cannot be null, even if unused.");

        this.model = new PouLogic();
        this.inventory = new InventoryImpl();

        this.shopController = new ShopControllerImpl(this.model, this.inventory);
        this.inventoryController = new InventoryControllerImpl(this.model, this.inventory);

        final PouGameLoop loop = new PouGameLoop();
        this.gameLoop = loop;

        LOGGER.info("[MainController] Logic System initialized. Shop and Inventory connected.");
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
