package it.unibo.jpou.mvc.view.minigames;

import it.unibo.jpou.mvc.model.minigames.fruitcatcher.FallingObject;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * The graphical view for the "Fruit Catcher" minigame.
 * It uses a Canvas to render the game state (falling objects, score, timer) at 60 FPS.
 * Adapted from the legacy view to match the new MVC architecture.
 */
public final class FruitCatcherJavaFXView extends StackPane implements FruitCatcherView {

    private static final double GAME_W = 800;
    private static final double GAME_H = 600;
    private static final double OBJ_SIZE = 40;
    private static final double GRASS_HEIGHT = 50;
    private static final double PLAYER_Y_OFFSET = 100;
    private static final double PLAYER_SIZE = 60;

    private static final String FONT_FAMILY = "Arial";

    private static final double MOUNTAIN_1_PEAK_X = 0.3;
    private static final double MOUNTAIN_1_HEIGHT = 100;
    private static final double MOUNTAIN_2_PEAK_X = 0.5;
    private static final double MOUNTAIN_2_END_X = 0.8;
    private static final double MOUNTAIN_2_HEIGHT = 80;

    private static final double EYE_OFFSET_X_LEFT = 12;
    private static final double EYE_OFFSET_X_RIGHT = 33;
    private static final double EYE_OFFSET_Y = 15;
    private static final double EYE_SIZE = 15;
    private static final double PUPIL_OFFSET_X_LEFT = 17;
    private static final double PUPIL_OFFSET_X_RIGHT = 38;
    private static final double PUPIL_OFFSET_Y = 20;
    private static final double PUPIL_SIZE = 5;

    private static final double APPLE_STEM_Y_OFFSET = 5;
    private static final double APPLE_STEM_WIDTH = 10;
    private static final double APPLE_STEM_HEIGHT = 4;
    private static final double BANANA_ARC_START = 250;
    private static final double BANANA_ARC_LENGTH = 150;
    private static final double PINEAPPLE_OFFSET_X = 5;
    private static final double PINEAPPLE_WIDTH_REDUCTION = 10;
    private static final double PINEAPPLE_LEAF_X = 10;
    private static final double PINEAPPLE_LEAF_Y = 8;
    private static final double BOMB_FUSE_HEIGHT = 5;

    private static final double HUD_MARGIN_X = 10;
    private static final double HUD_MARGIN_Y = 30;
    private static final double HUD_FONT_SIZE = 20;
    private static final double TIMER_X_OFFSET = 120;

    private static final double OVERLAY_OPACITY = 0.6;
    private static final double GO_TITLE_FONT_SIZE = 40;
    private static final double GO_TITLE_X_OFFSET = 120;
    private static final double GO_TITLE_Y_OFFSET = 20;
    private static final double GO_SCORE_FONT_SIZE = 25;
    private static final double GO_SCORE_X_OFFSET = 90;
    private static final double GO_SCORE_Y_OFFSET = 30;
    private static final double GO_SUB_FONT_SIZE = 16;
    private static final double GO_SUB_X_OFFSET = 80;
    private static final double GO_SUB_Y_OFFSET = 70;

    private final Canvas backgroundCanvas;
    private final GraphicsContext bgGc;

    private final Canvas gameCanvas;
    private final GraphicsContext gameGc;

    /**
     * Constructor. Initializes the canvases and layout.
     */
    public FruitCatcherJavaFXView() {
        this.setPrefSize(GAME_W, GAME_H);

        this.backgroundCanvas = new Canvas(0, 0);
        this.bgGc = backgroundCanvas.getGraphicsContext2D();
        backgroundCanvas.widthProperty().bind(this.widthProperty());
        backgroundCanvas.heightProperty().bind(this.heightProperty());

        this.gameCanvas = new Canvas(0, 0);
        this.gameGc = gameCanvas.getGraphicsContext2D();
        gameCanvas.widthProperty().bind(this.widthProperty());
        gameCanvas.heightProperty().bind(this.heightProperty());

        this.getChildren().addAll(backgroundCanvas, gameCanvas);

        this.setFocusTraversable(true);
    }

    /**
     * ### Rendering Grafico del Minigioco (Canvas & GraphicsContext).
     *
     * <p>Per gestire il rendering del minigioco "Fruit Catcher" a 60 FPS, ho scelto
     * di utilizzare l'API `Canvas` di JavaFX (Immediate Mode rendering).</p>
     *
     * <p>Questa scelta tecnica permette di gestire frequenti aggiornamenti di posizione
     * di molti oggetti (frutta e bombe) evitando overhead per dover
     * gestire nodi JavaFX complessi per ogni entità.</p>
     *
     * <p><strong>Tecniche Implementate:</strong></p>
     * <ul>
     * <li><strong>Loop di Rendering:</strong> Il metodo `render()` pulisce (`clearRect`) e ridisegna
     * l'intera scena ad ogni tick del GameLoop.</li>
     * <li><strong>Matrix Stack (save/restore):</strong> Utilizzo `gc.save()` e `gc.restore()` per isolare
     * le trasformazioni (es. traslazione per centrare il gioco nello schermo)
     * in modo che non influenzino i disegni successivi o i frame futuri.</li>
     * <li><strong>Disegno Procedurale:</strong> Sfondo e oggetti sono disegnati tramite primitive
     * geometriche (`fillPolygon`, `fillOval`) invece di immagini statiche.</li>
     * </ul>
     *
     * <p><strong>Riferimenti:</strong></p>
     * <ul>
     * <li>JavaFX Canvas Documentation</li>
     * <li>GraphicsContext API</li>
     * <li>Concetti di "Immediate Mode" vs "Retained Mode" GUI</li>
     * </ul>
     *
     * @param objects   the list of falling items (apples, bombs) to draw.
     * @param score     the current score to display.
     * @param timeLeft  the remaining time in seconds.
     * @param gameOver  true if the "Game Over" screen should be shown.
     * @param playerX   the current X position of the player (Pou/Basket).
     */
    @Override
    public void render(final List<FallingObject> objects,
                       final int score,
                       final double timeLeft,
                       final boolean gameOver,
                       final double playerX) {

        final double currentWidth = getWidth();
        final double currentHeight = getHeight();

        // calcola quanto spostarsi per centrare il box
        final double offsetX = (currentWidth - GAME_W) / 2;
        final double offsetY = (currentHeight - GAME_H) / 2;

        drawWideBackground();

        gameGc.clearRect(0, 0, currentWidth, currentHeight);
        gameGc.save();

        if (offsetX > 0 || offsetY > 0) {
            gameGc.translate(offsetX, offsetY);
        }

        drawPouPlaceholder(playerX);

        for (final FallingObject obj : objects) {
            drawFallingObject(obj);
        }

        drawHUD(score);
        drawTimer(timeLeft);

        gameGc.restore();

        if (gameOver) {
            drawGameOver(score, currentWidth, currentHeight);
        }
    }

    private void drawWideBackground() {
        final double w = getWidth();
        final double h = getHeight();
        final double horizonY = h - GRASS_HEIGHT;

        bgGc.setFill(Color.SKYBLUE);
        bgGc.fillRect(0, 0, w, h);

        bgGc.setFill(Color.FORESTGREEN);

        bgGc.fillPolygon(
                new double[]{0, w * MOUNTAIN_1_PEAK_X, w * MOUNTAIN_2_PEAK_X},
                new double[]{horizonY, horizonY - MOUNTAIN_1_HEIGHT, horizonY},
                3
        );
        bgGc.fillPolygon(
                new double[]{w * MOUNTAIN_2_PEAK_X, w * MOUNTAIN_2_END_X, w},
                new double[]{horizonY, horizonY - MOUNTAIN_2_HEIGHT, horizonY},
                3
        );

        bgGc.fillRect(0, horizonY, w, GRASS_HEIGHT);
    }

    private void drawPouPlaceholder(final double x) {
        final double y = GAME_H - PLAYER_Y_OFFSET;

        gameGc.setFill(Color.PERU);
        gameGc.fillOval(x, y, PLAYER_SIZE, PLAYER_SIZE);

        gameGc.setFill(Color.WHITE);
        gameGc.fillOval(x + EYE_OFFSET_X_LEFT, y + EYE_OFFSET_Y, EYE_SIZE, EYE_SIZE);
        gameGc.fillOval(x + EYE_OFFSET_X_RIGHT, y + EYE_OFFSET_Y, EYE_SIZE, EYE_SIZE);
        gameGc.setFill(Color.BLACK);
        gameGc.fillOval(x + PUPIL_OFFSET_X_LEFT, y + PUPIL_OFFSET_Y, PUPIL_SIZE, PUPIL_SIZE);
        gameGc.fillOval(x + PUPIL_OFFSET_X_RIGHT, y + PUPIL_OFFSET_Y, PUPIL_SIZE, PUPIL_SIZE);
    }

    private void drawFallingObject(final FallingObject obj) {
        final double x = obj.getX();
        final double y = obj.getY();
        final double size = OBJ_SIZE;

        switch (obj.getType()) {
            case APPLE -> {
                gameGc.setFill(Color.RED);
                gameGc.fillOval(x, y, size, size);
                gameGc.setFill(Color.GREEN);
                gameGc.fillOval(x + size / 2.0, y - APPLE_STEM_Y_OFFSET, APPLE_STEM_WIDTH, APPLE_STEM_HEIGHT);
            }
            case BANANA -> {
                gameGc.setFill(Color.GOLD);
                gameGc.fillArc(x, y, size, size, BANANA_ARC_START, BANANA_ARC_LENGTH, ArcType.CHORD);
            }
            case PINEAPPLE -> {
                gameGc.setFill(Color.ORANGE);
                gameGc.fillOval(x + PINEAPPLE_OFFSET_X, y, size - PINEAPPLE_WIDTH_REDUCTION, size);
                gameGc.setFill(Color.GREEN);
                gameGc.fillRect(x + PINEAPPLE_LEAF_X, y - PINEAPPLE_LEAF_Y, PINEAPPLE_WIDTH_REDUCTION, PINEAPPLE_LEAF_Y);
            }
            case BOMB -> {
                gameGc.setFill(Color.BLACK);
                gameGc.fillOval(x, y, size, size);
                gameGc.setStroke(Color.WHITE);
                gameGc.strokeLine(x + size / 2.0, y, x + size / 2.0, y - BOMB_FUSE_HEIGHT);
            }
        }
    }

    private void drawHUD(final int score) {
        gameGc.setFill(Color.WHITE);
        gameGc.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, HUD_FONT_SIZE));
        gameGc.fillText("Score: " + score, HUD_MARGIN_X, HUD_MARGIN_Y);
    }

    private void drawTimer(final double time) {
        gameGc.setFill(Color.WHITE);
        gameGc.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, HUD_FONT_SIZE));

        final String timeText = "Time: " + (int) time + "s";
        gameGc.fillText(timeText, GAME_W - TIMER_X_OFFSET, HUD_MARGIN_Y);

        if (time < 10) {
            gameGc.setFill(Color.RED);
            gameGc.fillText(timeText, GAME_W - TIMER_X_OFFSET, HUD_MARGIN_Y);
        }
    }

    private void drawGameOver(final int score, final double w, final double h) {
        gameGc.setFill(Color.rgb(0, 0, 0, OVERLAY_OPACITY));
        gameGc.fillRect(0, 0, w, h);

        gameGc.setFill(Color.RED);
        gameGc.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, GO_TITLE_FONT_SIZE));
        gameGc.fillText("GAME OVER", w / 2 - GO_TITLE_X_OFFSET, h / 2 - GO_TITLE_Y_OFFSET);

        gameGc.setFill(Color.GOLD);
        gameGc.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, GO_SCORE_FONT_SIZE));
        gameGc.fillText("Final Score: " + score, w / 2 - GO_SCORE_X_OFFSET, h / 2 + GO_SCORE_Y_OFFSET);

        gameGc.setFill(Color.WHITE);
        gameGc.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, GO_SUB_FONT_SIZE));
        gameGc.fillText("Press ESC to exit\"", w / 2 - GO_SUB_X_OFFSET, h / 2 + GO_SUB_Y_OFFSET);
    }

    @Override
    public void setKeyListener(final EventHandler<KeyEvent> handler) {
        this.setOnKeyPressed(handler);
    }

    @Override
    public Parent getNode() {
        return this;
    }
}
