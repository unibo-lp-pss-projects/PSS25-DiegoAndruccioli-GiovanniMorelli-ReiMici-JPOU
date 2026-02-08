package it.unibo.jpou.mvc.controller.gameloop;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of GameLoop specific for Pou logic.
 */
public final class PouGameLoop implements GameLoop {

    private static final long TICK_INTERVAL_SECONDS = 30;
    private static final long TIME_OUT = 5;

    private final long tickIntervalMillis;
    private long remainingTimeMillis;
    private long lastStartTimeMillis;

    private final List<Runnable> tickListeners = new CopyOnWriteArrayList<>();

    private ScheduledExecutorService scheduler;

    private ScheduledFuture<?> currentTask;

    private boolean running;

    /**
     * Create a new game engine, initially stopped.
     */
    public PouGameLoop() {
        this(TICK_INTERVAL_SECONDS);
    }

    /**
     * @param intervalSeconds is the interval in seconds between each tick
     */
    public PouGameLoop(final long intervalSeconds) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.running = false;
        this.tickIntervalMillis = TimeUnit.SECONDS.toMillis(intervalSeconds);
        this.remainingTimeMillis = this.tickIntervalMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (!this.running) {
            if (this.scheduler.isShutdown()) {
                this.scheduler = Executors.newSingleThreadScheduledExecutor();
            }

            this.running = true;
            this.lastStartTimeMillis = System.currentTimeMillis();

            scheduleTick(this.remainingTimeMillis);
        }
    }

    void scheduleTick(final long delayMillis) {
        this.currentTask = this.scheduler.schedule(
                () -> {
                    tick();
                    this.remainingTimeMillis = this.tickIntervalMillis;
                    this.lastStartTimeMillis = System.currentTimeMillis();
                    scheduleTick(this.tickIntervalMillis);
                },
                delayMillis,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        this.pause();
        this.scheduler.shutdown();

        try {
            if (!this.scheduler.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                this.scheduler.shutdownNow();
            }
        } catch (final InterruptedException e) {
            this.scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Pauses the game engine execution.
     */
    public void pause() {
        if (this.running) {
            this.running = false;

            final long now = System.currentTimeMillis();
            final long spent = now - this.lastStartTimeMillis;

            this.remainingTimeMillis = Math.max(0, this.remainingTimeMillis - spent);

            if (this.currentTask != null) {
                this.currentTask.cancel(false);
            }
        }
    }

    /**
     * Adds a listener that will be executed at every tick.
     *
     * @param listener the action to run
     */
    public void addTickListener(final Runnable listener) {
        this.tickListeners.add(listener);
    }

    private void tick() {
        for (final Runnable listener : this.tickListeners) {
            listener.run();
        }
    }
}
