package it.unibo.jpou.mvc.view.character;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

/**
 * Manages the visual and behavioral logic of Pou's eyes,
 * including mouse tracking.
 */
public final class PouEyesComponent extends Group {

    private static final double EYE_OFFSET_X = 25.0;
    private static final double EYE_Y = -15.0;
    private static final double PUPIL_RADIUS = 6.0;
    private static final double EYE_RADIUS = 18.0;
    private static final double MAX_PUPIL_OFFSET = 6.0;

    private final Ellipse leftEye = new Ellipse(-EYE_OFFSET_X, EYE_Y, EYE_RADIUS, EYE_RADIUS);
    private final Ellipse rightEye = new Ellipse(EYE_OFFSET_X, EYE_Y, EYE_RADIUS, EYE_RADIUS);
    private final Circle leftPupil = new Circle(-EYE_OFFSET_X, EYE_Y, PUPIL_RADIUS);
    private final Circle rightPupil = new Circle(EYE_OFFSET_X, EYE_Y, PUPIL_RADIUS);

    /**
     * Initializes eyes and starts tracking.
     */
    public PouEyesComponent() {
        this.leftEye.getStyleClass().add("pou-eye-white");
        this.rightEye.getStyleClass().add("pou-eye-white");
        this.leftPupil.getStyleClass().add("pou-pupil");
        this.rightPupil.getStyleClass().add("pou-pupil");

        this.getChildren().addAll(leftEye, rightEye, leftPupil, rightPupil);
        this.setUpEyeTracking();
    }

    /**
     * Updates eyes appearance based on sleeping state.
     *
     * @param sleeping true if closed.
     */
    public void setSleeping(final boolean sleeping) {
        final double scale = sleeping ? 0.1 : 1.0;
        this.leftEye.setScaleY(scale);
        this.rightEye.setScaleY(scale);
        this.leftPupil.setVisible(!sleeping);
        this.rightPupil.setVisible(!sleeping);
    }

    private void setUpEyeTracking() {
        Platform.runLater(() -> {
            if (this.getScene() != null) {
                this.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, this::updateAllPupils);
            } else {
                this.sceneProperty().addListener((obs, oldV, newV) -> {
                    if (newV != null) {
                        newV.addEventFilter(MouseEvent.MOUSE_MOVED, this::updateAllPupils);
                    }
                });
            }
        });
    }

    private void updateAllPupils(final MouseEvent e) {
        updateSinglePupil(leftPupil, -EYE_OFFSET_X, e.getSceneX(), e.getSceneY());
        updateSinglePupil(rightPupil, EYE_OFFSET_X, e.getSceneX(), e.getSceneY());
    }

    private void updateSinglePupil(final Circle pupil, final double centerX,
                                   final double mouseX, final double mouseY) {
        final var eyeInScene = this.localToScene(centerX, EYE_Y);

        final double dx = mouseX - eyeInScene.getX();
        final double dy = mouseY - eyeInScene.getY();
        final double angle = Math.atan2(dy, dx);
        final double dist = Math.min(MAX_PUPIL_OFFSET, Math.sqrt(dx * dx + dy * dy));

        pupil.setCenterX(centerX + Math.cos(angle) * dist);
        pupil.setCenterY(EYE_Y + Math.sin(angle) * dist);
    }
}
