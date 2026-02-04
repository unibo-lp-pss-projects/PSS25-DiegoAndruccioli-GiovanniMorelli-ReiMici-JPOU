package it.unibo.jpou.mvc.model.minigames;

/**
 * Represents an object falling from the top of the screen in the minigame.
 * It can be a good item (fruit) or a bad item (poison).
 */
public class FallingObject {

    /**
     * The type of the falling object.
     */
    public enum Type {
        /**
         * Represents a fruit that gives points.
         */
        FRUIT,
        /**
         * Represents an item that reduces score or health.
         */
        BOMB
    }

    private static final int DEFAULT_SPEED = 5;
    private final double xPos;
    private double yPos;
    private final int speed;
    private final Type type;
    private final int width;
    private final int height;

    /**
     * @param x      the initial X position.
     * @param y      the initial Y position.
     * @param type   the type of the object (FRUIT or POISON).
     * @param width  the width of the object hitbox.
     * @param height the height of the object hitbox.
     */
    public FallingObject(final double x, final double y, final Type type, final int width, final int height) {
        this.xPos = x;
        this.yPos = y;
        this.type = type;
        this.width = width;
        this.height = height;
        this.speed = DEFAULT_SPEED;
    }

    /**
     * Updates the position of the object based on its speed.
     */
    public void update() {
        this.yPos += this.speed;
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
     * @return the width of the object.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return the height of the object.
     */
    public int getHeight() {
        return this.height;
    }
}
