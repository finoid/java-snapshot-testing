package io.github.finoid.snapshots;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SnapshotFile {
    public static final String SPLIT_STRING = "\n\n\n";

    private final String fileName;
    @Getter
    private final Set<Snapshot> snapshots = Collections.synchronizedSortedSet(new TreeSet<>());
    private final Set<Snapshot> debugSnapshots = Collections.synchronizedSortedSet(new TreeSet<>());

    public SnapshotFile(String srcDirPath, String fileName, Class<?> testClass) throws IOException {
        this.fileName = srcDirPath + File.separator + fileName;
        log.info("Snapshot File: {}", this.fileName);

        final Path path = Paths.get(this.fileName);
        if (Files.exists(path)) {
            String fileText = Files.readString(path, StandardCharsets.UTF_8);
            if (!fileText.isBlank()) {
                snapshots.addAll(
                    Stream.of(fileText.split(SPLIT_STRING))
                        .map(String::trim)
                        .map(Snapshot::parse)
                        .collect(Collectors.toCollection(TreeSet::new)));
            }
        }

        deleteDebugFile();
    }

    private String getDebugFilename() {
        return this.fileName + ".debug";
    }

    public File createDebugFile(Snapshot snapshot) {
        Path debugPath = Paths.get(getDebugFilename());
        try {
            Files.createDirectories(debugPath.getParent());
            Files.writeString(debugPath, snapshot.raw(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create debug file", e);
        }
        return debugPath.toFile();
    }

    public void deleteDebugFile() {
        try {
            Files.deleteIfExists(Paths.get(getDebugFilename()));
        } catch (IOException e) {
            log.warn("Unable to delete debug file: {}", getDebugFilename(), e);
        }
    }

    public void delete() {
        try {
            Files.deleteIfExists(Paths.get(this.fileName));
        } catch (IOException e) {
            log.warn("Unable to delete snapshot file: {}", this.fileName, e);
        }
    }

    private void updateFile(String fileName, Set<Snapshot> snapshotsToUpdate) {
        Path path = Paths.get(fileName);
        try {
            Files.createDirectories(path.getParent());
            String content;
            synchronized (snapshotsToUpdate) {
                content = snapshotsToUpdate.stream()
                    .map(Snapshot::raw)
                    .collect(Collectors.joining(SPLIT_STRING));
            }
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + fileName, e);
        }
    }

    public void pushSnapshot(Snapshot snapshot) {
        synchronized (snapshots) {
            snapshots.add(snapshot);
            updateFile(this.fileName, snapshots);
        }
    }

    public void pushDebugSnapshot(Snapshot snapshot) {
        synchronized (debugSnapshots) {
            debugSnapshots.add(snapshot);
            updateFile(getDebugFilename(), debugSnapshots);
        }
    }

    @SneakyThrows
    public void cleanup() {
        Path path = Paths.get(this.fileName);
        if (Files.exists(path)) {
            if (Files.size(path) == 0 || snapshotsAreTheSame()) {
                deleteDebugFile();
            }

            if (Files.size(path) == 0) {
                delete();
            } else {
                String content = Files.readString(path, StandardCharsets.UTF_8);
                Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    private boolean snapshotsAreTheSame() throws IOException {
        Path snapshotPath = Paths.get(this.fileName);
        Path debugPath = Paths.get(this.getDebugFilename());
        if (Files.exists(debugPath)) {
            return Objects.equals(
                Files.readString(snapshotPath, StandardCharsets.UTF_8),
                Files.readString(debugPath, StandardCharsets.UTF_8)
            );
        }
        return false;
    }
}
