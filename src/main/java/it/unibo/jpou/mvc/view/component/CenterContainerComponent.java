package it.unibo.jpou.mvc.view.component;

import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.beans.property.IntegerProperty;
import javafx.scene.layout.StackPane;

/**
 * Component managing the central area where rooms and the character are displayed.
 * Updated for Dependency Injection.
 */
public final class CenterContainerComponent extends StackPane {

    private final PouCharacterView characterView;

    /**
     * Initializes the center container using an existing character view.
     *
     * @param characterView the shared character view instance (Dependency Injection).
     */
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
