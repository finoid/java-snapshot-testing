package io.github.finoid.snapshots;

import io.github.finoid.snapshots.exceptions.MissingSnapshotPropertiesKeyException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public enum SnapshotProperties {
    INSTANCE;

    private final Properties properties = new Properties();

    SnapshotProperties() {
        try (final InputStream in = SnapshotProperties.class.getClassLoader().getResourceAsStream("snapshot.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (Exception e) {
            // It's ok, if the SnapshotConfig implementation attempts to get a property they will receive
            // a MissingSnapshotPropertiesKeyException
        }
    }

    public static String getOrThrow(final String key) {
        final String value = INSTANCE.properties.getProperty(key);
        if (value == null) {
            throw new MissingSnapshotPropertiesKeyException(key);
        }
        return value;
    }

    public static <T> T getInstance(final String key) {
        final String className = getOrThrow(key);
        return createInstance(className);
    }

    public static <T> List<T> getInstances(final String key) {
        final String values = getOrThrow(key);

        return Arrays.stream(values.split(","))
            .map(String::trim)
            .map(it -> SnapshotProperties.<T>createInstance(it))
            .toList();
    }

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate class " + className, e);
        }
    }

    @Nullable
    public static String get(final String key) {
        return INSTANCE.properties.getProperty(key);
    }
}
