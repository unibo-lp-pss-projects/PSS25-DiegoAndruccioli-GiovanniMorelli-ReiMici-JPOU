package it.unibo.jpou.mvc.model.shop;

import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.durable.Durable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the Shop business logic.
 * Manages item transactions and validation.
 */
public final class ShopImpl implements Shop {

    private final List<Item> items;

    /**
     * Creates a new Shop with the specified list of items.
     *
     * @param items The list of items available for purchase.
     */
    public ShopImpl(final List<Item> items) {
        this.items = new ArrayList<>(Objects.requireNonNull(items));
    }

    @Override
    public List<Item> getAvailableItems() {
        return Collections.unmodifiableList(this.items);
    }

    @Override
    public int buyItem(final Inventory inventory, final int currentCoins, final Item item) {
        if (currentCoins < item.getPrice()) {
            throw new IllegalArgumentException("Insufficient funds! Needed: " + item.getPrice());
        }

        if (item instanceof Durable && inventory.isOwned((Durable) item)) {
            throw new IllegalStateException("Item already owned!");
        }

        inventory.addItem(item);

        return currentCoins - item.getPrice();
    }
}
