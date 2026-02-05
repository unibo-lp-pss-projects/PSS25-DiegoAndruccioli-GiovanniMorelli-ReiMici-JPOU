package it.unibo.jpou.mvc.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents the wallet of Pou.
 * No maximum limit, but cannot be negative.
 */
public final class PouCoins {

    /**
     * Minimum value for wallet.
     */
    public static final int MIN_COINS = 0;

    private final IntegerProperty coins;

    /**
     * Initializes the wallet with 0.
     */
    public PouCoins() {
        this.coins = new SimpleIntegerProperty(MIN_COINS);
    }

    /**
     * @return current coin amount.
     */
    public int getCoins() {
        return this.coins.get();
    }

    /**
     * Sets the amount of wallet.
     *
     * @param value new coins amount.
     */
    public void setCoins(final int value) {
        this.coins.set(Math.max(MIN_COINS, value));
    }

    /**
     * @return the observable property for GUI
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
            justification = "Standard JavaFX pattern implies returning the property object")
    public ReadOnlyIntegerProperty coinsProperty() {
        return this.coins;
    }
}
