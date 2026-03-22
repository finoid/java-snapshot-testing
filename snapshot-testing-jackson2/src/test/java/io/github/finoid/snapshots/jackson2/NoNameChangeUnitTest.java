package io.github.finoid.snapshots.jackson2;

import io.github.finoid.snapshots.jackson2.serializers.DeterministicJacksonSnapshotSerializer;
import io.github.finoid.snapshots.jackson2.serializers.JacksonSnapshotSerializer;
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
        assertThat(JacksonSnapshotSerializer.class.getName())
            .isEqualTo("io.github.finoid.snapshots.jackson2.serializers.JacksonSnapshotSerializer");
        assertThat(DeterministicJacksonSnapshotSerializer.class.getName())
            .isEqualTo(
                "io.github.finoid.snapshots.jackson2.serializers.DeterministicJacksonSnapshotSerializer");
    }
}
