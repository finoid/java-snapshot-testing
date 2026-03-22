package io.github.finoid.snapshots.comparators;

import io.github.finoid.snapshots.Snapshot;

@FunctionalInterface
public interface SnapshotComparator {
    boolean matches(final Snapshot previous, final Snapshot current);
}
