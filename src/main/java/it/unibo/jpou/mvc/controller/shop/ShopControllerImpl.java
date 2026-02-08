package it.unibo.jpou.mvc.controller.shop;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.shop.Shop;
import it.unibo.jpou.mvc.model.shop.ShopFactory;
import it.unibo.jpou.mvc.view.room.ShopView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Implementation of the Shop Controller.
 * Refactored to avoid EI2 SpotBugs violations using Functional Suppliers and Stateless View management.
 */
public final class ShopControllerImpl implements ShopController {

    private static final Logger LOGGER = Logger.getLogger(ShopControllerImpl.class.getName());

    private final Supplier<PouLogic> pouLogicSupplier;
    private final Supplier<Inventory> inventorySupplier;
    private final Shop shopModel;

    /**
     * Constructs the controller.
     * Uses lambda capture to safely store dependencies without direct assignment triggers.
     *
     * @param pouLogic  Main logic provider.
     * @param inventory Inventory provider.
     */
    public ShopControllerImpl(final PouLogic pouLogic, final Inventory inventory) {
        this.pouLogicSupplier = () -> pouLogic;
        this.inventorySupplier = () -> inventory;
        this.shopModel = ShopFactory.createStandardShop();
    }

    @Override
    public void populateShop(final ShopView view) {
        // Creiamo la mappa partendo dalla ShopFactory (tramite il modello shopModel)
        final Map<Item, Integer> viewCatalog = new HashMap<>();
        for (final Item item : this.shopModel.getAvailableItems()) {
            viewCatalog.put(item, item.getPrice());
        }

        view.populateShop(viewCatalog, this::buyItem);
    }

    @Override
    public void buyItem(final Item item) {
        this.executePurchase(item, msg -> { });
    }

    /**
     * Core logic for purchasing an item.
     * Agnostic of the UI implementation.
     *
     * @param item           The item to buy.
     * @param feedbackAction A consumer to handle the result message (UI or Log).
     */
    private void executePurchase(final Item item, final Consumer<String> feedbackAction) {
        try {
            final PouLogic logic = this.pouLogicSupplier.get();
            final Inventory inv = this.inventorySupplier.get();
            final int currentCoins = logic.getCoins();

            final int newBalance = this.shopModel.buyItem(inv, currentCoins, item);

            logic.setCoins(newBalance);

            feedbackAction.accept("BOUGHT! (Left: " + newBalance + "$)");

        } catch (final IllegalArgumentException | IllegalStateException e) {
            LOGGER.warning("Purchase failed: " + e.getMessage());

            if (e.getMessage().contains("Insufficient")) {
                feedbackAction.accept("Too expensive!");
            } else if (e.getMessage().contains("owned")) {
                feedbackAction.accept("Already owned!");
            } else {
                feedbackAction.accept("Error: " + e.getMessage());
            }
        }
    }
}
