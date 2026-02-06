package it.unibo.jpou.mvc.model.items.durable.skin;

/**
 * Implementation of the default skin for the character.
 * Provides the baseline aesthetic at no cost.
 */
public class DefaultSkin implements Skin {

    public static final String DEFAULT_NAME = "Default";
    private static final int DEFAULT_PRICE = 0;
    private static final String DEFAULT_COLOR = "#E3C072";

    /**
     * @return the Skin name.
     */
    @Override
    public String getName() {
        return DEFAULT_NAME;
    }

    /**
     * @return 0 as the default skin is free.
     */
    @Override
    public int getPrice() {
        return DEFAULT_PRICE;
    }

    /**
     * @return the hexadecimal code for the classic color.
     */
    @Override
    public String getColorHex() {
        return DEFAULT_COLOR;
    }
}
