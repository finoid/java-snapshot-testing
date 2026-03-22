package io.github.finoid.snapshots.junit5;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.jackson2.serializers.v1.JacksonSnapshotSerializer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@ExtendWith({SnapshotExtension.class})
class SnapshotParameterUnitTest {

    private Expect expect;

    static Stream<Arguments> testData() {

        return Stream.of(
            Arguments.of("Scenario2", "test input 1"),
            Arguments.of("Scenario2", "test input 1"),
            Arguments.of("Scenario2", "test input 1"),
            Arguments.of("Scenario3", "test input 2"),
            Arguments.of("Scenario3", "test input 2"));
    }

    @ParameterizedTest
    @MethodSource("io.github.finoid.snapshots.junit5.SnapshotParameterUnitTest#testData")
    void givenParameterizedTestDataAndExpectParameter_whenMatchingSnapshots_thenAllMatch(String scenario, String testInput, Expect expect) {
        expect.toMatchSnapshot("Duplicates are OK");
        expect.toMatchSnapshot("Duplicates are OK");
        expect.scenario("Scenario1").toMatchSnapshot("Additional snapshots need to include a scenario");
        expect
            .serializer(JacksonSnapshotSerializer.class)
            .scenario(scenario)
            .toMatchSnapshot(testInput);
    }

    @ParameterizedTest
    @MethodSource("io.github.finoid.snapshots.junit5.SnapshotParameterUnitTest#testData")
    void givenParameterizedTestDataAndExpectInstanceVariable_whenMatchingSnapshots_thenAllMatch(String scenario, String testInput) {
        this.expect.toMatchSnapshot("Duplicates are OK");
        this.expect.toMatchSnapshot("Duplicates are OK");
        this.expect
            .scenario("Scenario1")
            .toMatchSnapshot("Additional snapshots need to include a scenario");
        this.expect
            .serializer(JacksonSnapshotSerializer.class)
            .scenario(scenario)
            .toMatchSnapshot(testInput);
    }
}
