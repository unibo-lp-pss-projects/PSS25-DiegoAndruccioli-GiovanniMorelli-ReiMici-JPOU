package it.unibo.jpou.mvc.model.items.consumable;

import it.unibo.jpou.mvc.model.items.Item;

/**
 * Represents a specific category of items that are removed from the inventory after the use.
 * Unlike durable items, consumables provide a quantitative effect on the Pou's status and have a single-use lifecycle.
 */
public interface Consumable extends Item {

    /**
     * Returns the quantitative magnitude of the item's effect.
     * The interpretation of this value depends on the concrete implementation:
     * For {@code Food}, it represents the hunger reduction value.
     * For {@code Potion}, it represents the health restoration value.
     *
     * @return the positive integer representing the effect power.
     */
    int getEffectValue();
}
