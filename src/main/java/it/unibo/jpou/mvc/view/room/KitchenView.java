package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Objects;

/**
 * Specific view for the Kitchen.
 * Manages food selection from the fridge and eating actions.
 */
public final class KitchenView extends AbstractRoomView {

    private final Button eatButton;
    private final Button fridgeButton;
    private final Button shopButton;
    private final Label selectedFoodLabel;

    /**
     * Initializes the kitchen layout by extending the abstract room structure.
     */
    public KitchenView() {
        super("Kitchen");
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleKitchenView.css"))
                        .toExternalForm());
        this.getStyleClass().add("kitchen-view");

        this.selectedFoodLabel = new Label("");
        this.selectedFoodLabel.getStyleClass().add("selected-food-label");

        this.getCharacterArea().getChildren().add(this.selectedFoodLabel);

        this.eatButton = new Button("Eat");
        this.fridgeButton = new Button("Fridge");
        this.shopButton = new Button("Shop");

        this.eatButton.setDisable(true);

        this.getActionBar().getChildren().addAll(
                this.eatButton,
                this.fridgeButton,
                this.shopButton
        );
    }

    /**
     * Updates the text showing the currently selected food item.
     *
     * @param foodName the name of the food.
     */
    public void setSelectedFoodText(final String foodName) {
        this.selectedFoodLabel.setText(foodName);
    }

    /**
     * Toggles the eat button state.
     *
     * @param disabled true to disable, false to enable.
     */
    public void setEatDisabled(final boolean disabled) {
        this.eatButton.setDisable(disabled);
    }

    /**
     * Sets the action for the eat button.
     *
     * @param handler the event handler.
     */
    public void setOnEatAction(final EventHandler<ActionEvent> handler) {
        this.eatButton.setOnAction(handler);
    }

    /**
     * Sets the action for the fridge button.
     *
     * @param handler the event handler.
     */
    public void setOnFridgeAction(final EventHandler<ActionEvent> handler) {
        this.fridgeButton.setOnAction(handler);
    }

    /**
     * Sets the action for the shop button.
     *
     * @param handler the event handler.
     */
    public void setOnShopAction(final EventHandler<ActionEvent> handler) {
        this.shopButton.setOnAction(handler);
    }
}
