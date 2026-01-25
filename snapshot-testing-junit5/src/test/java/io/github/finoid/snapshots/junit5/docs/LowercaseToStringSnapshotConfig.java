package io.github.finoid.snapshots.junit5.docs;

import io.github.finoid.snapshots.config.PropertyResolvingSnapshotConfig;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;

public class LowercaseToStringSnapshotConfig extends PropertyResolvingSnapshotConfig {

    @Override
    public SnapshotSerializer getSerializer() {
        return new LowercaseToStringSerializer();
    }
}
