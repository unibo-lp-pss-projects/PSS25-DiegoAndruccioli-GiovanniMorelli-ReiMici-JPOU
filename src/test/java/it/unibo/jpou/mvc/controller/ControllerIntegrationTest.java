package it.unibo.jpou.mvc.controller;

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
 * This verifies the "Business Logic" flow without needing the GUI or GameLoop.
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
        // 1. Setup del sistema completo (Wiring manuale per il test)
        this.model = new PouLogic();
        this.inventory = new InventoryImpl();

        // 2. Istanziazione dei Controller (usando i Supplier interni che abbiamo creato)
        this.shopController = new ShopControllerImpl(model, inventory);
        this.inventoryController = new InventoryControllerImpl(model, inventory);
    }

    @Test
    void testCompleteGameFlow() {
        // Scenario: Utente povero prova a comprare Sushi (Costoso)
        final Sushi sushi = new Sushi();
        this.model.setCoins(0); // Povertà assoluta

        boolean bought = this.shopController.buyItem(sushi);
        assertFalse(bought, "Should not be able to buy sushi with 0 coins");
        assertFalse(this.inventory.getConsumables().containsKey(sushi), "Inventory should be empty");

        // Scenario: Utente guadagna e compra
        this.model.setCoins(sushi.getPrice() + SALARY_INCREMENT); // Stipendio
        bought = this.shopController.buyItem(sushi);

        assertTrue(bought, "Buying should succeed with enough coins");
        assertEquals(1, this.inventory.getConsumables().get(sushi), "Sushi should be in inventory");

        // Scenario: Utente ha fame e mangia
        this.model.setHunger(INITIAL_HUNGER); // Ha fame
        final int expectedHunger = INITIAL_HUNGER + sushi.getEffectValue();

        this.inventoryController.useItem(sushi);

        // Verifiche finali (Cross-Component)
        assertEquals(expectedHunger, this.model.getHunger(), "Pou logic should reflect eating");
        assertFalse(this.inventory.getConsumables().containsKey(sushi), "Item should be consumed/removed");
    }

    @Test
    void testSkinPersistenceFlow() {
        // Scenario: Comprare e indossare una Skin (Durevole)
        final RedSkin skin = new RedSkin();
        this.model.setCoins(RICH_COINS);

        this.shopController.buyItem(skin);
        assertTrue(this.inventory.isOwned(skin), "Skin should be in inventory");

        // Indossa
        this.inventoryController.useItem(skin);
        assertEquals("Red Skin", this.model.getSkin().getName(), "Model should update skin");

        // Riprova a indossare (Non deve sparire dall'inventario)
        this.inventoryController.useItem(skin);
        assertTrue(this.inventory.isOwned(skin), "Skin should NOT be consumed after use");
    }
}
