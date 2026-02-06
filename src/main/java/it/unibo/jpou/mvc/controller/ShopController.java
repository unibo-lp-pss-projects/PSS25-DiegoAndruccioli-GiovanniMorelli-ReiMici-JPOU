package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.view.room.ShopView;

/**
 * Interface for the Shop Controller.
 * Defines the contract for Shop interactions within the MVC architecture.
 */
public interface ShopController {

    /**
     * Initializes the shop view with the current catalog and user balance.
     * Essential for the View to know what to display.
     *
     * @param view The view component to populate.
     */
    void populateShop(ShopView view);

    /**
     * Attempts to purchase an item.
     * Unlike the boolean version, this method handles the transaction logic internally
     * and updates the view directly with success/failure messages.
     *
     * @param item the item to purchase.
     */
    void buyItem(Item item);
}
