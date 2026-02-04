package it.unibo.jpou.mvc.model.shop;

import java.util.Collections;
import java.util.List;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.durable.Durable;

/**
 * Concrete implementation of the Shop interface.
 */
public class ShopImpl implements Shop {

    private final List<Item> catalog;

    /**
     * Constructs a shop with a predefined list of items.
     *
     * @param items the items to be sold in the shop.
     */
    public ShopImpl(final List<Item> items) {
        this.catalog = List.copyOf(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int buyItem(final Inventory inventory, final int currentBalance, final Item item) {
        if (item instanceof Durable && inventory.isOwned((Durable) item)) {
            throw new IllegalStateException("Item already owned: " + item.getName());
        }

        if (currentBalance < item.getPrice()) {
            throw new IllegalArgumentException("Not enough credits to buy: " + item.getName());
        }

        inventory.addItem(item);
        return currentBalance - item.getPrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Item> getAvailableItems() {
        return Collections.unmodifiableList(this.catalog);
    }
}
