package it.unibo.jpou.mvc.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PouGameLoopTest {

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
}
