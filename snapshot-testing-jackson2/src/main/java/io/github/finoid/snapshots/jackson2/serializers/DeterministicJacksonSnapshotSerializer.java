package io.github.finoid.snapshots.jackson2.serializers;

import io.github.finoid.snapshots.logging.LoggingHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * Attempts to deterministically render a snapshot.
 *
 * <p>This can help in situations where collections are rendering in a different order on subsequent
 * runs.
 *
 * <p>Note that collections will be ordered which mar or may not be desirable given your use case.
 */
@Deprecated
@Slf4j
public class DeterministicJacksonSnapshotSerializer
    extends io.github.finoid.snapshots.jackson2.serializers.v1.DeterministicJacksonSnapshotSerializer {
    public DeterministicJacksonSnapshotSerializer() {
        super();
        LoggingHelper.deprecatedV5(
            log,
            "Update to `v1.serializers.jackson2.io.github.finoid.snapshots.DeterministicJacksonSnapshotSerializer` in `snapshot.properties`");
    }
}
