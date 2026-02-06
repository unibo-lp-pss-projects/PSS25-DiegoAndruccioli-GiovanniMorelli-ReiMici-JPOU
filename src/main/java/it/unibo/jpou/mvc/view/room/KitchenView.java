package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *  View for the Kitchen.
 */
public final class KitchenView extends AbstractRoomView {

    private final Label feedbackLabel;
    private final Button eatButton;
    private final Button inventoryButton;

    private Consumer<Food> onEatAction;

    /**
     * Initializes the Kitchen View layout.
     */
    public KitchenView() {
        super("Kitchen");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleKitchenView.css"))
                        .toExternalForm());
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/defaultRoom.css"))
                        .toExternalForm());
        this.getStyleClass().add("kitchen-view");

        final StackPane centerLayout = new StackPane();
        this.feedbackLabel = new Label("Hungry?");
        this.feedbackLabel.getStyleClass().add("feedback-label");
        centerLayout.getChildren().add(feedbackLabel);
        this.getChildren().add(centerLayout);

        this.eatButton = new Button("Eat");
        this.eatButton.getStyleClass().add("action-button");

        this.inventoryButton = new Button("Inventory");
        this.inventoryButton.getStyleClass().add("action-button");

        this.eatButton.setOnAction(_ -> {
            if (this.onEatAction != null) {
                this.feedbackLabel.setText("Waiting for selection...");
            } else {
                this.feedbackLabel.setText("Chomp Chomp! (Demo)");
            }
        });

        this.getActionBar().getChildren().addAll(this.eatButton, this.inventoryButton);
    }

    /**
     * Updates the view based on inventory.
     *
     * @param inventory The current inventory state.
     */
    public void refreshItems(final Map<Item, Integer> inventory) {
        if (inventory.isEmpty()) {
            this.feedbackLabel.setText("Fridge is empty.");
        } else {
            this.feedbackLabel.setText("You have food available.");
        }
    }

    /**
     * Sets the action to be performed when food is eaten.
     *
     * @param listener The consumer handling the eaten food.
     */
    public void setOnEat(final Consumer<Food> listener) {
        this.onEatAction = listener;
    }
}
