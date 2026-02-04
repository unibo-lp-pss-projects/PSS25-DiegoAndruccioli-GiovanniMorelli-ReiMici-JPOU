package it.unibo.jpou.mvc.model;

import javafx.beans.property.ReadOnlyIntegerProperty;

/**
 * Represents the wallet of Pou.
 * No maximum limit, but cannot be negative.
 */
public final class PouCoins {

    /**
     * Minimum value for wallet.
     */
    public static final int MIN_COINS = 0;

    /**
     * Initializes the wallet with 0.
     */
    public PouCoins() {
        // default constructor
    }

    /**
     * @return current coin amount.
     */
    public int getCoins() {
        return -1;
    }

    /**
     * Sets the amount of wallet.
     *
     * @param value new coins amount.
     */
    public void setCoins(final int value) {

    }

    /**
     * @return the observable property for GUI
     */
    public ReadOnlyIntegerProperty coinsProperty() {
        return null;
    }
}
