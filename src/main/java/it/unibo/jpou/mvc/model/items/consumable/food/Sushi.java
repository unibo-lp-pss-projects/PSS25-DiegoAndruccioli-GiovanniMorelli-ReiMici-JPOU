package it.unibo.jpou.mvc.model.items.consumable.food;

import it.unibo.jpou.mvc.model.items.consumable.AbstractConsumable;

/**
 * Sushi, a more expensive and nutritious food item.
 */
public final class Sushi extends AbstractConsumable implements Food {

    private static final String SUSHI_NAME = "Sushi";
    private static final int SUSHI_PRICE = 25;
    private static final int SUSHI_EFFECT = 40;

    /**
     * Constructs a new Sushi.
     */
    public Sushi() {
        super(SUSHI_NAME, SUSHI_PRICE, SUSHI_EFFECT);
    }
}
