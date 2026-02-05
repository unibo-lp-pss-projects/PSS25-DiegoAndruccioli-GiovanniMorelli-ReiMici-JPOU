package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Concrete implementation of the Shop Controller.
 */
public final class ShopControllerImpl implements ShopController {

    private final Supplier<PouLogic> pouLogic;
    private final Supplier<Inventory> inventory;

    /**
     * @param pouLogic the main logic model.
     * @param inventory the inventory model.
     */
    public ShopControllerImpl(final PouLogic pouLogic, final Inventory inventory) {
        Objects.requireNonNull(pouLogic);
        Objects.requireNonNull(inventory);

        this.pouLogic = () -> pouLogic;
        this.inventory = () -> inventory;
    }

    @Override
    public boolean buyItem(final Item item) {
        if (item == null) {
            return false;
        }

        final PouLogic logic = this.pouLogic.get();
        final Inventory inv = this.inventory.get();

        final int price = item.getPrice();
        final int currentCoins = logic.getCoins();

        if (currentCoins >= price) {
            logic.setCoins(currentCoins - price);
            inv.addItem(item);
            return true;
        }

        return false;
    }
}
