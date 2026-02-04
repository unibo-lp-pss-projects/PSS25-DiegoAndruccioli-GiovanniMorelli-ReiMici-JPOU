package it.unibo.jpou.mvc.model.shop;

import java.util.List;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.inventory.Inventory;

/**
 * Interface representing the game shop where items can be purchased.
 * Manages transactions and ensures players have enough credits.
 */
public interface Shop {

    /**
     * Attempts to purchase an item.
     *
     * @param inventory the player's inventory where the item will be added.
     * @param currentBalance the player's current credit amount.
     * @param item the item to buy.
     * @return the remaining balance after the purchase.
     * @throws IllegalStateException if the item is already owned (for durables).
     * @throws IllegalArgumentException if credits are insufficient.
     */
    int buyItem(Inventory inventory, int currentBalance, Item item);

    /**
     * @return an unmodifiable list of items available in the shop.
     */
    List<Item> getAvailableItems();
}
