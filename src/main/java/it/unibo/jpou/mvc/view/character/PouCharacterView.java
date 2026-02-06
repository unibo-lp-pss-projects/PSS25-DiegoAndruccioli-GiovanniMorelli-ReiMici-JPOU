package it.unibo.jpou.mvc.view.character;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.shape.Ellipse;

import java.util.Objects;

/**
 * Orchestrates body and eyes to represent the Pou character.
 */
public final class PouCharacterView extends Group {

    private static final double BODY_RX = 70.0;
    private static final double BODY_RY = 60.0;

    private static final double MIN_SCALE = 0.5;
    private static final double MAX_SCALE = 2.0;
    private static final double MAX_AGE_REF = 720.0;

    private final Ellipse body = new Ellipse(0, 0, BODY_RX, BODY_RY);
    private final PouEyesComponent eyes = new PouEyesComponent();

    /**
     * Constructs the character view.
     */
    public PouCharacterView() {
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/character/stylePouCharacter.css")).toExternalForm());

        this.body.getStyleClass().add("pou-body");
        this.getChildren().addAll(body, eyes);
    }

    /**
     * Binds the skin color to the model property.
     *
     * @param skinColorProperty hex color string property.
     */
    public void bindColor(final StringProperty skinColorProperty) {
        if (skinColorProperty != null) {
            this.body.styleProperty().bind(
                    Bindings.concat("-fx-fill: ", skinColorProperty, ";")
            );
        }
    }

    /**
     * Binds character size to age.
     *
     * @param ageProperty age in minutes/hours.
     */
    public void bindSize(final IntegerProperty ageProperty) {
        if (ageProperty != null) {
            ageProperty.addListener(
                    (obs, old, newAge) -> updateScale(newAge.intValue()));
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
     * Updates eyes state.
     *
     * @param sleeping true if eyes should be closed.
     */
    public void setSleeping(final boolean sleeping) {
        this.eyes.setSleeping(sleeping);
    }
}
