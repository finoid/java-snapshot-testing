package io.github.finoid.snapshots;

import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.config.SnapshotConfig;
import io.github.finoid.snapshots.exceptions.SnapshotMatchException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UpdateSnapshotUnitTest {

    @BeforeEach
    public void beforeEach() throws Exception {
        File file =
            new File("src/test/java/io/github/finoid/snapshots/__snapshots__/UpdateSnapshotUnitTest.snap");
        String content =
            "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateAllConfig_whenMatchingSnapshot_thenAllSnapshotsAreUpdated=[\n"
                + "OLD\n"
                + "]\n"
                + "\n"
                + "\n"
                + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateClassNameConfig_whenMatchingSnapshot_thenSnapshotsForClassAreUpdated=[\n"
                + "OLD\n"
                + "]\n"
                + "\n"
                + "\n"
                + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateNoneConfig_whenMatchingSnapshot_thenSnapshotsAreNotUpdated=[\n"
                + "OLD\n"
                + "]";
        Path parentDir = file.getParentFile().toPath();
        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void givenUpdateAllConfig_whenMatchingSnapshot_thenAllSnapshotsAreUpdated(TestInfo testInfo) throws IOException {
        SnapshotConfig config =
            new BaseSnapshotConfig() {
                @Override
                public Optional<String> updateSnapshot() {
                    return Optional.of("");
                }
            };
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(config, testInfo.getTestClass().get(), false);
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("NEW");
        snapshotVerifier.validateSnapshots();

        String content =
            new String(
                Files.readAllBytes(
                    Paths.get(
                        "src/test/java/io/github/finoid/snapshots/__snapshots__/UpdateSnapshotUnitTest.snap")),
                StandardCharsets.UTF_8);
        Assertions.assertThat(content)
            .isEqualTo(
                "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateAllConfig_whenMatchingSnapshot_thenAllSnapshotsAreUpdated=[\n"
                    + "NEW\n"
                    + "]\n"
                    + "\n"
                    + "\n"
                    + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateClassNameConfig_whenMatchingSnapshot_thenSnapshotsForClassAreUpdated=[\n"
                    + "OLD\n"
                    + "]\n"
                    + "\n"
                    + "\n"
                    + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateNoneConfig_whenMatchingSnapshot_thenSnapshotsAreNotUpdated=[\n"
                    + "OLD\n"
                    + "]");
    }

    @Test
    void givenUpdateNoneConfig_whenMatchingSnapshot_thenSnapshotsAreNotUpdated(TestInfo testInfo) {
        SnapshotConfig config =
            new BaseSnapshotConfig() {
                @Override
                public Optional<String> updateSnapshot() {
                    return Optional.empty();
                }
            };
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(config, testInfo.getTestClass().get(), false);
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(SnapshotMatchException.class, () -> expect.toMatchSnapshot("FOOBAR"));
    }

    @Test
    public void givenUpdateNewConfig_whenMatchingSnapshot_thenNewSnapshotsAreUpdated() {
        SnapshotConfig config =
            new BaseSnapshotConfig() {
                @Override
                public Optional<String> updateSnapshot() {
                    return Optional.of("new");
                }
            };

        // TODO Pending Implementation
    }

    @Test
    public void givenUpdateClassNameConfig_whenMatchingSnapshot_thenSnapshotsForClassAreUpdated(TestInfo testInfo) throws IOException {
        SnapshotConfig config =
            new BaseSnapshotConfig() {
                @Override
                public Optional<String> updateSnapshot() {
                    return Optional.of("UpdateSnapshotUnitTest");
                }
            };
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(config, testInfo.getTestClass().get(), false);
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("NEW");
        snapshotVerifier.validateSnapshots();

        String content =
            new String(
                Files.readAllBytes(
                    Paths.get(
                        "src/test/java/io/github/finoid/snapshots/__snapshots__/UpdateSnapshotUnitTest.snap")),
                StandardCharsets.UTF_8);
        Assertions.assertThat(content)
            .isEqualTo(
                "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateAllConfig_whenMatchingSnapshot_thenAllSnapshotsAreUpdated=[\n"
                    + "OLD\n"
                    + "]\n"
                    + "\n"
                    + "\n"
                    + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateClassNameConfig_whenMatchingSnapshot_thenSnapshotsForClassAreUpdated=[\n"
                    + "NEW\n"
                    + "]\n"
                    + "\n"
                    + "\n"
                    + "io.github.finoid.snapshots.UpdateSnapshotUnitTest.givenUpdateNoneConfig_whenMatchingSnapshot_thenSnapshotsAreNotUpdated=[\n"
                    + "OLD\n"
                    + "]");
    }
}
