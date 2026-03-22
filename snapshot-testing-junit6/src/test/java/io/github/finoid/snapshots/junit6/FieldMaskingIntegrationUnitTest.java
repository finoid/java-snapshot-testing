package io.github.finoid.snapshots.junit6;

import io.github.finoid.snapshots.Expect;
import io.github.finoid.snapshots.annotations.Mask;
import io.github.finoid.snapshots.jackson3.serializers.v1.JacksonSnapshotSerializer;
import io.github.finoid.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class})
public class FieldMaskingIntegrationUnitTest {

    @Test
    public void givenOrderWithSensitiveData_whenMatchingSnapshotWithMasks_thenFieldsAreMasked(Expect expect) {
        Order order = new UserOrder("ORD-123", "4111-2222-3333-4444", "John Doe");

        expect
            .serializer(JacksonSnapshotSerializer.class)
            .mask("$.customerName", "REDACTED_NAME")
            .mask("$.nonExistent", "SHOULD_NOT_SHOW")
            .toMatchSnapshot(order);
    }

    public interface Order {}

    public static class UserOrder implements Order {
        private String orderId;
        @Mask
        private String creditCard;
        private String customerName;

        public UserOrder(String orderId, String creditCard, String customerName) {
            this.orderId = orderId;
            this.creditCard = creditCard;
            this.customerName = customerName;
        }

        public String getOrderId() { return orderId; }
        public String getCreditCard() { return creditCard; }
        public String getCustomerName() { return customerName; }
    }
}
