package io.github.finoid.snapshots;

import io.github.finoid.snapshots.comparators.PlainTextEqualsComparator;
import io.github.finoid.snapshots.reporters.PlainTextSnapshotReporter;
import io.github.finoid.snapshots.serializers.Base64SnapshotSerializer;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.ToStringSnapshotSerializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These classes are likely defined in snapshot.properties as a string.
 *
 * <p>The clients IDE will not complain if they change so ensure they don't
 */
public class NoNameChangeUnitTest {

    @Test
    public void givenSerializers_whenCheckingClassName_thenNamesAreStable() {
        assertThat(Base64SnapshotSerializer.class.getName())
            .isEqualTo("io.github.finoid.snapshots.serializers.Base64SnapshotSerializer");
        assertThat(ToStringSnapshotSerializer.class.getName())
            .isEqualTo("io.github.finoid.snapshots.serializers.ToStringSnapshotSerializer");
        assertThat(SerializerType.class.getName())
            .isEqualTo("io.github.finoid.snapshots.serializers.SerializerType");
    }

    @Test
    public void givenReporters_whenCheckingClassName_thenNamesAreStable() {
        assertThat(PlainTextSnapshotReporter.class.getName())
            .isEqualTo("io.github.finoid.snapshots.reporters.PlainTextSnapshotReporter");
    }

    @Test
    public void givenComparators_whenCheckingClassName_thenNamesAreStable() {
        assertThat(PlainTextEqualsComparator.class.getName())
            .isEqualTo("io.github.finoid.snapshots.comparators.PlainTextEqualsComparator");
    }
}
