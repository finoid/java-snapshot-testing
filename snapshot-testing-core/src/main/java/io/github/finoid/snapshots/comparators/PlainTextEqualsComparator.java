package io.github.finoid.snapshots.comparators;

import io.github.finoid.snapshots.Snapshot;

public class PlainTextEqualsComparator implements SnapshotComparator {
    @Override
    public boolean matches(final Snapshot previous, final Snapshot current) {
        return previous.getBody().equals(current.getBody());
    }
}
