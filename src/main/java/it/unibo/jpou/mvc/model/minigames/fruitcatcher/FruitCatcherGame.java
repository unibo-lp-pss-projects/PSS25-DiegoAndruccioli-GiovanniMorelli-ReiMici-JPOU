package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

import it.unibo.jpou.mvc.model.minigames.Minigame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the "Fruit Catcher" minigame.
 * The player controls Pou moving horizontally to catch fruits (points) and avoid bombs (game over).
 */
public final class FruitCatcherGame implements Minigame {

    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 550;
    private static final int OBJ_SIZE = 35;
    private static final int PLAYER_SIZE = 60;
    private static final double PLAYER_Y_POS = 430;
    private static final double GRAVITY = 2.5;
    private static final double MAX_TIME = 60.0;
    private static final double TIME_DECREMENT = 0.017;

    private static final double SPAWN_PROBABILITY = 0.02;

    // 0.0 - 0.2: BOMBA (20%)
    // 0.2 - 0.3: ANANAS (20%)
    // 0.3 - 0.5: BANANA (10%)
    // 0.5 - 1.0: MELA (50%)
    private static final double BOMB_CHANCE_THRESHOLD = 0.2;
    private static final double BANANA_CHANCE_THRESHOLD = 0.3;
    private static final double PINEAPPLE_CHANCE_THRESHOLD = 0.5;

    private static final double HITBOX_TOLERANCE_FACTOR = 1.0;
    private static final double PLAYER_BOUNDARY_OFFSET = 50.0;

    private final List<FallingObject> fallingObjects;
    private final Random random;

    private int score;
    private boolean gameOver;
    private double pouX;
    private double timeLeft;

    /**
     * Constructs a new FruitCatcherGame.
     */
    public FruitCatcherGame() {
        this.fallingObjects = new ArrayList<>();
        this.random = new Random();
        this.timeLeft = MAX_TIME;
        this.gameOver = false;
        this.score = 0;
    }

    @Override
    public void startGame() {
        this.score = 0;
        this.gameOver = false;
        this.fallingObjects.clear();
        this.pouX = GAME_WIDTH / 2.0;
        this.timeLeft = MAX_TIME;
    }

    @Override
    public void gameLoop(final long now) {
        if (this.gameOver) {
            return;
        }

        //  TIMER
        this.timeLeft -= TIME_DECREMENT;
        if (this.timeLeft <= 0) {
            this.timeLeft = 0;
            this.gameOver = true;
            return;
        }

        // OGGETTI
        if (this.random.nextDouble() < SPAWN_PROBABILITY) {
            spawnObject();
        }

        // COLLISIONI
        updateAndCheckCollisions();
    }

    private void spawnObject() {
        final double x = this.random.nextDouble() * (GAME_WIDTH - OBJ_SIZE);

        final FallingObject.Type type;
        final double chance = this.random.nextDouble();

        if (chance < BOMB_CHANCE_THRESHOLD) {
            type = FallingObject.Type.BOMB;
        } else if (chance < BANANA_CHANCE_THRESHOLD) {
            type = FallingObject.Type.PINEAPPLE;
        } else if (chance < PINEAPPLE_CHANCE_THRESHOLD) {
            type = FallingObject.Type.BANANA;
        } else {
            type = FallingObject.Type.APPLE;
        }

        this.fallingObjects.add(new FallingObject(x, 0, type, OBJ_SIZE, OBJ_SIZE));
    }

    private void updateAndCheckCollisions() {
        final Iterator<FallingObject> iterator = this.fallingObjects.iterator();

        while (iterator.hasNext()) {
            final FallingObject obj = iterator.next();

            obj.fall(GRAVITY);

            final boolean hitX = Math.abs(obj.getX() - this.pouX) < (PLAYER_SIZE / HITBOX_TOLERANCE_FACTOR);
            final boolean hitY = obj.getY() > PLAYER_Y_POS && obj.getY() < PLAYER_Y_POS + PLAYER_SIZE;

            if (hitX && hitY) {
                if (obj.isBomb()) {
                    this.gameOver = true;
                } else {
                    this.score += obj.getValue();
                }
                iterator.remove();
            } else if (obj.getY() > GAME_HEIGHT) {
                iterator.remove();
            }
        }
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public boolean isGameOver() {
        return this.gameOver;
    }

    @Override
    public int calculateCoins() {
        return this.score;
    }

    /**
     * Sets the player horizontal position.
     *
     * @param x the new x position.
     */
    public void setPlayerPosition(final double x) {
        if (x < 0) {
            this.pouX = 0;
        } else if (x > GAME_WIDTH - PLAYER_BOUNDARY_OFFSET) {
            this.pouX = GAME_WIDTH - PLAYER_BOUNDARY_OFFSET;
        } else {
            this.pouX = x;
        }
    }

    /**
     * @return current player X position.
     */
    public double getPlayerX() {
        return this.pouX;
    }

    /**
     * @return unmodifiable list of falling objects.
     */
    public List<FallingObject> getFallingObjects() {
        return Collections.unmodifiableList(this.fallingObjects);
    }

    /**
     * @return remaining time in seconds.
     */
    public double getTimeLeft() {
        return this.timeLeft;
    }
}
