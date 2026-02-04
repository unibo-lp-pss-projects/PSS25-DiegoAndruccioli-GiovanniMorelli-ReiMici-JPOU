package it.unibo.jpou.mvc.model.inventory;

import java.util.Map;
import java.util.Set;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.Consumable;
import it.unibo.jpou.mvc.model.items.durable.Durable;

/**
 * Interface representing the player's inventory.
 * Manages the storage and retrieval of both consumable and durable items.
 */
public interface Inventory {

    /**
     * Adds an item to the inventory.
     *
     * @param item the item to add.
     */
    void addItem(Item item);

    /**
     * Uses a consumable item, reducing its quantity.
     *
     * @param consumable the item to use.
     * @throws IllegalArgumentException if the item is not present.
     */
    void consumeItem(Consumable consumable);

    /**
     * Checks if a durable item is already owned.
     *
     * @param durable the item to check.
     * @return true if owned, false otherwise.
     */
    boolean isOwned(Durable durable);

    /**
     * Returns all consumables with their respective quantities.
     *
     * @return an unmodifiable map of consumables.
     */
    Map<Consumable, Integer> getConsumables();

    /**
     * Returns all owned durable items.
     *
     * @return an unmodifiable set of durables.
     */
    Set<Durable> getDurables();
}
