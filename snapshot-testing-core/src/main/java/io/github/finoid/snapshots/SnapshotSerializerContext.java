package io.github.finoid.snapshots;

import io.github.finoid.snapshots.annotations.SnapshotName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Contains details of the pending snapshot that can be modified in the Serializer prior to calling
 * toSnapshot().
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SnapshotSerializerContext {

    private String name;
    private @Nullable String scenario;
    private SnapshotHeader header;
    private final Class<?> testClass;
    private final Method testMethod;
    @Singular
    private Map<String, Object> masks;

    public SnapshotSerializerContext(
        String name,
        @Nullable String scenario,
        SnapshotHeader header,
        Class<?> testClass,
        Method testMethod) {
        this(name, scenario, header, testClass, testMethod, java.util.Collections.emptyMap());
    }

    public static SnapshotSerializerContext from(SnapshotContext context) {
        SnapshotName snapshotNameAnnotation = context.getTestMethod().getAnnotation(SnapshotName.class);
        String name =
            snapshotNameAnnotation == null
                ? context.getTestClass().getName() + "." + context.getTestMethod().getName()
                : snapshotNameAnnotation.value();
        return new SnapshotSerializerContext(
            name,
            context.getScenario(),
            context.getHeader(),
            context.getTestClass(),
            context.getTestMethod(),
            context.getMasks());
    }

    public Snapshot toSnapshot(String body) {
        return Snapshot.builder()
            .name(name)
            .scenario(scenario)
            .header(header)
            .body(body)
            .build();
    }
}
