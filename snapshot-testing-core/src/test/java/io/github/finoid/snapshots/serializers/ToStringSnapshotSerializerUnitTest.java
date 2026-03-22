package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotHeader;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringSnapshotSerializerUnitTest {
    ToStringSnapshotSerializer serializer = new ToStringSnapshotSerializer();

    private SnapshotSerializerContext mockSnapshotGenerator =
        SnapshotSerializerContext.builder()
            .name("toStringTest")
            .header(new SnapshotHeader())
            .testClass(ToStringSnapshotSerializerUnitTest.class)
            .build();


    @Test
    void givenString_whenSerializing_thenSnapshotMatches() {
        Snapshot result = serializer.apply("John Doe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn Doe\n]");
    }

    @Test
    void givenUnicodeString_whenSerializing_thenSnapshotMatches() {
        Snapshot result = serializer.apply("🤔", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n🤔\n]");
    }

    @Test
    void givenObject_whenSerializing_thenSnapshotMatches() {
        Snapshot result = serializer.apply(new Dummy(1, "John Doe"), mockSnapshotGenerator);
        assertThat(result.getBody())
            .isEqualTo("[\nToStringSerializerTest.Dummy(id=1, name=John Doe)\n]");
    }

    @Test
    void givenObject_whenSerializing_thenSnapshotMatchesDuplicate() {
        Snapshot result = serializer.apply(new Dummy(1, "John Doe"), mockSnapshotGenerator);
        assertThat(result.getBody())
            .isEqualTo("[\nToStringSerializerTest.Dummy(id=1, name=John Doe)\n]");
    }

    @Test
    void givenSerializer_whenCheckingOutputFormat_thenIsText() {
        assertThat(serializer.getOutputFormat()).isEqualTo("TEXT");
    }

    @Test
    void givenThreeNewLines_whenSerializing_thenNewLinesAreReplaced() {
        Snapshot result = serializer.apply("John\n\n\nDoe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn\n.\n.\nDoe\n]");
    }

    @Test
    void givenTwoNewLinesAtEnd_whenSerializing_thenNewLinesAreReplaced() {
        Snapshot result = serializer.apply("John Doe\n\n", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\nJohn Doe\n.\n.\n]");
    }

    @Test
    void givenTwoNewLinesAtBeginning_whenSerializing_thenNewLinesAreReplaced() {
        Snapshot result = serializer.apply("\n\nJohn Doe", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n.\n.\nJohn Doe\n]");
    }

    @Test
    void givenMultipleIllegalNewLines_whenSerializing_thenAllAreReplaced() {
        Snapshot result = serializer.apply("\n\nJohn\n\n\nDoe\n\n", mockSnapshotGenerator);
        assertThat(result.getBody()).isEqualTo("[\n.\n.\nJohn\n.\n.\nDoe\n.\n.\n]");
    }

    @AllArgsConstructor
    @Data
    private static class Dummy {
        private int id;
        private String name;

        public String toString() {
            return "ToStringSerializerTest.Dummy(id=" + this.getId() + ", name=" + this.getName() + ")";
        }
    }
}
