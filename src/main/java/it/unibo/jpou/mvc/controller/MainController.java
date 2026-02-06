package it.unibo.jpou.mvc.controller;

/**
 * The Orchestrator of the application.
 * It manages the lifecycle, connects the View to the Model,
 * and delegates specific business logic to sub-controllers.
 */
public interface MainController {

    /**
     * Starts the application (game loop, initial rendering).
     */
    void start();

    /**
     * Stops the application and releases resources.
     */
    void stop();

    /**
     * Starts the Fruit Catcher minigame.
     */
    void startFruitCatcher();

    /**
     * Returns the controller responsible for the Shop logic.
     *
     * @return the shop controller instance.
     */
    ShopController getShopController();

    /**
     * Returns the controller responsible for Inventory logic.
     *
     * @return the inventory controller instance.
     */
    InventoryController getInventoryController();
}
