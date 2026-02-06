package it.unibo.jpou.mvc.persistence;

import it.unibo.jpou.mvc.model.PouCoins;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.items.durable.skin.DefaultSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.GreenSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;
import it.unibo.jpou.mvc.model.save.PouSaveData;
import it.unibo.jpou.mvc.model.save.SavedInventory;
import it.unibo.jpou.mvc.model.save.SavedStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.nio.file.Files.exists;

class PersistenceManagerTest {

    private static final int CUSTOM_STATISTICS = 60;
    private static final int CUSTOM_WALLET = 10_000;

    @TempDir
    private Path tempDir;

    @Test
    void testLoadDefaultWhenFileMissing() {
        final Path doesNotExistFile = tempDir.resolve("j-pou-save.json");
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
        final Path saveFile = tempDir.resolve("j-pou-test-save.json");
        final PersistenceManager manager = new PersistenceManager(saveFile);

        final PouSaveData dataToSave = createCustomData();

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
                () -> assertEquals(PouState.SLEEPING.name(), loadedData.statistics().state()),
                () -> assertEquals(GreenSkin.SKIN_NAME, loadedData.inventory().equippedSkin()),
                () -> assertEquals(Room.BATHROOM.name(), loadedData.currentRoom())
                );
    }

    private PouSaveData createCustomData() {
        final SavedStatistics customStatistics = new SavedStatistics(
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_STATISTICS,
                CUSTOM_WALLET,
                PouState.SLEEPING.name()
        );

        final SavedInventory customInventory = new SavedInventory(
                Collections.emptyList(),
                List.of(DefaultSkin.DEFAULT_NAME, GreenSkin.SKIN_NAME, RedSkin.SKIN_NAME), GreenSkin.SKIN_NAME
        );

        return new PouSaveData(customStatistics, customInventory, Room.BATHROOM.name());
    }

    @Test
    void testDefaultSavePath() {
        final Path path = PersistenceManager.getDefaultSavePath();

        assertAll("Il file dev'essere caricato nel percorso di default",
                () -> assertNotNull(path, "Il percorso default non dev'essere null"),
                () -> assertTrue(path.toString().endsWith("j-pou-save.json"))
        );
    }

    @Test
    void testDeleteSaveFile() throws IOException {
        final Path saveFile = tempDir.resolve("delete-test.json");
        final PersistenceManager manager = new PersistenceManager(saveFile);

        manager.save(createCustomData());
        assertTrue(exists(saveFile), "Il file deve esistere prima di eliminarlo");

        manager.deleteSaveFile();
        assertFalse(exists(saveFile), "Il file è stato eliminato");
    }
}
