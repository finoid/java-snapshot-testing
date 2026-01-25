package io.github.finoid.snapshots.jackson2.serializers;

import io.github.finoid.snapshots.logging.LoggingHelper;
import lombok.extern.slf4j.Slf4j;

@Deprecated
@Slf4j
public class JacksonSnapshotSerializer
    extends io.github.finoid.snapshots.jackson2.serializers.v1.JacksonSnapshotSerializer {

    public JacksonSnapshotSerializer() {
        super();
        LoggingHelper.deprecatedV5(
            log,
            "Update to `v1.serializers.jackson2.io.github.finoid.snapshots.JacksonSnapshotSerializer` in `snapshot.properties`");
    }
}
