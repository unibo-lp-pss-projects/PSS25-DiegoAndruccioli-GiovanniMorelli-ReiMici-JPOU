package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.Objects;

/**
 * View for the Bathroom.
 * Displays a central "Wash" button.
 */
public final class BathroomView extends AbstractRoomView {

    private final Button washButton;

    /**
     * Initializes the Bathroom View.
     */
    public BathroomView() {
        super("Bathroom");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleBathroomView.css"))
                        .toExternalForm());

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/defaultRoom.css"))
                        .toExternalForm());

        this.getStyleClass().add("bathroom-view");

        this.washButton = new Button("Wash");
        this.washButton.getStyleClass().add("action-button");

        this.getActionBar().getChildren().add(this.washButton);
    }

    /**
     * Sets the handler for the wash action.
     *
     * @param handler the event handler to set
     */
    public void setOnWashHandler(final EventHandler<ActionEvent> handler) {
        this.washButton.setOnAction(handler);
    }
}
