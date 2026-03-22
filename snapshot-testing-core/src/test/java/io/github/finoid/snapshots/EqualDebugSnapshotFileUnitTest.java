package io.github.finoid.snapshots;

import io.github.finoid.snapshots.config.ToStringSnapshotConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EqualDebugSnapshotFileUnitTest {

    private static final String SNAPSHOT_FILE_PATH =
        "src/test/java/io/github/finoid/snapshots/__snapshots__/EqualDebugSnapshotFileUnitTest.snap";
    private static final String DEBUG_FILE_PATH =
        "src/test/java/io/github/finoid/snapshots/__snapshots__/EqualDebugSnapshotFileUnitTest.snap.debug";

    @BeforeAll
    static void beforeAll() {
        SnapshotUtils.copyTestSnapshots();
    }

    @DisplayName("Should remove equal debug snapshots")
    @Test
    public void givenEqualDebugSnapshot_whenValidatingSnapshots_thenDebugFileIsRemoved(TestInfo testInfo) {
        assertTrue(Files.exists(Paths.get(SNAPSHOT_FILE_PATH)));
        assertTrue(Files.exists(Paths.get(DEBUG_FILE_PATH)));

        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new ToStringSnapshotConfig(), testInfo.getTestClass().get());
        snapshotVerifier.validateSnapshots();

        assertTrue(Files.exists(Paths.get(SNAPSHOT_FILE_PATH)));
        assertTrue(Files.notExists(Paths.get(DEBUG_FILE_PATH)));
    }
}
