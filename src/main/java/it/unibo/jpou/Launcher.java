package it.unibo.jpou;

/**
 * Application entry point workaround.
 * This class is necessary to launch JavaFX applications correctly from a JAR file
 * or certain IDE configurations, bypassing the strict module-path checks on startup.
 */
public final class Launcher {

    private Launcher() {

    }

    /**
     * Main entry point.
     * Delegates the execution immediately to the {@link App} class.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        App.main(args);
    }
}
