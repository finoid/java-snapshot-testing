package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.exceptions.ReservedWordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnapshotNameAnnotationUnitTest {

    @BeforeEach
    void beforeEach() {
        SnapshotUtils.copyTestSnapshots();
    }

    @SnapshotName("can_use_snapshot_name")
    @Test
    void givenSnapshotNameAnnotation_whenMatchingSnapshot_thenSnapshotNameIsUsed(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("Hello World");
        snapshotVerifier.validateSnapshots();
    }

    @SnapshotName("can use snapshot name with spaces")
    @Test
    void givenSnapshotNameAnnotationWithSpaces_whenMatchingSnapshot_thenSnapshotNameIsUsed(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot("Hello World");
        snapshotVerifier.validateSnapshots();
    }

    @SnapshotName("can't use '=' character in snapshot name")
    @Test
    void givenSnapshotNameWithEquals_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @SnapshotName("can't use '[' character in snapshot name")
    @Test
    void givenSnapshotNameWithOpeningBracket_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @SnapshotName("can't use ']' character in snapshot name")
    @Test
    void givenSnapshotNameWithClosingBracket_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(ReservedWordException.class, () -> expect.toMatchSnapshot("FooBar"));
    }

    @Test
    void givenScenarioNameWithEquals_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use = symbol in scenario").toMatchSnapshot("FooBar"));
    }

    @Test
    void givenScenarioNameWithOpeningBracket_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use [ symbol in scenario").toMatchSnapshot("FooBar"));
    }

    @Test
    void givenScenarioNameWithClosingBracket_whenMatchingSnapshot_thenReservedWordExceptionIsThrown(TestInfo testInfo) {
        SnapshotVerifier snapshotVerifier =
            new SnapshotVerifier(new BaseSnapshotConfig(), testInfo.getTestClass().get());
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            ReservedWordException.class,
            () -> expect.scenario("can't use ] symbol in scenario").toMatchSnapshot("FooBar"));
    }
}
