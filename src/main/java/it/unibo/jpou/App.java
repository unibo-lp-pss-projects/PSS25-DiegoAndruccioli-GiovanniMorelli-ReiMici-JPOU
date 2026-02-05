package it.unibo.jpou;

import it.unibo.jpou.mvc.controller.MainController;
import it.unibo.jpou.mvc.controller.MainControllerImpl;
import it.unibo.jpou.mvc.view.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application entry-point.
 * Responsible for setting up the JavaFX Stage and loading the MVC architecture.
 */
public final class App extends Application {

    private static final double APP_WIDTH = 800;
    private static final double APP_HEIGHT = 600;

    private MainController controller;

    /**
     * Entry point method.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        final MainView mainView = new MainView();

        this.controller = new MainControllerImpl(mainView);
        this.controller.start();

        final Scene scene = new Scene(mainView, APP_WIDTH, APP_HEIGHT);
        stage.setTitle("J-Pou");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(_ -> {
            if (this.controller != null) {
                this.controller.stop();
            }
            Platform.exit();
        });
        stage.show();
    }
}
