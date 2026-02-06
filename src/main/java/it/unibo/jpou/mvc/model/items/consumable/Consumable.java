package it.unibo.jpou.mvc.model.items.consumable;

import it.unibo.jpou.mvc.model.items.Item;

/**
 * Interface for consumable items.
 */
public interface Consumable extends Item {

    /**
     * @return the value of the effect as an integer.
     */
    int getEffectValue();

    /**
     * @return the display name of the consumable.
     */
    @Override
    String getName();
}
