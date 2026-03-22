package io.github.finoid.snapshots.junit6;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
@ExtendWith({SnapshotExtension.class})
@Disabled // TODO (nw) rewrite
public class NestedClassUnitTest {

    @AfterAll
    public static void afterAll() {
        Path path =
            Paths.get("src/test/java/io/github/finoid/snapshots/junit6/__snapshots__/NestedClassUnitTest.snap");
        assertThat(Files.exists(path)).isFalse();
    }

    @Nested
    class NestedClassWithExpectArgument {

        @Test
        public void givenExpectArgument_whenMatchingSnapshot_thenSnapshotMatches(Expect expect) {
            expect.toMatchSnapshot("Hello World");
        }
    }

    @Nested
    class NestedClassWithoutSnapshot {

        @Test
        public void givenNoSnapshot_whenAssertingTrue_thenPasses() {
            assertThat(true).isTrue();
        }
    }

    @Nested
    class NestedClassWithExpectInstance {

        Expect expect;

        @Test
        public void givenExpectInstance_whenMatchingSnapshot_thenSnapshotMatches() {
            expect.toMatchSnapshot("Hello World");
        }
    }
}
