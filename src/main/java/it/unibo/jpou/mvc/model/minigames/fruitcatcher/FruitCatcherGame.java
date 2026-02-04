package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

import it.unibo.jpou.mvc.model.minigames.Minigame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the Fruit Catcher minigame.
 * The player controls a basket to catch falling fruits while avoiding bombs.
 */
public final class FruitCatcherGame implements Minigame {

    private static final int SPAWN_THRESHOLD = 50;
    private static final int COIN_CONVERSION_RATE = 10;
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int OBJ_SIZE = 40;
    private static final int PLAYER_SIZE = 60;
    private static final int MAX_RANDOM = 1000;
    private static final double GRAVITY = 0.2;
    private static final double PLAYER_Y_POS = GAME_HEIGHT - 100.0;

    private final List<FallingObject> fallingObjects;
    private final Random random;
    private int score;
    private boolean gameOver;
    private double playerX;

    /**
     * Constructs a new FruitCatcherGame.
     */
    public FruitCatcherGame() {
        this.fallingObjects = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public void startGame() {
        this.score = 0;
        this.gameOver = false;
        this.fallingObjects.clear();
        this.playerX = GAME_WIDTH / 2.0;
    }

    @Override
    public void gameLoop(final long now) {
        if (this.gameOver) {
            return;
        }
        spawnObjects();
        updateObjects();
        checkCollisions();
    }

    private void spawnObjects() {
        if (this.random.nextInt(MAX_RANDOM) < SPAWN_THRESHOLD) {
            final double x = this.random.nextInt(GAME_WIDTH - OBJ_SIZE);
            final FallingObject.Type[] types = FallingObject.Type.values();
            final FallingObject.Type selectedType = types[this.random.nextInt(types.length)];

            this.fallingObjects.add(new FallingObject(x, 0, selectedType, OBJ_SIZE, OBJ_SIZE));
        }
    }

    private void updateObjects() {
        final Iterator<FallingObject> iterator = this.fallingObjects.iterator();
        while (iterator.hasNext()) {
            final FallingObject obj = iterator.next();
            obj.fall(GRAVITY);

            if (obj.getY() > GAME_HEIGHT) {
                iterator.remove();
            }
        }
    }

    private void checkCollisions() {
        final Iterator<FallingObject> iterator = this.fallingObjects.iterator();
        while (iterator.hasNext()) {
            final FallingObject obj = iterator.next();
            if (isColliding(obj)) {
                if (obj.isBomb()) {
                    this.gameOver = true;
                } else {
                    this.score += obj.getValue();
                }
                iterator.remove();
            }
        }
    }

    private boolean isColliding(final FallingObject obj) {
        return obj.getX() < this.playerX + PLAYER_SIZE
                && obj.getX() + obj.getWidth() > this.playerX
                && obj.getY() < PLAYER_Y_POS + PLAYER_SIZE
                && obj.getY() + obj.getHeight() > PLAYER_Y_POS;
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
        return this.score / COIN_CONVERSION_RATE;
    }

    /**
     * Updates the player's horizontal position.
     *
     * @param x the new x position
     */
    public void setPlayerPosition(final double x) {
        this.playerX = x;
    }

    /**
     * Returns a safe copy of the falling objects for the view.
     *
     * @return the list of falling objects.
     */
    public List<FallingObject> getFallingObjects() {
        return Collections.unmodifiableList(this.fallingObjects);
    }

    /**
     * @return the current X position of the player.
     */
    public double getPlayerX() {
        return this.playerX;
    }
}
