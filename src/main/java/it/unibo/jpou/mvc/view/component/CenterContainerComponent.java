package it.unibo.jpou.mvc.view.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.beans.property.IntegerProperty;
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
     * Returns the character view instance.
     *
     * @return the character view instance.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
            justification = "Access to the view node is required for the minigame architecture")
    public PouCharacterView getPouCharacterView() {
        return this.characterView;
    }

    /**
     * Restores the character in this container after it has been used elsewhere (e.g., a minigame).
     * Also resets layouts and transformations.
     */
    public void restoreCharacter() {
        if (!this.getChildren().contains(this.characterView)) {
            // reset proprietà modificate dal minigioco
            this.characterView.setManaged(true);
            this.characterView.setTranslateX(0);
            this.characterView.setTranslateY(0);
            this.characterView.setLayoutX(0);
            this.characterView.setLayoutY(0);

            this.getChildren().add(this.characterView);
        }
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

    /**
     * Binds the character size to the logic age.
     *
     * @param ageProperty the property representing the age of the character.
     */
    public void bindPouSize(final IntegerProperty ageProperty) {
        this.characterView.bindSize(ageProperty);
    }
}
