package io.github.finoid.snapshots;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SnapshotHeader extends HashMap<String, String> {

    private static final Pattern JSON_PATTERN = Pattern.compile("\\\"(?<key>.*)\\\": \\\"(?<value>.*)\\\"");

    public SnapshotHeader() {
        super();
    }

    public SnapshotHeader(Map<String, String> m) {
        super(m);
    }

    public static SnapshotHeader fromJson(@Nullable String json) {
        SnapshotHeader snapshotHeader = new SnapshotHeader();

        if (json == null || json.isBlank()) {
            return snapshotHeader;
        }

        Matcher m = JSON_PATTERN.matcher(json);
        while (m.find()) {
            String key = m.group("key");
            String value = m.group("value");
            if (key != null && value != null) {
                snapshotHeader.put(key, value);
            }
        }
        return snapshotHeader;
    }

    /**
     * Manual JSON serialization as we want to avoid extra dependencies.
     *
     * @return JSON representation of the header
     */
    public String toJson() {
        if (isEmpty()) {
            return "{}";
        }

        return entrySet().stream()
            .map(entry -> String.format("  \"%s\": \"%s\"", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining(",\n", "{\n", "\n}"));
    }
}
