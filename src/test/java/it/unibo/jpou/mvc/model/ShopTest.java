package it.unibo.jpou.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.model.shop.Shop;
import it.unibo.jpou.mvc.model.shop.ShopFactory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.Consumable;
import it.unibo.jpou.mvc.model.items.durable.Durable;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;

/**
 * Unit tests for the Shop logic and transactions.
 */
class ShopTest {

    private static final Logger LOGGER = Logger.getLogger(ShopTest.class.getName());
    private static final int INITIAL_BALANCE = 100;
    private static final int INSUFFICIENT_BALANCE = 1;

    private Shop shop;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        this.shop = ShopFactory.createStandardShop();
        this.inventory = new InventoryImpl();
    }

    @Test
    @DisplayName("Test successful purchase and balance update")
    void testSuccessfulPurchase() {
        final Item itemToBuy = this.shop.getAvailableItems().getFirst();
        LOGGER.info("Attempting to buy: " + itemToBuy.getName());

        final int newBalance = this.shop.buyItem(this.inventory, INITIAL_BALANCE, itemToBuy);

        assertEquals(INITIAL_BALANCE - itemToBuy.getPrice(), newBalance, "Balance should be correctly reduced");
        assertTrue(this.inventory.getConsumables().containsKey((Consumable) itemToBuy)
                        || this.inventory.isOwned((Durable) itemToBuy),
                "Item should be added to inventory");
    }

    @Test
    @DisplayName("Test purchase failure due to insufficient funds")
    void testInsufficientFunds() {
        final Item expensiveItem = new RedSkin(); // Costa 50
        LOGGER.info("Attempting to buy " + expensiveItem.getName() + " with only " + INSUFFICIENT_BALANCE);

        assertThrows(IllegalArgumentException.class, () -> {
            this.shop.buyItem(this.inventory, INSUFFICIENT_BALANCE, expensiveItem);
        }, "Should throw exception if balance is too low");
    }

    @Test
    @DisplayName("Test purchase failure for already owned durable item")
    void testDurableDuplicatePurchase() {
        final Item skin = new RedSkin();
        this.inventory.addItem(skin);
        LOGGER.info("Attempting to buy " + skin.getName() + " which is already owned");

        assertThrows(IllegalStateException.class, () -> {
            this.shop.buyItem(this.inventory, INITIAL_BALANCE, skin);
        }, "Should throw exception if durable item is already in inventory");
    }
}
