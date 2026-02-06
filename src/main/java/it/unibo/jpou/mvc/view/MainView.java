package it.unibo.jpou.mvc.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.view.character.PouCharacterView;
import it.unibo.jpou.mvc.view.component.BottomNavBarComponent;
import it.unibo.jpou.mvc.view.component.CenterContainerComponent;
import it.unibo.jpou.mvc.view.component.TopBarComponent;
import it.unibo.jpou.mvc.view.overlay.GameOverOverlayView;
import it.unibo.jpou.mvc.view.overlay.PauseOverlayView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private final PauseOverlayView pauseOverlay;
    private final GameOverOverlayView gameOverOverlay;

    /**
     * Initializes the main view structure.
     */
    public MainView() {
        this.mainLayout = new BorderPane();
        this.rootStack = new StackPane();

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

        this.pauseOverlay = new PauseOverlayView();
        this.gameOverOverlay = new GameOverOverlayView();

        this.pauseOverlay.setVisible(false);
        this.gameOverOverlay.setVisible(false);

        this.rootStack.getChildren().addAll(this.mainLayout, this.pauseOverlay, this.gameOverOverlay);
    }

    /**
     * @return the root JavaFX node.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
            justification = "JavaFX convention requires returning the root node for scene attachment.")
    public Parent getNode() {
        return this.rootStack;
    }

    /**
     * Gets the character view instance.
     * Necessary for passing the visual character to minigames.
     *
     * @return the PouCharacterView.
     */
    public PouCharacterView getPouCharacterView() {
        return this.centerContainer.getPouCharacterView();
    }

    /**
     * Restores the character to the main view container.
     */
    public void restoreCharacter() {
        this.centerContainer.restoreCharacter();
    }

    /**
     * Updates a statistic bar.
     *
     * @param key      statistic name
     * @param progress value 0-1
     * @param text     label text
     */
    public void updateStat(final String key, final double progress, final String text) {
        this.topBar.updateStat(key, progress, text);
    }

    /**
     * Sets the room change listener using Consumer.
     *
     * @param room     target room
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
     * Supports ReadOnlyIntegerProperty from the Model.
     *
     * @param ageProperty read-only property
     */
    public void bindPouAge(final ReadOnlyIntegerProperty ageProperty) {
        if (ageProperty instanceof IntegerProperty) {
            this.centerContainer.bindPouSize((IntegerProperty) ageProperty);
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
     * Updates skin color.
     *
     * @param hexColor hex code
     */
    public void setPouSkinColor(final String hexColor) {
        if (this.characterView != null) {
            this.characterView.updateSkinColor(hexColor);
        }
    }

    /**
     * Shows a minigame view on top of the main interface but below overlays.
     *
     * @param gameNode the visual root of the minigame.
     */
    public void showMinigame(final Parent gameNode) {
        if (!this.rootStack.getChildren().contains(gameNode)) {
            this.rootStack.getChildren().add(1, gameNode);
        }
        gameNode.setVisible(true);
        gameNode.requestFocus();
    }

    /**
     * Shows a generic node as minigame (support for generic Node).
     *
     * @param node the minigame view node.
     */
    public void showMinigame(final Node node) {
        if (node instanceof Parent) {
            showMinigame((Parent) node);
        } else {
            if (!this.rootStack.getChildren().contains(node)) {
                this.rootStack.getChildren().add(1, node);
            }
            node.setVisible(true);
            node.requestFocus();
        }
    }

    /**
     * Removes the minigame view from the interface.
     *
     * @param gameNode the visual root of the minigame to remove.
     */
    public void removeMinigame(final Parent gameNode) {
        this.rootStack.getChildren().remove(gameNode);
        this.mainLayout.requestFocus();
    }

    /**
     * Removes the minigame node from the interface.
     *
     * @param node the visual node of the minigame to remove.
     */
    public void removeMinigame(final Node node) {
        this.rootStack.getChildren().remove(node);
    }

    /**
     * Sets the handler for the resume action in the pause overlay.
     *
     * @param handler the event handler to be invoked.
     */
    public void setOnResumeAction(final EventHandler<ActionEvent> handler) {
        this.pauseOverlay.setOnResume(handler);
    }

    /**
     * Sets the handler for the restart action in the game over overlay.
     *
     * @param handler the event handler to be invoked.
     */
    public void setOnRestartAction(final EventHandler<ActionEvent> handler) {
        this.gameOverOverlay.setOnRestart(handler);
    }

    /**
     * Sets the visibility of the pause overlay.
     *
     * @param visible true to show the pause overlay, false to hide it.
     */
    public void setPauseVisible(final boolean visible) {
        this.pauseOverlay.setVisible(visible);
        if (visible) {
            this.pauseOverlay.toFront();
        }
    }

    /**
     * Sets the visibility of the game over overlay.
     *
     * @param visible true to show the game over overlay, false to hide it.
     */
    public void setGameOverVisible(final boolean visible) {
        this.gameOverOverlay.setVisible(visible);
        if (visible) {
            this.gameOverOverlay.toFront();
        }
    }

    /**
     * Sets the handler for the settings button action.
     *
     * @param handler the event handler to be invoked
     */
    public void setOnSettingsAction(final EventHandler<ActionEvent> handler) {
        this.topBar.setOnSettingsAction(handler);
    }

    /**
     * Sets the handler for the quit action in PauseOverlayView.
     *
     * @param handler the event handler to be invoked
     */
    public void setOnQuitAction(final EventHandler<ActionEvent> handler) {
        this.pauseOverlay.setOnQuit(handler);
    }
}
