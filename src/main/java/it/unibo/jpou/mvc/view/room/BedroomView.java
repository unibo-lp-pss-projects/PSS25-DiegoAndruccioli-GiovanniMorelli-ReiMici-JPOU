package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.PouState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

/**
 * View for the Bedroom.
 * Displays "Sleep/WakeUp" button and "Inventory" button.
 */
public final class BedroomView extends AbstractRoomView {

    private static final String NIGHT_MODE_STYLE = "night-mode";
    private final Button actionButton;
    private final Button inventoryButton;

    /**
     * Initializes the Bedroom View constructing the button and layout.
     */
    public BedroomView() {
        super("Bedroom");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleBedroomView.css"))
                        .toExternalForm());

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/defaultRoom.css"))
                        .toExternalForm());

        this.getStyleClass().add("bedroom-view");

        this.actionButton = new Button("Sleep");
        this.inventoryButton = new Button("Wardrobe");

        this.actionButton.getStyleClass().add("action-button");
        this.inventoryButton.getStyleClass().add("action-button");

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getActionBar().getChildren().addAll(this.inventoryButton, spacer, this.actionButton);
    }

    /**
     * Updates the main action button text based on Pou's state.
     * Activates night-mode style when sleeping.
     *
     * @param state the current state of Pou
     */
    public void updateView(final PouState state) {
        if (state == PouState.SLEEPING) {
            this.actionButton.setText("Wake Up");
            if (!this.getStyleClass().contains(NIGHT_MODE_STYLE)) {
                this.getStyleClass().add(NIGHT_MODE_STYLE);
            }
        } else {
            this.actionButton.setText("Sleep");
            this.getStyleClass().remove(NIGHT_MODE_STYLE);
        }
        this.inventoryButton.setDisable(state == PouState.SLEEPING);
    }

    /**
     * Sets the handler for the sleep/wakeUp action.
     *
     * @param handler the event handler to set
     */
    public void setOnActionHandler(final EventHandler<ActionEvent> handler) {
        this.actionButton.setOnAction(handler);
    }

    /**
     * Sets the handler for the wardrobe (inventory) action.
     *
     * @param handler the event handler to set
     */
    public void setOnInventoryHandler(final EventHandler<ActionEvent> handler) {
        this.inventoryButton.setOnAction(handler);
    }
}
