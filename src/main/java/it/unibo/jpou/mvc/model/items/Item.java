package it.unibo.jpou.mvc.model.items;

/**
 * Represents the fundamental contract for any entity in the game economy.
 * This interface defines the immutable properties required for transaction logic (Shop)
 * and basic identification (Inventory), abstracting away specific behaviors.
 */
public interface Item {

    /**
     * @return the name of the item.
     */
    String getName();

    /**
     * @return the price (must be a positive integer).
     */
    int getPrice();
}
