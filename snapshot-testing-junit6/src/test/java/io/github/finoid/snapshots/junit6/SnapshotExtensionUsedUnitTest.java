package io.github.finoid.snapshots.junit6;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SnapshotExtension.class)
public class SnapshotExtensionUsedUnitTest {

    private Expect expect;

    @Test
    public void givenExpectInjected_whenMatchingSnapshot_thenSnapshotMatches(Expect expect) {
        expect.toMatchSnapshot("Hello World");
    }

    @Test
    public void givenExpectInjectedAgain_whenMatchingSnapshot_thenSnapshotMatches(Expect expect) {
        expect.toMatchSnapshot("Hello World");
    }

    @Test
    public void givenExpectInjectedInInstanceVariable_whenMatchingSnapshot_thenSnapshotMatches() {
        expect.toMatchSnapshot("Hello World");
    }

    @Test
    public void givenExpectInjectedInInstanceVariableAgain_whenMatchingSnapshot_thenSnapshotMatches() {
        expect.toMatchSnapshot("Hello World");
    }

    @SnapshotName("hello_world")
    @Test
    public void givenSnapshotNameAnnotation_whenMatchingSnapshot_thenSnapshotMatches() {
        expect.toMatchSnapshot("Hello World");
    }

    @SnapshotName("hello_world_2")
    @Test
    public void givenSnapshotNameAnnotationAgain_whenMatchingSnapshot_thenSnapshotMatches() {
        expect.toMatchSnapshot("Hello World");
    }
}
