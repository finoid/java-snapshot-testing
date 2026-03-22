# Specification - Field Masking with JSON Path

## Overview
Introduce support for masking sensitive or dynamic fields in snapshots using JSON Path expressions. This ensures that sensitive data (like passwords or tokens) or volatile data (like timestamps or UUIDs) can be redacted from snapshots to prevent unnecessary test failures or security leaks.

## Functional Requirements
- **JSON Path Support:** Use `com.jayway.jsonpath` to identify fields for masking.
- **Dual Jackson Support:** Implement masking for both `JacksonSnapshotSerializer` (Jackson 2) and `JacksonSnapshotSerializer` (Jackson 3).
- **Configuration Methods:**
    - **Annotation:** Support a new `@Mask` annotation on fields or classes to specify JSON paths to mask.
    - **Expect API:** Enhance the `Expect` API to allow programmatic masking: `expect.mask("$.path.to.field", "***REDACTED***")`.
- **Customizable Mask Value:** Allow the redaction string to be customized (default: `***REDACTED***`).
- **Ignore Missing Paths:** If a specified JSON path does not match any fields in the actual output, it should be silently ignored.

## Non-Functional Requirements
- **Performance:** Masking should have minimal overhead on the serialization process.
- **Thread Safety:** The masking logic must be thread-safe.

## Acceptance Criteria
- A user can apply `@Mask("$.creditCard")` to a class, and that field is redacted in the resulting snapshot.
- A user can call `expect.mask("$..password").toMatchSnapshot(user)` and all password fields are redacted.
- Both Jackson 2 and Jackson 3 serializers produce correctly masked JSON output.
- Non-matching paths do not cause errors or warnings.

## Out of Scope
- Masking for non-JSON serializers (e.g., `ToStringSnapshotSerializer`).
- Complex masking logic (e.g., partial redaction like `4111-****-****-1111`).
