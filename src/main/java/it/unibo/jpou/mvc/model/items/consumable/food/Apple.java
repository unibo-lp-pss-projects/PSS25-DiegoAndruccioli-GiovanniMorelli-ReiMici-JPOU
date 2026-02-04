package it.unibo.jpou.mvc.model.items.consumable.food;

import it.unibo.jpou.mvc.model.items.consumable.AbstractConsumable;

/**
 * A simple apple, a basic and cheap food item.
 */
public final class Apple extends AbstractConsumable implements Food {

    private static final String APPLE_NAME = "Apple";
    private static final int APPLE_PRICE = 5;
    private static final int APPLE_EFFECT = 10;

    /**
     * Constructs a new Apple.
     */
    public Apple() {
        super(APPLE_NAME, APPLE_PRICE, APPLE_EFFECT);
    }
}
