package it.unibo.jpou.mvc.view;

import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.view.component.BottomNavBarComponent;
import it.unibo.jpou.mvc.view.component.CenterContainerComponent;
import it.unibo.jpou.mvc.view.component.TopBarComponent;
import it.unibo.jpou.mvc.view.overlay.GameOverOverlayView;
import it.unibo.jpou.mvc.view.overlay.PauseOverlayView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.util.Objects;

/**
 * The root container of the UI.
 */
public final class MainView extends StackPane {

    private static final String[] STATS = {"hunger", "energy", "fun", "health"};

    private final TopBarComponent topBar;
    private final BottomNavBarComponent bottomBar;
    private final CenterContainerComponent centerContainer;
    private final PauseOverlayView pauseOverlay;
    private final GameOverOverlayView gameOverOverlay;

    private final BorderPane mainLayout;

    /**
     * Assemblies the entire game interface.
     */
    public MainView() {
        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/style/styleMainView.css")).toExternalForm());

        this.topBar = new TopBarComponent(STATS);
        this.bottomBar = new BottomNavBarComponent(Room.values());
        this.centerContainer = new CenterContainerComponent();
        this.pauseOverlay = new PauseOverlayView();
        this.gameOverOverlay = new GameOverOverlayView();

        this.mainLayout = new BorderPane();
        this.mainLayout.setTop(this.topBar);
        this.mainLayout.setCenter(this.centerContainer);
        this.mainLayout.setBottom(this.bottomBar);

        this.pauseOverlay.setVisible(false);
        this.gameOverOverlay.setVisible(false);

        this.getChildren().addAll(mainLayout, this.pauseOverlay, this.gameOverOverlay);
    }

    /**
     * Shows a minigame view on top of the main interface.
     *
     * @param gameNode the visual root of the minigame.
     */
    public void showMinigame(final Parent gameNode) {
        if (!this.getChildren().contains(gameNode)) {
            this.getChildren().add(1, gameNode); //livello 1: minigioco. sovrappone la 0
        }
        gameNode.setVisible(true);
        gameNode.requestFocus();
    }

    /**
     * Removes the minigame view from the interface.
     *
     * @param gameNode the visual root of the minigame to remove.
     */
    public void removeMinigame(final Parent gameNode) {
        this.getChildren().remove(gameNode);
        this.mainLayout.requestFocus();
    }

    /**
     * Updates a statistic in the top bar.
     *
     * @param key the stat name.
     * @param val the value (0.0 - 1.0).
     * @param txt the text label.
     */
    public void updateStat(final String key, final double val, final String txt) {
        this.topBar.updateStat(key, val, txt);
    }

    /**
     * Sets the room change handler.
     *
     * @param room the room to switch to.
     * @param handler the event handler.
     */
    public void setOnRoomChange(final Room room, final EventHandler<ActionEvent> handler) {
        this.bottomBar.setOnRoomChange(room, handler);
    }

    /**
     * Sets the resume action handler.
     *
     * @param handler the event handler.
     */
    public void setOnResumeAction(final EventHandler<ActionEvent> handler) {
        this.pauseOverlay.setOnResume(handler);
    }

    /**
     * Sets the restart action handler.
     *
     * @param handler the event handler.
     */
    public void setOnRestartAction(final EventHandler<ActionEvent> handler) {
        this.gameOverOverlay.setOnRestart(handler);
    }

    /**
     * Sets the current room view.
     *
     * @param room the room view.
     */
    public void setRoom(final AbstractRoomView room) {
        this.centerContainer.setRoom(room);
    }

    /**
     * Shows or hides the pause overlay.
     *
     * @param visible true to show.
     */
    public void setPauseVisible(final boolean visible) {
        this.pauseOverlay.setVisible(visible);
    }

    /**
     * Shows or hides the game over overlay.
     *
     * @param visible true to show.
     */
    public void setGameOverVisible(final boolean visible) {
        this.gameOverOverlay.setVisible(visible);
    }

    /**
     * Shows or hides the character.
     *
     * @param visible true to show.
     */
    public void setCharacterVisible(final boolean visible) {
        this.centerContainer.setCharacterVisible(visible);
    }

    /**
     * Updates the character visuals for sleeping state.
     *
     * @param sleeping true if Pou is sleeping
     */
    public void setPouSleeping(final boolean sleeping) {
        this.centerContainer.setPouSleeping(sleeping);
    }
}
