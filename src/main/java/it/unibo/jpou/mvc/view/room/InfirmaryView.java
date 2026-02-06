package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Infirmary.
 */
public final class InfirmaryView extends AbstractRoomView {

    private static final int TOP_PADDING = 50;
    private final Label feedbackLabel;
    private final Button healButton;
    private final Button nextItemButton;

    private List<Potion> availablePotions = new ArrayList<>();
    private Map<Potion, Integer> potionQuantities = new HashMap<>();
    private int currentIndex = -1;
    private Potion currentSelectedPotion;

    private Consumer<Potion> onUsePotionAction;

    /**
     * Initializes the Infirmary layout.
     */
    public InfirmaryView() {
        super("Infirmary");

        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/styleInfirmaryView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("infirmary-view");

        final VBox topContainer = new VBox();
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(TOP_PADDING, 0, 0, 0));

        this.feedbackLabel = new Label("Medicine cabinet empty.");
        this.feedbackLabel.getStyleClass().add("selected-potion-label");

        topContainer.getChildren().add(feedbackLabel);
        this.setTop(topContainer);

        this.healButton = new Button("USE POTION");
        this.healButton.getStyleClass().add("action-button");
        this.healButton.setDisable(true);

        this.nextItemButton = new Button("NEXT ITEM");
        this.nextItemButton.getStyleClass().add("action-button");

        this.nextItemButton.setOnAction(_ -> scrollNextItem());
        this.healButton.setOnAction(_ -> triggerHeal());

        this.getActionBar().getChildren().addAll(this.healButton, this.nextItemButton);
    }

    /**
     * Refresh the potions displayed.
     *
     * @param potions map of potions.
     */
    public void refreshPotions(final Map<Potion, Integer> potions) {
        this.potionQuantities = new HashMap<>(potions);
        this.availablePotions = new ArrayList<>(potions.keySet());

        if (this.availablePotions.isEmpty()) {
            this.currentIndex = -1;
            this.currentSelectedPotion = null;
            this.feedbackLabel.setText("Medicine cabinet empty.");
            this.healButton.setDisable(true);
        } else {
            if (this.currentIndex < 0 || this.currentIndex >= this.availablePotions.size()) {
                this.currentIndex = 0;
            }
            this.currentSelectedPotion = this.availablePotions.get(this.currentIndex);
            this.healButton.setDisable(false);
            updateDisplay();
        }
    }

    private void scrollNextItem() {
        if (!this.availablePotions.isEmpty()) {
            this.currentIndex = (this.currentIndex + 1) % this.availablePotions.size();
            this.currentSelectedPotion = this.availablePotions.get(this.currentIndex);
            updateDisplay();
        }
    }

    private void updateDisplay() {
        if (this.currentSelectedPotion != null) {
            final String name = this.currentSelectedPotion.getName().toUpperCase(Locale.ROOT);
            final int qty = this.potionQuantities.get(this.currentSelectedPotion);
            this.feedbackLabel.setText(name + " (x" + qty + ")");
        }
    }

    private void triggerHeal() {
        if (this.currentSelectedPotion != null && this.onUsePotionAction != null) {
            this.onUsePotionAction.accept(this.currentSelectedPotion);
        }
    }

    /**
     * @param listener handler for potion use.
     */
    public void setOnUsePotion(final Consumer<Potion> listener) {
        this.onUsePotionAction = listener;
    }
}
