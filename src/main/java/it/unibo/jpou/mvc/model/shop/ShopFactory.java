package it.unibo.jpou.mvc.model.shop;

import java.util.ArrayList;
import java.util.List;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.food.Sushi;
import it.unibo.jpou.mvc.model.items.consumable.potion.EnergyPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.items.durable.skin.GreenSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;

/**
 * Utility class to create pre-configured shop instances.
 * Acts as a Factory for the shop catalog.
 */
public final class ShopFactory {

    private ShopFactory() {

    }

    /**
     * Creates a standard shop with all available items in the game.
     *
     * @return a new Shop instance populated with default items.
     */
    public static Shop createStandardShop() {
        final List<Item> catalog = new ArrayList<>();

        // Food
        catalog.add(new Apple());
        catalog.add(new Sushi());

        // Potions
        catalog.add(new HealthPotion());
        catalog.add(new EnergyPotion());

        // Skins
        catalog.add(new RedSkin());
        catalog.add(new GreenSkin());

        return new ShopImpl(catalog);
    }
}