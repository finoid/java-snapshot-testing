package io.github.finoid.snapshots.junit5;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.jackson2.serializers.v1.JacksonSnapshotSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class CircularReferenceIntegrationUnitTest {

    @Test
    public void givenCircularReference_whenMatchingSnapshot_thenSnapshotMatches(Expect expect) {
        Circular a = new Circular("a");
        Circular b = new Circular("b");
        a.setChild(b);
        b.setChild(a);

        expect
            .serializer(JacksonSnapshotSerializer.class)
            .toMatchSnapshot(a);
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
