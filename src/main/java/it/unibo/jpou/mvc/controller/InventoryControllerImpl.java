package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.Consumable;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import it.unibo.jpou.mvc.model.items.durable.Durable;
import it.unibo.jpou.mvc.model.items.durable.skin.Skin;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Concrete implementation of the Inventory Controller.
 */
public final class InventoryControllerImpl implements InventoryController {

    private final Supplier<PouLogic> pouLogic;
    private final Supplier<Inventory> inventory;

    /**
     * @param pouLogic the main logic model.
     * @param inventory the inventory model.
     */
    public InventoryControllerImpl(final PouLogic pouLogic, final Inventory inventory) {
        Objects.requireNonNull(pouLogic);
        Objects.requireNonNull(inventory);

        this.pouLogic = () -> pouLogic;
        this.inventory = () -> inventory;
    }

    @Override
    public void useItem(final Item item) {
        if (item == null) {
            return;
        }

        final PouLogic logic = this.pouLogic.get();
        final Inventory inv = this.inventory.get();

        boolean exists = false;
        if (item instanceof Consumable) {
            exists = inv.getConsumables().containsKey((Consumable) item);
        } else if (item instanceof Durable) {
            exists = inv.isOwned((Durable) item);
        }

        if (!exists) {
            return;
        }

        switch (item) {
            case final Food food -> {
                logic.eat(food);
                inv.consumeItem((Consumable) item);
            }
            case final Potion potion -> {
                logic.usePotion(potion);
                inv.consumeItem((Consumable) item);
            }
            case final Skin skin -> logic.setSkin(skin);
            default -> {

            }
        }
    }
}
