package it.unibo.samplejavafx.mvcexample.model.items.durable.skin;

/**
 * Implementation of the red skin for the character.
 */
public class RedSkin implements Skin {

    private static final String SKIN_NAME = "Red Skin";
    private static final int SKIN_PRICE = 50;
    private static final String SKIN_COLOR = "#FF0000";

    /**
     * @return the Skin name.
     */
    @Override
    public String getName() {
        return SKIN_NAME;
    }

    /**
     * @return the cost of the skin.
     */
    @Override
    public int getPrice() {
        return SKIN_PRICE;
    }

    /**
     * @return the hexadecimal code for the red color.
     */
    @Override
    public String getColorHex() {
        return SKIN_COLOR;
    }
}
