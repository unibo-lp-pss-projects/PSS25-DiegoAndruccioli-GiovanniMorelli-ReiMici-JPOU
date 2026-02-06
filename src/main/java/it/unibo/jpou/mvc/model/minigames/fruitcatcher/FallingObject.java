package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

/**
 * Represents a single object falling from the top of the screen during the minigame.
 * It handles its own position, speed, type (Fruit or Bomb), and score value.
 */
public class FallingObject {

    /**
     * Defines the type of the falling object, including its score value and whether it is dangerous.
     */
    public enum Type {
        /** Represents an apple worth 1 point. */
        APPLE(1, false),
        /** Represents a pineapple worth 2 points. */
        PINEAPPLE(2, false),
        /** Represents a banana worth 3 points. */
        BANANA(3, false),
        /** Represents a bomb that ends the game. */
        BOMB(0, true);

        private final int scoreValue;
        private final boolean dangerous;

        Type(final int scoreValue, final boolean dangerous) {
            this.scoreValue = scoreValue;
            this.dangerous = dangerous;
        }

        /**
         * @return the score value associated with this item.
         */
        public int getScoreValue() {
            return this.scoreValue;
        }

        /**
         * @return true if this item ends the game, false otherwise.
         */
        public boolean isDangerous() {
            return this.dangerous;
        }
    }

    private static final double BASE_FALL_SPEED = 3.5;

    private final double xPos;
    private double yPos;
    private final Type type;

    /**
     * Constructs a new falling object.
     *
     * @param startX the initial horizontal position.
     * @param startY the initial vertical position.
     * @param type   the type of the object (e.g., APPLE, BOMB).
     * @param width  the width of the object hitbox.
     * @param height the height of the object hitbox.
     */
    public FallingObject(final double startX, final double startY, final Type type, final int width, final int height) {
        this.xPos = startX;
        this.yPos = startY;
        this.type = type;
    }

    /**
     * Updates the object's position by applying gravity to its speed.
     *
     * @param gravity the acceleration factor to apply.
     */
    public void fall(final double gravity) {
        // variabilw locale temporanea invece di campo di classe, visto che viene sempre sovrascritta mentre object cade
        final double currentSpeed = BASE_FALL_SPEED + gravity;
        this.yPos += currentSpeed;
    }

    /**
     * @return the current X position.
     */
    public double getX() {
        return this.xPos;
    }

    /**
     * @return the current Y position.
     */
    public double getY() {
        return this.yPos;
    }

    /**
     * @return the type of the object.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @return the score value of this object.
     */
    public int getValue() {
        return this.type.getScoreValue();
    }

    /**
     * @return true if the object is a bomb.
     */
    public boolean isBomb() {
        return this.type.isDangerous();
    }
}
