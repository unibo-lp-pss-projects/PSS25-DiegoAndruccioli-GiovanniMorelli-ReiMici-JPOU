package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Infirmary.
 */
public final class InfirmaryView extends AbstractRoomView {

    private final Label feedbackLabel;
    private final Button usePotionButton;
    private final Button inventoryButton;

    private Consumer<Potion> onUsePotionAction;

    /**
     * Initializes the Infirmary layout.
     */
    public InfirmaryView() {
        super("Infirmary");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleInfirmaryView.css"))
                        .toExternalForm());
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/defaultRoom.css"))
                        .toExternalForm());
        this.getStyleClass().add("infirmary-view");

        final StackPane centerLayout = new StackPane();
        this.feedbackLabel = new Label("Pou feels sick?");
        this.feedbackLabel.getStyleClass().add("feedback-label");
        centerLayout.getChildren().add(feedbackLabel);
        this.getChildren().add(centerLayout);

        this.usePotionButton = new Button("Heal");
        this.usePotionButton.getStyleClass().add("action-button");

        this.inventoryButton = new Button("Inventory");
        this.inventoryButton.getStyleClass().add("action-button");

        this.usePotionButton.setOnAction(_ -> {
            if (this.onUsePotionAction != null) {
                this.feedbackLabel.setText("Healing...");
            } else {
                this.feedbackLabel.setText("Gulp! (Demo)");
            }
        });

        this.getActionBar().getChildren().addAll(this.usePotionButton, this.inventoryButton);
    }

    /**
     * Updates the view based on inventory.
     *
     * @param inventory The current inventory state.
     */
    public void refreshItems(final Map<Item, Integer> inventory) {
        // FIX: Variable 'hasPotions' should be final
        final boolean hasPotions = inventory.keySet().stream().anyMatch(i -> i instanceof Potion);

        if (!hasPotions) {
            this.feedbackLabel.setText("No medicine available.");
        } else {
            this.feedbackLabel.setText("You have potions ready.");
        }
    }

    /**
     * Sets the handler for when a potion is used.
     *
     * @param listener The callback.
     */
    public void setOnUsePotion(final Consumer<Potion> listener) {
        this.onUsePotionAction = listener;
    }
}
