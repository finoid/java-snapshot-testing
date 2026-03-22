package io.github.finoid.snapshots;

import io.github.finoid.snapshots.exceptions.LogGithubIssueException;
import lombok.Builder;
import lombok.Value;
import org.jspecify.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
@Builder(toBuilder = true)
public class Snapshot implements Comparable<Snapshot> {
    private static final Pattern SNAPSHOT_PATTERN = Pattern.compile(
        "^(?<name>.*?)(\\[(?<scenario>[^]]*)])?=(?<header>\\{[^}]*?})?(?<snapshot>(.*)$)",
        Pattern.DOTALL
    );

    String name;
    @Nullable String scenario;
    SnapshotHeader header;
    String body;

    public static Snapshot parse(String rawText) {
        Matcher m = SNAPSHOT_PATTERN.matcher(rawText);
        if (!m.find()) {
            throw new LogGithubIssueException(
                "Corrupt Snapshot (REGEX matches = 0): possibly due to manual editing or our REGEX failing\n"
                    + "Possible Solutions\n"
                    + "1. Ensure you have not accidentally manually edited the snapshot file!\n"
                    + "2. Compare the snapshot with GIT history");
        }

        String name = m.group("name");
        String scenario = m.group("scenario");
        String header = m.group("header");
        String snapshot = m.group("snapshot");

        if (name == null || snapshot == null) {
            throw new LogGithubIssueException(
                "Corrupt Snapshot (REGEX name or snapshot group missing): possibly due to manual editing or our REGEX failing\n"
                    + "Possible Solutions\n"
                    + "1. Ensure you have not accidentally manually edited the snapshot file\n"
                    + "2. Compare the snapshot with your version control history");
        }

        return Snapshot.builder()
            .name(name)
            .scenario(scenario)
            .header(SnapshotHeader.fromJson(header))
            .body(snapshot)
            .build();
    }

    @Override
    public int compareTo(Snapshot other) {
        return (name + (scenario == null ? "" : scenario))
            .compareTo(other.name + (other.scenario == null ? "" : other.scenario));
    }

    public String getIdentifier() {
        return scenario == null ? name : String.format("%s[%s]", name, scenario);
    }

    /**
     * The raw string representation of the snapshot as it would appear in the *.snap file.
     *
     * @return raw snapshot
     */
    public String raw() {
        String headerJson = (header == null) || header.isEmpty() ? "" : header.toJson();
        return getIdentifier() + "=" + headerJson + body;
    }

    @Override
    public String toString() {
        return raw();
    }
}
