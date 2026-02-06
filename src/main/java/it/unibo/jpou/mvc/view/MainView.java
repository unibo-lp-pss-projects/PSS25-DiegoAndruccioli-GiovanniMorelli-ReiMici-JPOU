package it.unibo.jpou.mvc.view;

import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.component.BottomNavBarComponent;
import it.unibo.jpou.mvc.view.component.CenterContainerComponent;
import it.unibo.jpou.mvc.view.component.TopBarComponent;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Main View container acting as the layout orchestrator.
 */
public final class MainView {

    private final BorderPane mainLayout;
    private final StackPane rootStack;
    private final TopBarComponent topBar;
    private final CenterContainerComponent centerContainer;
    private final BottomNavBarComponent bottomBar;
    private final PouCharacterView characterView;

    /**
     * Initializes the main view structure.
     */
    public MainView() {
        this.mainLayout = new BorderPane();
        this.rootStack = new StackPane(this.mainLayout);

        this.mainLayout.getStyleClass().add("main-view");
        this.mainLayout.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/styleMainView.css")).toExternalForm());

        final String[] statsKeys = {"hunger", "health", "energy", "fun"};
        this.topBar = new TopBarComponent(statsKeys);

        this.characterView = new PouCharacterView();
        this.centerContainer = new CenterContainerComponent(this.characterView);
        this.bottomBar = new BottomNavBarComponent(Room.values());

        this.mainLayout.setTop(this.topBar);
        this.mainLayout.setCenter(this.centerContainer);
        this.mainLayout.setBottom(this.bottomBar);
    }

    /**
     * @return the root JavaFX node.
     */
    public Parent getNode() {
        return this.rootStack;
    }

    /**
     * Updates a statistic bar.
     *
     * @param key statistic name
     * @param progress value 0-1
     * @param text label text
     */
    public void updateStat(final String key, final double progress, final String text) {
        this.topBar.updateStat(key, progress, text);
    }

    /**
     * Sets the room change listener.
     *
     * @param room target room
     * @param listener callback
     */
    public void setOnRoomChange(final Room room, final Consumer<Room> listener) {
        this.bottomBar.setOnRoomChange(room, _ -> listener.accept(room));
    }

    /**
     * Changes the current room view.
     *
     * @param roomView the new view
     */
    public void setRoom(final AbstractRoomView roomView) {
        this.centerContainer.setRoom(roomView);
    }

    /**
     * Sets character visibility.
     *
     * @param visible true to show
     */
    public void setCharacterVisible(final boolean visible) {
        this.centerContainer.setCharacterVisible(visible);
    }

    /**
     * Binds age property to character size.
     *
     * @param ageProperty read-only property
     */
    public void bindPouAge(final ReadOnlyIntegerProperty ageProperty) {
        if (ageProperty instanceof javafx.beans.property.IntegerProperty) {
            this.centerContainer.bindPouSize((javafx.beans.property.IntegerProperty) ageProperty);
        }
    }

    /**
     * Sets sleeping visuals.
     *
     * @param sleeping true if sleeping
     */
    public void setPouSleeping(final boolean sleeping) {
        this.centerContainer.setPouSleeping(sleeping);
    }

    /**
     * Shows a minigame overlay.
     *
     * @param node the minigame view
     */
    public void showMinigame(final Node node) {
        this.rootStack.getChildren().add(node);
    }

    /**
     * Removes a minigame overlay.
     *
     * @param node the minigame view
     */
    public void removeMinigame(final Node node) {
        this.rootStack.getChildren().remove(node);
    }

    /**
     * Updates skin color.
     *
     * @param hexColor hex code
     */
    public void setPouSkinColor(final String hexColor) {
        if (this.characterView != null) {
            this.characterView.updateSkinColor(hexColor);
        }
    }
}
