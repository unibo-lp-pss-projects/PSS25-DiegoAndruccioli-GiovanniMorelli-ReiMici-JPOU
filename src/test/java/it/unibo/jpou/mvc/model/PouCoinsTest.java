package it.unibo.jpou.mvc.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.jpou.mvc.model.PouCoins.MIN_COINS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PouCoinsTest {

    private static final int POSITIVE_COINS_VALUE = 10_000;
    private static final int NEGATIVE_COINS_VALUE = -50;
    private PouCoins coins;

    @BeforeEach
    void setUp() {
        this.coins = new PouCoins();
    }

    @Test
    void testInitialCoins() {
        assertEquals(MIN_COINS, coins.getCoins(),
                "Il wallet iniziale dev'essere a 0");
    }

    @Test
    void testSetCoinsPositive() {
        coins.setCoins(POSITIVE_COINS_VALUE);
        assertEquals(POSITIVE_COINS_VALUE, coins.getCoins(),
                "Non devono esserci limiti di monete");
    }

    @Test
    void testSetCoinsNegative() {
        coins.setCoins(NEGATIVE_COINS_VALUE);
        assertEquals(MIN_COINS, coins.getCoins(),
                "Il wallet non può essere negativo");
    }

    @Test
    void testPropertyNotNull() {
        assertNotNull(coins.coinsProperty(),
                "La Property non dev'essere nulla");
    }
}
