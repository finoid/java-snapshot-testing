package io.github.finoid.snapshots;

import io.github.finoid.snapshots.config.BaseSnapshotConfig;
import io.github.finoid.snapshots.config.SnapshotConfig;
import io.github.finoid.snapshots.exceptions.SnapshotMatchException;
import io.github.finoid.snapshots.serializers.UppercaseToStringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
@ExtendWith(MockitoExtension.class)
public class SnapshotIntegrationUnitTest {
    private static final SnapshotConfig DEFAULT_CONFIG = new BaseSnapshotConfig();
    static SnapshotVerifier snapshotVerifier;

    @BeforeAll
    static void beforeAll() {
        SnapshotUtils.copyTestSnapshots();
        snapshotVerifier = new SnapshotVerifier(DEFAULT_CONFIG, SnapshotIntegrationUnitTest.class);
    }

    @AfterAll
    static void afterAll() {
        snapshotVerifier.validateSnapshots();
    }

    @Test
    void givenObject1_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(FakeObject.builder().id("anyId1").value(1).name("anyName1").build());
    }

    @Test
    void givenObject2_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(FakeObject.builder().id("anyId2").value(2).name("anyName2").build());
    }

    @Test
    void givenObject3_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(FakeObject.builder().id("anyId3").value(3).name("anyName3").build());
    }

    @Test
    void givenObjectWithIllegalNewLines_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(
            FakeObject.builder().id("anyId4").value(4).name("any\n\n\nName4").build());
    }

    @Test
    void givenCallToPrivateMethod_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        matchInsidePrivate(testInfo);
    }

    @Test
    void givenMismatchedObject_whenMatchingSnapshot_thenSnapshotMatchExceptionIsThrown(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        assertThrows(
            SnapshotMatchException.class,
            () ->
                expect.toMatchSnapshot(
                    FakeObject.builder().id("anyId5").value(6).name("anyName5").build()),
            "Error on: \n"
                + "io.github.finoid.snapshots.SnapshotIntegrationUnitTest.givenMismatchedObject_whenMatchingSnapshot_thenSnapshotMatchExceptionIsThrown=[");
    }

    @Test
    void givenSerializerClass_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.serializer(UppercaseToStringSerializer.class).toMatchSnapshot("Hello World");
    }

    @Test
    void givenSerializerName_whenMatchingSnapshot_thenSnapshotMatches(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.serializer("lowercase").toMatchSnapshot("Hello World");
    }

    private void matchInsidePrivate(TestInfo testInfo) {
        Expect expect = Expect.of(snapshotVerifier, testInfo.getTestMethod().get());
        expect.toMatchSnapshot(
            FakeObject.builder().id("anyPrivate").value(5).name("anyPrivate").build());
    }
}
