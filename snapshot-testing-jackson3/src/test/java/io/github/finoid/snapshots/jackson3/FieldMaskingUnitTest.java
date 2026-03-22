package io.github.finoid.snapshots.jackson3;

import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotHeader;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.annotations.Mask;
import io.github.finoid.snapshots.jackson3.serializers.v1.JacksonSnapshotSerializer;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldMaskingUnitTest {

    private final SnapshotSerializerContext gen =
        new SnapshotSerializerContext(
            "test", null, new SnapshotHeader(), FieldMaskingUnitTest.class, null);

    @Test
    public void givenAnnotatedField_whenSerializing_thenFieldIsMasked() {
        User user = new User("admin", "secret123");
        SnapshotSerializer serializer = new JacksonSnapshotSerializer();

        Snapshot result = serializer.apply(user, gen);

        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"password\":\"***REDACTED***\"");
        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"username\":\"admin\"");
    }

    @Test
    public void givenAnnotatedClass_whenSerializing_thenFieldsAreMasked() {
        SensitiveData data = new SensitiveData("1234-5678-9012-3456", "999");
        SnapshotSerializer serializer = new JacksonSnapshotSerializer();

        Snapshot result = serializer.apply(data, gen);

        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"creditCard\":\"***REDACTED***\"");
        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"cvv\":\"***REDACTED***\"");
    }

    @Test
    public void givenProgrammaticMask_whenSerializing_thenFieldIsMasked() {
        SimpleData data = new SimpleData("sensitive", "visible");
        SnapshotSerializer serializer = new JacksonSnapshotSerializer();
        SnapshotSerializerContext context = gen.toBuilder()
            .mask("$.sensitive", "MASKED")
            .build();

        Snapshot result = serializer.apply(data, context);

        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"sensitive\":\"MASKED\"");
        assertThat(result.getBody().replaceAll("\\s+", ""))
            .contains("\"visible\":\"visible\"");
    }

    public static class SimpleData {
        private String sensitive;
        private String visible;

        public SimpleData(String sensitive, String visible) {
            this.sensitive = sensitive;
            this.visible = visible;
        }

        public String getSensitive() { return sensitive; }
        public String getVisible() { return visible; }
    }

    public static class User {
        private String username;
        @Mask
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }

    @Mask("$.creditCard")
    @Mask("$.cvv")
    public static class SensitiveData {
        private String creditCard;
        private String cvv;

        public SensitiveData(String creditCard, String cvv) {
            this.creditCard = creditCard;
            this.cvv = cvv;
        }

        public String getCreditCard() { return creditCard; }
        public String getCvv() { return cvv; }
    }
}
