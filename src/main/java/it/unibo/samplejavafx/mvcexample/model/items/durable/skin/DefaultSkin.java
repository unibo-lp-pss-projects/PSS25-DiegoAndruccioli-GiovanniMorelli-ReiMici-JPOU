package it.unibo.samplejavafx.mvcexample.model.items.durable.skin;

/**
 * Implementation of the default skin for the character.
 * Provides the baseline aesthetic at no cost.
 */
public class DefaultSkin implements Skin {
        /**
         * @return the Skin name.
         */
        @Override
        public String getName() {
            return "Default";
        }

        /**
         * @return 0 as the default skin is free.
         */
        @Override
        public int getPrice() {
            return 0;
        }

        /**
         * @return the hexadecimal code for the classic color.
         */
        @Override
        public String getColorHex() {
            return "#E3C072";
        }
}
