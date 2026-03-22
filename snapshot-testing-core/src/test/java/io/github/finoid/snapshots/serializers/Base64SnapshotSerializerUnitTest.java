package io.github.finoid.snapshots.serializers;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotHeader;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Base64SnapshotSerializerUnitTest {

    @Test
    void givenByteArray_whenSerializing_thenReturnsBase64Snapshot() throws NoSuchMethodException {
        Base64SnapshotSerializer serializer = new Base64SnapshotSerializer();
        SnapshotSerializerContext context = snapshotSerializerContext("givenByteArray_whenSerializing_thenReturnsBase64Snapshot");

        Snapshot snapshot = serializer.apply("John Doe".getBytes(), context);

        assertThat(snapshot.getName()).isEqualTo(context.getName());
        assertThat(snapshot.getScenario()).isNull();
        assertThat(snapshot.getHeader()).isEqualTo(context.getHeader());
        assertThat(snapshot.getBody()).isEqualTo("[\nSm9obiBEb2U=\n]");
    }

    @Test
    void givenString_whenSerializing_thenReturnsBase64Snapshot() throws NoSuchMethodException {
        Base64SnapshotSerializer serializer = new Base64SnapshotSerializer();
        SnapshotSerializerContext context = snapshotSerializerContext("givenString_whenSerializing_thenReturnsBase64Snapshot");

        Snapshot snapshot = serializer.apply("John Doe", context);

        assertThat(snapshot.getName()).isEqualTo(context.getName());
        assertThat(snapshot.getScenario()).isNull();
        assertThat(snapshot.getHeader()).isEqualTo(context.getHeader());
        assertThat(snapshot.getBody()).isEqualTo("[\nSm9obiBEb2U=\n]");
    }

    @Test
    void givenSerializer_whenCheckingOutputFormat_thenIsBase64() {
        Base64SnapshotSerializer serializer = new Base64SnapshotSerializer();

        assertThat(serializer.getOutputFormat()).isEqualTo("BASE64");
    }

    private SnapshotSerializerContext snapshotSerializerContext(String name) throws NoSuchMethodException {
        return new SnapshotSerializerContext(
            name,
            null,
            new SnapshotHeader(),
            Base64SnapshotSerializerUnitTest.class,
            Base64SnapshotSerializerUnitTest.class.getDeclaredMethod(name)
        );
    }
}
