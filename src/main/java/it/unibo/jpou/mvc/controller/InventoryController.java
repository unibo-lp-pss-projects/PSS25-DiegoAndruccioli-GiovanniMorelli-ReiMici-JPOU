package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.items.Item;

/**
 * Controller responsible for inventory interactions (using items).
 * Functional Interface: handles the single action of using an item.
 */
@FunctionalInterface
public interface InventoryController {

    /**
     * Uses an item from the inventory.
     *
     * @param item the item to use.
     */
    void useItem(Item item);
}
