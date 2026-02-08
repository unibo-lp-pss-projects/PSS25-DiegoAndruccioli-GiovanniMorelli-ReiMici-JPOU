package it.unibo.jpou.mvc.view.overlay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Overlay view displayed when the game is paused.
 * Provides options to resume or exit the game.
 */
public final class PauseOverlayView extends VBox {

    private static final double SPACING = 20.0;
    private final Button resumeButton;
    private final Button quitButton;

    /**
     * Initializes the pause overlay with its controls.
     */
    public PauseOverlayView() {
        super(SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("pause-overlay");

        this.getStylesheets().add(getClass().getResource("/style/overlay/stylePauseOverlay.css")
                .toExternalForm());

        final Label pauseLabel = new Label("GAME PAUSED");
        pauseLabel.getStyleClass().add("overlay-title");

        this.resumeButton = new Button("Resume");
        this.resumeButton.getStyleClass().add("overlay-button");
        this.resumeButton.getStyleClass().add("resume-button");

        this.quitButton = new Button("Save and Exit");
        this.quitButton.getStyleClass().add("overlay-button");
        this.quitButton.getStyleClass().add("quit-button");

        this.getChildren().addAll(pauseLabel, this.resumeButton, this.quitButton);
    }

    /**
     * Sets the action for the resume button.
     *
     * @param handler the handler for the resume action.
     */
    public void setOnResume(final EventHandler<ActionEvent> handler) {
        this.resumeButton.setOnAction(handler);
    }

    /**
     * Sets the action for the quit button.
     *
     * @param handler the handler for the quit action.
     */
    public void setOnQuit(final EventHandler<ActionEvent> handler) {
        this.quitButton.setOnAction(handler);
    }
}
