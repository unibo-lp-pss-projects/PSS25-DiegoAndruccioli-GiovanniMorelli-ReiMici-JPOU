package it.unibo.jpou.mvc.view.component;

import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.scene.layout.StackPane;

/**
 * Component managing the central area where rooms and the character are displayed.
 * It ensures the dynamic character is always rendered on top of the current room.
 */
public final class CenterContainerComponent extends StackPane {

    private final PouCharacterView characterView;

    /**
     * Initializes the center container, applying styles and loading the dynamic character.
     */
    public CenterContainerComponent() {
        this.getStyleClass().add("center-container");

        this.characterView = new PouCharacterView();

        this.getChildren().add(this.characterView);
    }

    /**
     * Replaces the current room with a new one.
     * The room is inserted at index 0 (background) to ensure the character (index 1) stays on top.
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
     * Sets the character's visibility.
     *
     * @param visible true to show the character, false to hide it.
     */
    public void setCharacterVisible(final boolean visible) {
        this.characterView.setVisible(visible);
    }

    /**
     * Updates the character's sleeping visuals.
     *
     * @param sleeping true to close eyes
     */
    public void setPouSleeping(final boolean sleeping) {
        this.characterView.setSleeping(sleeping);
    }
}
