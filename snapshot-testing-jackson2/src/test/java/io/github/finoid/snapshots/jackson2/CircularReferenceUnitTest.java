package io.github.finoid.snapshots.jackson2;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotHeader;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.jackson2.serializers.JacksonSnapshotSerializer;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CircularReferenceUnitTest {

    private final SnapshotSerializerContext gen =
        new SnapshotSerializerContext(
            "test", null, new SnapshotHeader(), CircularReferenceUnitTest.class, null);

    @Test
    public void givenCircularReference_whenSerializing_thenCircularRefMarkerIsPresent() {
        Circular a = new Circular("a");
        Circular b = new Circular("b");
        a.setChild(b);
        b.setChild(a);

        SnapshotSerializer serializer = new JacksonSnapshotSerializer();

        // This is expected to fail or hang before the fix
        Snapshot result = serializer.apply(a, gen);

        assertThat(result.getBody()).contains("[CIRCULAR_REF]");
    }

    public static class Circular {
        private String id;
        private Circular child;

        public Circular(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setChild(Circular child) {
            this.child = child;
        }

        public Circular getChild() {
            return child;
        }
    }
}
