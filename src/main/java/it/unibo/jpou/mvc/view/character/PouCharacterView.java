package it.unibo.jpou.mvc.view.character;

import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.shape.Ellipse;
import java.util.Objects;

/**
 * View representing the character's visual appearance.
 */
public final class PouCharacterView extends Group {

    private static final double BODY_RX = 70.0;
    private static final double BODY_RY = 60.0;

    private static final double MIN_SCALE = 0.5;
    private static final double MAX_SCALE = 2.0;
    private static final double MAX_AGE_REF = 720.0;

    private final Ellipse bodyShape;
    private final PouEyesComponent eyes;

    /**
     * Initializes the character view composed of Body and Eyes.
     */
    public PouCharacterView() {
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/character/stylePouCharacter.css")).toExternalForm());
        this.getStyleClass().add("pou-character-view");

        this.bodyShape = new Ellipse(0, 0, BODY_RX, BODY_RY);
        this.bodyShape.getStyleClass().add("pou-body");

        this.eyes = new PouEyesComponent();

        this.getChildren().addAll(this.bodyShape, this.eyes);
    }

    /**
     * Updates the character's skin color using inline CSS.
     *
     * @param hexColor the hexadecimal color code.
     */
    public void updateSkinColor(final String hexColor) {
        if (hexColor != null && !hexColor.isEmpty()) {
            this.bodyShape.setStyle("-fx-fill: " + hexColor + ";");
        }
    }

    /**
     * Delegates the sleeping state to the eyes component.
     *
     * @param sleeping true to close eyes.
     */
    public void setSleeping(final boolean sleeping) {
        this.eyes.setSleeping(sleeping);
    }

    /**
     * Binds the character scale to the age property.
     *
     * @param ageProperty the logic age.
     */
    public void bindSize(final IntegerProperty ageProperty) {
        if (ageProperty != null) {
            ageProperty.addListener((_, _, newAge) -> updateScale(newAge.intValue()));
            updateScale(ageProperty.get());
        }
    }

    private void updateScale(final int age) {
        final double factor = Math.min(age, MAX_AGE_REF) / MAX_AGE_REF;
        final double scale = MIN_SCALE + (factor * (MAX_SCALE - MIN_SCALE));

        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    /**
     * @return the eyes component access.
     */
    public PouEyesComponent getEyes() {
        return this.eyes;
    }

    /**
     * Disables the age-based scaling and sets a fixed scale for the character.
     * Useful for minigames where the size must be constant.
     *
     * @param scale the fixed scale value (e.g., 0.5 for half size)
     */
    public void setFixedScale(final double scale) {
        this.scaleXProperty().unbind();
        this.scaleYProperty().unbind();

        this.setScaleX(scale);
        this.setScaleY(scale);
    }
}
