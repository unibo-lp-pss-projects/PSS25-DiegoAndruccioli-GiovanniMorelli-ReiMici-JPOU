package it.unibo.jpou.mvc.view.component;

import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Component managing the central area where rooms and the character are displayed.
 * It ensures the character is always rendered on top of the current room.
 */
public final class CenterContainerComponent extends StackPane {

    private static final double POU_SIZE = 200.0;
    private final ImageView tamagotchiView;

    /**
     * Initializes the center container with character view settings.
     */
    public CenterContainerComponent() {
        this.getStyleClass().add("center-container");

        this.tamagotchiView = new ImageView();
        this.tamagotchiView.setFitWidth(POU_SIZE);
        this.tamagotchiView.setFitHeight(POU_SIZE);
        this.tamagotchiView.setPreserveRatio(true);
        this.tamagotchiView.setSmooth(true);

        this.getChildren().add(this.tamagotchiView);
    }

    /**
     * Replaces the current room with a new one.
     * The room is placed at the background (index 0).
     *
     * @param roomView the new room to display.
     */
    public void setRoom(final AbstractRoomView roomView) {
        if (this.getChildren().size() > 1) {
            this.getChildren().removeFirst();
        }
        this.getChildren().addFirst(roomView);
    }

    /**
     * Updates the character's image (e.g., for animations or skins).
     *
     * @param image the new image to display for the Pou.
     */
    public void setPouImage(final Image image) {
        this.tamagotchiView.setImage(image);
    }

    /**
     * Sets the character's visibility.
     *
     * @param visible true to show the character, false to hide it.
     */
    public void setCharacterVisible(final boolean visible) {
        this.tamagotchiView.setVisible(visible);
    }
}
