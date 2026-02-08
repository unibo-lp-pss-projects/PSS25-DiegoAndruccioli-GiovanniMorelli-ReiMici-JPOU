package it.unibo.jpou.mvc.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PouGameLoopTest {

    private static final int FAST_LOOP_INTERVAL = 1;
    private static final int LONGER_INTERVAL_FAST_LOOP = 1500;

    private static final long TEST_INTERVAL_SEC = 4;
    private static final long WAIT_HALF_INTERVAL = 2000;
    private static final long WAIT_PAUSE = 2000;
    private static final long MAX_WAIT_RESUME = 4000;
    private static final long MAX_EXPECTED_DELAY = 3000;
    private static final long MIN_EXPECTED_DELAY = 1000;

    private PouGameLoop gameLoop;

    @BeforeEach
    void setUp() {
        this.gameLoop = new PouGameLoop();
    }

    @AfterEach
    void tearDown() {
        if (this.gameLoop.isRunning()) {
            this.gameLoop.shutdown();
        }
    }

    @Test
    void testInitialState() {
        assertFalse(this.gameLoop.isRunning(),
                "Il game loop non è attivo appena creato");
    }

    @Test
    void testStart() {
        this.gameLoop.start();
        assertTrue(this.gameLoop.isRunning(),
                "Dopo start() il gioco è in esecuzione");
    }

    @Test
    void testPause() {
        this.gameLoop.start();
        this.gameLoop.pause();
        assertFalse(this.gameLoop.isRunning(),
                "Dopo pause() il gioco si ferma temporaneamente");
    }

    @Test
    void testResume() {
        this.gameLoop.start();
        this.gameLoop.pause();
        this.gameLoop.start(); //for resume

        assertTrue(this.gameLoop.isRunning(),
                "Dopo start() il gioco deve tornare in esecuzione del punto in cui si trovare in pause()");
    }

    @Test
    void testShutdown() {
        this.gameLoop.start();
        this.gameLoop.shutdown();

        assertFalse(this.gameLoop.isRunning(),
                "Dopo shutdown() il gioco si chiude e il loop si ferma");
    }

    @Test
    void testNotification() throws InterruptedException {
        this.gameLoop = new PouGameLoop(FAST_LOOP_INTERVAL);
        final CountDownLatch latch = new CountDownLatch(1);

        this.gameLoop.addTickListener(latch::countDown);
        this.gameLoop.start();

        final boolean tickHappened = latch.await(LONGER_INTERVAL_FAST_LOOP, TimeUnit.MILLISECONDS);
        assertTrue(tickHappened, "Il listener dev'essere eseguito dopo il tick");
    }

    @Test
    void testPreciseResume() throws InterruptedException {
        this.gameLoop = new PouGameLoop(TEST_INTERVAL_SEC);

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicLong tickTime = new AtomicLong(0);

        this.gameLoop.addTickListener(() -> {
            tickTime.set(System.currentTimeMillis());
            latch.countDown();
        });

        this.gameLoop.start();
        Thread.sleep(WAIT_HALF_INTERVAL); // Wait half-time

        this.gameLoop.pause(); // Pause
        Thread.sleep(WAIT_PAUSE); // Wait in pause

        final long resumeTime = System.currentTimeMillis();
        this.gameLoop.start(); // Resume

        final boolean completed = latch.await(MAX_WAIT_RESUME, TimeUnit.MILLISECONDS);
        assertTrue(completed, "Il tick deve avvenire");

        final long timeSinceResume = tickTime.get() - resumeTime;

        assertTrue(timeSinceResume < MAX_EXPECTED_DELAY,
                "Resume troppo lento: timer resettato?");
        assertTrue(timeSinceResume > MIN_EXPECTED_DELAY,
                "Resume troppo presto");
    }
}
