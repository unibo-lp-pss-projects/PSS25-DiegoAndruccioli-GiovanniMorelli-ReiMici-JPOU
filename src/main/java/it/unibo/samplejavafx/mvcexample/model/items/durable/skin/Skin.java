package it.unibo.samplejavafx.mvcexample.model.items.durable.skin;

import it.unibo.samplejavafx.mvcexample.model.items.durable.Durable;

/**
 * Represents a specific durable item that changes the visual appearance (color) of the character.
 * Implementations of this interface (Red, Green, Default) will provide specific CSS color codes.
 */
public interface Skin extends Durable {

    /**
     * Returns the CSS hexadecimal color code associated with this skin.
     * Used by the view to fill the character's body shape.
     *
     * @return a string representing the color.
     */
    String getColorHex();
}
