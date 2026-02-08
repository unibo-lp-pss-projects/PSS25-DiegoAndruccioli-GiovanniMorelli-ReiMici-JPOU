package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.items.durable.skin.Skin;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Bedroom.
 */
public final class BedroomView extends AbstractRoomView {

    private static final String NIGHT_MODE_STYLE = "night-mode";
    private static final String ACTION_BTN_STYLE = "action-button";

    private final Label feedbackLabel;
    private final Button actionButton;
    private final Button nextSkinButton;
    private final Button selectSkinButton;

    private List<Skin> availableSkins = new ArrayList<>();
    private int currentIndex = -1;
    private Consumer<Skin> onSkinSelected;

    /**
     * Initializes the Bedroom layout.
     */
    public BedroomView() {
        super("Bedroom");

        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/styleBedroomView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("bedroom-view");

        this.feedbackLabel = new Label("Wardrobe empty");
        this.feedbackLabel.getStyleClass().add("selected-item-label");

        this.addHeaderContent(this.feedbackLabel);

        this.actionButton = new Button("Sleep");
        this.actionButton.getStyleClass().add(ACTION_BTN_STYLE);

        this.nextSkinButton = new Button("Next Skin");
        this.nextSkinButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.nextSkinButton.setOnAction(_ -> scrollNextSkin());

        this.selectSkinButton = new Button("Wear");
        this.selectSkinButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.selectSkinButton.setOnAction(_ -> triggerWear());

        this.getActionBar().getChildren().addAll(
                this.nextSkinButton,
                this.selectSkinButton,
                this.actionButton
        );
    }

    /**
     * Refresh the list of owned skins.
     *
     * @param skins the map of skins from inventory.
     */
    public void refreshSkins(final Map<Skin, Integer> skins) {
        this.availableSkins = new ArrayList<>(skins.keySet());
        if (this.availableSkins.isEmpty()) {
            this.feedbackLabel.setText("Wardrobe empty");
            this.feedbackLabel.setStyle("");
            this.currentIndex = -1;
            this.selectSkinButton.setDisable(true);
            this.nextSkinButton.setDisable(true);
        } else {
            this.currentIndex = 0;
            this.selectSkinButton.setDisable(false);
            this.nextSkinButton.setDisable(false);
            updateDisplay();
        }
    }

    /**
     * Updates the UI based on Pou's state.
     * Handles text changes and Night Mode CSS.
     *
     * @param state the current state of Pou.
     */
    public void updateView(final PouState state) {
        final boolean isSleeping = state == PouState.SLEEPING;

        this.actionButton.setText(isSleeping ? "Wake Up" : "Sleep");

        this.nextSkinButton.setDisable(isSleeping);
        this.selectSkinButton.setDisable(isSleeping);

        if (isSleeping) {
            if (!this.getStyleClass().contains(NIGHT_MODE_STYLE)) {
                this.getStyleClass().add(NIGHT_MODE_STYLE);
            }
        } else {
            this.getStyleClass().remove(NIGHT_MODE_STYLE);
        }
    }

    /**
     * Sets the handler for the Sleep/Wake button.
     *
     * @param handler the consumer (accepts null).
     */
    public void setOnActionHandler(final Consumer<Void> handler) {
        this.actionButton.setOnAction(_ -> handler.accept(null));
    }

    /**
     * Sets the handler for the "Wear" button.
     *
     * @param handler the consumer accepting the selected Skin.
     */
    public void setOnSkinSelected(final Consumer<Skin> handler) {
        this.onSkinSelected = handler;
    }

    private void scrollNextSkin() {
        if (!this.availableSkins.isEmpty()) {
            this.currentIndex = (this.currentIndex + 1) % this.availableSkins.size();
            updateDisplay();
        }
    }

    private void triggerWear() {
        if (this.currentIndex >= 0 && !this.availableSkins.isEmpty() && this.onSkinSelected != null) {
            this.onSkinSelected.accept(this.availableSkins.get(this.currentIndex));
        }
    }

    private void updateDisplay() {
        if (this.currentIndex >= 0 && this.currentIndex < this.availableSkins.size()) {
            final Skin current = this.availableSkins.get(this.currentIndex);
            this.feedbackLabel.setText(current.getName().toUpperCase(Locale.ROOT));
            this.feedbackLabel.setStyle("-fx-text-fill: " + current.getColorHex() + ";");
        }
    }
}
