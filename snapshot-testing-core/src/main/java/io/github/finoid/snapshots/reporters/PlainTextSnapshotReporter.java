package io.github.finoid.snapshots.reporters;

import io.github.finoid.snapshots.Snapshot;
import org.assertj.core.util.diff.DiffUtils;
import org.assertj.core.util.diff.Patch;
import org.opentest4j.AssertionFailedError;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlainTextSnapshotReporter implements SnapshotReporter {
    private static final Supplier<IllegalStateException> NO_DIFF_EXCEPTION_SUPPLIER =
        () ->
            new IllegalStateException(
                "No differences found. Potential mismatch between comparator and reporter");

    public static String getDiffString(Patch<String> patch) {
        if (patch.getDeltas().isEmpty()) {
            throw NO_DIFF_EXCEPTION_SUPPLIER.get();
        }

        return patch.getDeltas().stream()
            .map(delta -> delta.toString() + "\n")
            .collect(Collectors.joining());
    }

    @Override
    public boolean supportsFormat(String outputFormat) {
        return true; // always true
    }

    @Override
    public void report(Snapshot previous, Snapshot current) {
        Patch<String> patch =
            DiffUtils.diff(
                Arrays.asList(previous.raw().split("\n")), Arrays.asList(current.raw().split("\n")));

        String message = "Error on: \n" + current.raw() + "\n\n" + getDiffString(patch);

        throw new AssertionFailedError(message, previous.raw(), current.raw());
    }
}
