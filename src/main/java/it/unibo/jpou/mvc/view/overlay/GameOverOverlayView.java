package it.unibo.jpou.mvc.view.overlay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Overlay view displayed when the character dies or the game ends.
 */
public final class GameOverOverlayView extends VBox {

    private static final double SPACING = 25.0;
    private final Button restartButton;

    /**
     * Initializes the game over overlay.
     */
    public GameOverOverlayView() {
        super(SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("game-over-overlay");

        this.getStylesheets().add(getClass().getResource("/style/overlay/styleGameOverOverlayView.css")
                .toExternalForm());

        final Label deathLabel = new Label("POU IS GONE...");
        deathLabel.getStyleClass().add("game-over-title");

        final Label subLabel = new Label("Press the button to close the game and prepare a new game");
        subLabel.getStyleClass().add("game-over-subtitle");

        this.restartButton = new Button("Try Again");
        this.restartButton.getStyleClass().add("restart-button");

        this.getChildren().addAll(deathLabel, subLabel, this.restartButton);
    }

    /**
     * Sets the action for the restart button.
     *
     * @param handler the handler for the restart action.
     */
    public void setOnRestart(final EventHandler<ActionEvent> handler) {
        this.restartButton.setOnAction(handler);
    }
}
