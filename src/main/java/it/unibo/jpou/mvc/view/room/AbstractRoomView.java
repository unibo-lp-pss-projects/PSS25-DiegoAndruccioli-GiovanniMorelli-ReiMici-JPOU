package it.unibo.jpou.mvc.view.room;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Objects;
import java.net.URL;

/**
 * Abstract base class for all room views in the game.
 * Provides a common layout structure including a title, a central character area,
 * and a bottom action bar.
 */
public abstract class AbstractRoomView extends BorderPane {

    private final HBox actionBar;
    private final StackPane characterArea;
    private final Label titleLabel;

    /**
     * Initializes the basic layout of the room.
     *
     * @param title the name of the room to be displayed at the top.
     */
    protected AbstractRoomView(final String title) {
        final URL styleUrl = AbstractRoomView.class.getResource("/style/room/defaultRoom.css");

        this.getStylesheets().add(Objects.requireNonNull(styleUrl).toExternalForm());

        this.titleLabel = new Label(title);
        this.titleLabel.getStyleClass().add("room-title");
        this.setTop(this.titleLabel);
        setAlignment(this.titleLabel, Pos.CENTER);

        this.characterArea = new StackPane();
        this.characterArea.getStyleClass().add("character-area");
        this.setCenter(this.characterArea);

        this.actionBar = new HBox();
        this.actionBar.getStyleClass().add("action-bar");
        this.actionBar.setAlignment(Pos.CENTER);
        this.setBottom(this.actionBar);
    }

    /**
     * @return the container for the action buttons.
     */
    protected final HBox getActionBar() {
        return this.actionBar;
    }

    /**
     * @return the central area of the room.
     */
    protected final StackPane getCharacterArea() {
        return this.characterArea;
    }

    /**
     * Updates the title of the room.
     *
     * @param title the new title.
     */
    public final void setTitle(final String title) {
        this.titleLabel.setText(title);
    }
}
