package it.unibo.jpou.mvc.model.items.consumable;

/**
 * Abstract base class for all consumable items.
 */
public abstract class AbstractConsumable implements Consumable {

    private final String name;
    private final int price;
    private final int effectValue;

    /**
     * @param name display name.
     * @param price cost.
     * @param effectValue magnitude of benefit.
     */
    protected AbstractConsumable(final String name, final int price, final int effectValue) {
        this.name = name;
        this.price = price;
        this.effectValue = effectValue;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final int getPrice() {
        return this.price;
    }

    @Override
    public final int getEffectValue() {
        return this.effectValue;
    }
}
