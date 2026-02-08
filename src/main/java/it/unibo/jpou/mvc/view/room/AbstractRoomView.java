package it.unibo.jpou.mvc.view.room;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;

/**
 * Abstract base class for all room views in the game.
 */
public abstract class AbstractRoomView extends BorderPane {

    private static final int HEADER_SPACING = 10;
    private static final String CSS_PATH = "/style/room/defaultRoom.css";

    private final VBox headerContainer;
    private final Label titleLabel;
    private final StackPane characterArea;
    private final HBox actionBar;

    /**
     * Initializes the architectural layout of the room.
     *
     * @param title the name of the room to be displayed at the top.
     */
    protected AbstractRoomView(final String title) {
        final URL styleUrl = AbstractRoomView.class.getResource(CSS_PATH);
        this.getStylesheets().add(Objects.requireNonNull(styleUrl, "CSS file not found: " + CSS_PATH).toExternalForm());

        this.headerContainer = new VBox(HEADER_SPACING);
        this.headerContainer.setAlignment(Pos.CENTER);

        this.titleLabel = new Label(title);
        this.titleLabel.getStyleClass().add("room-title");

        this.headerContainer.getChildren().add(this.titleLabel);
        this.setTop(this.headerContainer);

        this.characterArea = new StackPane();
        this.characterArea.getStyleClass().add("character-area");
        this.setCenter(this.characterArea);

        this.actionBar = new HBox();
        this.actionBar.getStyleClass().add("action-bar");
        this.actionBar.setAlignment(Pos.CENTER);
        this.setBottom(this.actionBar);
    }

    /**
     * Allows subclasses to inject custom nodes into the header area,
     * directly below the standard title.
     *
     * @param node the JavaFX Node to add.
     */
    protected final void addHeaderContent(final Node node) {
        this.headerContainer.getChildren().add(node);
    }

    /**
     * Provides access to the bottom action bar for adding buttons.
     *
     * @return the HBox container for action buttons.
     */
    protected final HBox getActionBar() {
        return this.actionBar;
    }

    /**
     * Provides access to the central area for character rendering.
     *
     * @return the central StackPane of the room.
     */
    protected final StackPane getCharacterArea() {
        return this.characterArea;
    }

    /**
     * Dynamically updates the room title.
     *
     * @param title the new title string.
     */
    public final void setTitle(final String title) {
        this.titleLabel.setText(title);
    }
}
