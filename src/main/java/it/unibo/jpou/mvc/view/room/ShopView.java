package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Pos;

import java.util.Objects;

/**
 * View responsible for displaying the in-game Shop.
 * It leverages AbstractRoomView for layout consistency and avoids exposing
 * internal representation.
 */
public final class ShopView extends AbstractRoomView {

    private static final double GAP = 15.0;
    private final FlowPane itemsContainer;
    private final Button backButton;
    private final Label feedbackLabel;

    /**
     * Initializes the shop interface.
     */
    public ShopView() {
        super("Shop");
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleShopView.css"))
                        .toExternalForm());
        this.getStyleClass().add("shop-view");

        this.feedbackLabel = new Label("");
        this.feedbackLabel.getStyleClass().add("shop-feedback-label");

        this.itemsContainer = new FlowPane();
        this.itemsContainer.setAlignment(Pos.CENTER);
        this.itemsContainer.setHgap(GAP);
        this.itemsContainer.setVgap(GAP);
        this.itemsContainer.getStyleClass().add("items-container");

        this.getCharacterArea().getChildren().addAll(this.feedbackLabel, this.itemsContainer);

        this.backButton = new Button("Back");
        this.backButton.getStyleClass().add("action-button");
        this.getActionBar().getChildren().add(this.backButton);
    }

    /**
     * Adds a new item component to the shop display.
     *
     * @param itemNode the graphical node representing the shop item.
     */
    public void addItem(final Node itemNode) {
        this.itemsContainer.getChildren().add(itemNode);
    }

    /**
     * Clears all items from the shop display.
     */
    public void clearItems() {
        this.itemsContainer.getChildren().clear();
    }

    /**
     * Updates the feedback message for the user.
     *
     * @param message the text to show.
     */
    public void setFeedbackText(final String message) {
        this.feedbackLabel.setText(message);
    }

    /**
     * Sets the action for the back button.
     *
     * @param handler the event handler.
     */
    public void setOnBackAction(final EventHandler<ActionEvent> handler) {
        this.backButton.setOnAction(handler);
    }
}
