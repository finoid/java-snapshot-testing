package io.github.finoid.snapshots;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class SnapshotUnitTest {

    @Test
    public void givenRawSnapshot_whenParsing_thenSnapshotIsCorrect() {
        Snapshot snapshot =
            Snapshot.parse(
                Snapshot.builder().name("io.github.finoid.snapshots.Test").body("body").build().raw());
        assertThat(snapshot.getIdentifier()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getName()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getHeader()).isEmpty();
        assertThat(snapshot.getScenario()).isBlank();
        assertThat(snapshot.getBody()).isEqualTo("body");
    }

    @Test
    public void givenRawSnapshotWithHeaders_whenParsing_thenSnapshotIsCorrect() {
        SnapshotHeader header = new SnapshotHeader();
        header.put("header1", "value1");
        Snapshot snapshot =
            Snapshot.parse(
                Snapshot.builder()
                    .name("io.github.finoid.snapshots.Test")
                    .header(header)
                    .body("body")
                    .build()
                    .raw());
        assertThat(snapshot.getIdentifier()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getName()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getHeader()).containsExactly(entry("header1", "value1"));
        assertThat(snapshot.getScenario()).isBlank();
        assertThat(snapshot.getBody()).isEqualTo("body");
    }

    @Test
    public void givenRawSnapshotWithScenario_whenParsing_thenSnapshotIsCorrect() {
        Snapshot snapshot =
            Snapshot.parse(
                Snapshot.builder()
                    .name("io.github.finoid.snapshots.Test")
                    .scenario("scenario")
                    .body("body")
                    .build()
                    .raw());
        assertThat(snapshot.getIdentifier()).isEqualTo("io.github.finoid.snapshots.Test[scenario]");
        assertThat(snapshot.getName()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getHeader()).isEmpty();
        assertThat(snapshot.getScenario()).isEqualTo("scenario");
        assertThat(snapshot.getBody()).isEqualTo("body");
    }

    @Test
    public void givenRawSnapshotWithScenarioAndHeaders_whenParsing_thenSnapshotIsCorrect() {
        SnapshotHeader header = new SnapshotHeader();
        header.put("header1", "value1");
        Snapshot snapshot =
            Snapshot.parse(
                Snapshot.builder()
                    .name("io.github.finoid.snapshots.Test")
                    .scenario("scenario")
                    .header(header)
                    .body("body")
                    .build()
                    .raw());
        assertThat(snapshot.getIdentifier()).isEqualTo("io.github.finoid.snapshots.Test[scenario]");
        assertThat(snapshot.getName()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getHeader()).containsExactly(entry("header1", "value1"));
        assertThat(snapshot.getScenario()).isEqualTo("scenario");
        assertThat(snapshot.getBody()).isEqualTo("body");
    }

    @Test
    public void givenRawSnapshotWithConfusingBody_whenParsing_thenSnapshotIsCorrect() {
        Snapshot snapshot =
            Snapshot.parse(
                Snapshot.builder()
                    .name("io.github.finoid.snapshots.Test")
                    .scenario("scenario")
                    .body("[xxx]=yyy")
                    .build()
                    .raw());
        assertThat(snapshot.getIdentifier()).isEqualTo("io.github.finoid.snapshots.Test[scenario]");
        assertThat(snapshot.getName()).isEqualTo("io.github.finoid.snapshots.Test");
        assertThat(snapshot.getHeader()).isEmpty();
        assertThat(snapshot.getScenario()).isEqualTo("scenario");
        assertThat(snapshot.getBody()).isEqualTo("[xxx]=yyy");
    }
}
