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
     * Initializes the center container using an existing character view.
     *
     * @param characterView the shared character view instance (Dependency Injection).
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "This component acts as a container for the shared CharacterView."
    )
    public CenterContainerComponent(final PouCharacterView characterView) {
        this.getStyleClass().add("center-container");
        this.characterView = characterView;
        this.characterView.setPickOnBounds(false);
        this.characterView.setMouseTransparent(true);
        this.getChildren().add(this.characterView);
    }

    /**
     * Replaces the current room with a new one.
     *
     * @param roomView the new room to display.
     */
    public void setRoom(final AbstractRoomView roomView) {
        if (this.getChildren().size() > 1) {
            this.getChildren().remove(0);
        }
        this.getChildren().add(0, roomView);
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
     * Sets character visibility.
     *
     * @param visible true to show
     */
    public void setCharacterVisible(final boolean visible) {
        this.characterView.setVisible(visible);
    }

    /**
     * Sets sleeping visuals.
     *
     * @param sleeping true if sleeping
     */
    public void setPouSleeping(final boolean sleeping) {
        this.characterView.setSleeping(sleeping);
    }

    /**
     * Binds size property.
     *
     * @param ageProperty property to bind
     */
    public void bindPouSize(final IntegerProperty ageProperty) {
        this.characterView.bindSize(ageProperty);
    }
}
