package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.controller.inventory.InventoryController;
import it.unibo.jpou.mvc.controller.inventory.InventoryControllerImpl;
import it.unibo.jpou.mvc.controller.shop.ShopController;
import it.unibo.jpou.mvc.controller.shop.ShopControllerImpl;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.model.items.consumable.food.Sushi;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration Test ensuring Controllers, Model and Inventory work together correctly.
 */
class ControllerIntegrationTest {

    private static final int INITIAL_HUNGER = 50;
    private static final int RICH_COINS = 999;
    private static final int SALARY_INCREMENT = 10;

    private PouLogic model;
    private Inventory inventory;
    private ShopController shopController;
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        this.model = new PouLogic();
        this.inventory = new InventoryImpl();
        this.shopController = new ShopControllerImpl(model, inventory);
        this.inventoryController = new InventoryControllerImpl(model, inventory);
    }

    @Test
    void testCompleteGameFlow() {
        final Sushi sushi = new Sushi();
        this.model.setCoins(0);

        this.shopController.buyItem(sushi);

        assertFalse(this.inventory.getConsumables().containsKey(sushi),
                "Inventory should be empty after failed purchase attempt");

        this.model.setCoins(sushi.getPrice() + SALARY_INCREMENT);
        this.shopController.buyItem(sushi);

        assertTrue(this.inventory.getConsumables().containsKey(sushi),
                "Sushi should be in inventory after successful purchase");
        assertEquals(1, this.inventory.getConsumables().get(sushi));

        this.model.setHunger(INITIAL_HUNGER);

        this.inventoryController.useItem(sushi);

        assertTrue(this.model.getHunger() > INITIAL_HUNGER, "Pou hunger should increase after eating");
        assertFalse(this.inventory.getConsumables().containsKey(sushi), "Item should be consumed/removed");
    }

    @Test
    void testSkinPersistenceFlow() {
        final RedSkin skin = new RedSkin();
        this.model.setCoins(RICH_COINS);

        this.shopController.buyItem(skin);

        assertTrue(this.inventory.isOwned(skin), "Skin should be in inventory after buying");

        this.inventoryController.useItem(skin);

        assertEquals(skin.getClass(), this.model.getSkin().getClass(), "Model should update skin type");

        this.inventoryController.useItem(skin);
        assertTrue(this.inventory.isOwned(skin), "Skin should NOT be consumed after use");
    }
}
