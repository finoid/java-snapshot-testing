package io.github.finoid.snapshots.junit6.docs;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.annotations.SnapshotName;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith({SnapshotExtension.class})
public class MyFirstSnapshotUnitTest {

    private Expect expect;

    @SnapshotName("i_can_give_custom_names_to_my_snapshots")
    @Test
    public void givenString_whenMatchingSnapshotWithCustomName_thenSnapshotMatches() {
        expect.toMatchSnapshot("Hello World");
    }

    @Test
    public void givenMap_whenMatchingSnapshotWithJsonSerializer_thenSnapshotMatches() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "John Doe");
        map.put("age", 40);

        expect.serializer("json").toMatchSnapshot(map);
    }
}
