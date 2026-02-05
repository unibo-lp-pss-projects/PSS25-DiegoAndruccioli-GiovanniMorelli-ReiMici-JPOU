package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.items.Item;

/**
 * Controller responsible for shop transactions.
 * Functional Interface: handles the single action of buying an item.
 */
@FunctionalInterface
public interface ShopController {

    /**
     * Attempts to buy an item.
     *
     * @param item the item to purchase.
     * @return true if the transaction was successful (enough coins), false otherwise.
     */
    boolean buyItem(Item item);
}
