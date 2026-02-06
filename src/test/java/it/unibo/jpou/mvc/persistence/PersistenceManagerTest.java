package it.unibo.jpou.mvc.persistence;

import it.unibo.jpou.mvc.model.PouCoins;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.items.durable.skin.DefaultSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.GreenSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;
import it.unibo.jpou.mvc.model.save.PouSaveData;
import it.unibo.jpou.mvc.model.save.SavedInventory;
import it.unibo.jpou.mvc.model.save.SavedItem;
import it.unibo.jpou.mvc.model.save.SavedStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.nio.file.Files.exists;

class PersistenceManagerTest {

    private static final int CUSTOM_STATISTICS = 60;
    private static final int CUSTOM_WALLET = 10_000;
    private static final String CUSTOM_STATE = "SLEEPING";
    private static final String DEFAULT_SKIN = "Default";
    private static final String GREEN_SKIN = "Green Skin";
    private static final String RED_SKIN = "Red Skin";
    private static final String CUSTOM_ROOM = "BATHROOM";

    @TempDir
    Path tempDir;

    @Test
    void testLoadDefaultWhenFileMissing() {
        final Path doesNotExistFile = tempDir.resolve("jpou-save.json");
        final PersistenceManager manager = new PersistenceManager(doesNotExistFile);

        final PouSaveData data = manager.load();

        assertAll("I dati che vengono caricati non devono essere nulli",
                () -> assertNotNull(data),
                () -> assertNotNull(data.statistics(), "Le statistiche non devono essere nulle")
        );

        assertAll("Le statistiche hanno i valori di default",
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, data.statistics().hunger()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, data.statistics().energy()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, data.statistics().fun()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, data.statistics().health()),
                () -> assertEquals(PouCoins.MIN_COINS, data.statistics().coins())
                );
    }

    @Test
    void testSaveAndLoad() throws IOException {
        final Path saveFile = tempDir.resolve("jpou-test-save.json");
        final PersistenceManager manager = new PersistenceManager(saveFile);

        final SavedStatistics customStatistics = new SavedStatistics(
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_WALLET,
                CUSTOM_STATE
        );

        final SavedInventory customInventory = new SavedInventory(
                Collections.emptyList(),
                List.of(DEFAULT_SKIN, GREEN_SKIN, RED_SKIN), GREEN_SKIN
        );

        final PouSaveData dataToSave = new PouSaveData(customStatistics, customInventory, CUSTOM_ROOM);

        manager.save(dataToSave);

        assertTrue(exists(saveFile),
                "Il file dovrebbe essere stato creato correttamente");

        final PersistenceManager newManager = new PersistenceManager(saveFile);
        final PouSaveData loadedData = newManager.load();

        assertAll("I dati sono quelli che ci aspettiamo al ricarico della partita",
                () -> assertEquals(CUSTOM_STATISTICS, loadedData.statistics().hunger()),
                () -> assertEquals(CUSTOM_STATISTICS, loadedData.statistics().energy()),
                () -> assertEquals(CUSTOM_STATISTICS, loadedData.statistics().fun()),
                () -> assertEquals(CUSTOM_STATISTICS, loadedData.statistics().health()),
                () -> assertEquals(CUSTOM_WALLET, loadedData.statistics().coins()),
                () -> assertEquals(CUSTOM_STATE, loadedData.statistics().state()),
                () -> assertEquals(GREEN_SKIN, loadedData.inventory().equippedSkin()),
                () -> assertEquals(CUSTOM_ROOM, loadedData.currentRoom())
                );
    }
}
