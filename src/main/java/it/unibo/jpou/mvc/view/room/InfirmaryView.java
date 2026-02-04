package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Objects;

/**
 * Specific view for the Infirmary.
 * Manages potion inventory access and healing actions.
 */
public final class InfirmaryView extends AbstractRoomView {

    private final Button inventoryButton;
    private final Button healButton;
    private final Button shopButton;
    private final Label selectedPotionLabel;

    /**
     * Initializes the infirmary by extending the abstract room structure.
     */
    public InfirmaryView() {
        super("Infirmary");
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleInfirmaryView.css"))
                .toExternalForm());
        this.getStyleClass().add("infirmary-view");

        this.selectedPotionLabel = new Label("");
        this.selectedPotionLabel.getStyleClass().add("selected-potion-label");

        this.getCharacterArea().getChildren().add(this.selectedPotionLabel);

        this.inventoryButton = new Button("Inventory");
        this.healButton = new Button("Heal");
        this.shopButton = new Button("Shop");

        this.healButton.setDisable(true);

        this.getActionBar().getChildren().addAll(
                this.inventoryButton,
                this.healButton,
                this.shopButton
        );
    }

    /**
     * Updates the text showing the selected potion.
     *
     * @param potionName the name of the potion.
     */
    public void setSelectedPotionText(final String potionName) {
        this.selectedPotionLabel.setText(potionName);
    }

    /**
     * Enables or disables the heal button.
     *
     * @param disabled true to disable, false to enable.
     */
    public void setHealDisabled(final boolean disabled) {
        this.healButton.setDisable(disabled);
    }

    /**
     * Sets the handler for the inventory button.
     *
     * @param handler the action to perform.
     */
    public void setOnInventoryAction(final EventHandler<ActionEvent> handler) {
        this.inventoryButton.setOnAction(handler);
    }

    /**
     * Sets the handler for the heal button.
     *
     * @param handler the action to perform.
     */
    public void setOnHealAction(final EventHandler<ActionEvent> handler) {
        this.healButton.setOnAction(handler);
    }

    /**
     * Sets the handler for the shop button.
     *
     * @param handler the action to perform.
     */
    public void setOnShopAction(final EventHandler<ActionEvent> handler) {
        this.shopButton.setOnAction(handler);
    }
}
