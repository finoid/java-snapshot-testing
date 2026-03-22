# Specification - Circular Reference Handling

## Goal
Add support for detecting and handling circular references during object serialization in snapshot testing. When a circular reference is encountered, it should be replaced with a marker string `[CIRCULAR_REF]` to prevent infinite recursion and stack overflow errors.

## Requirements
- Detect circular references in arbitrary object graphs.
- Replace detected circular references with the literal string `[CIRCULAR_REF]`.
- Support this across both Jackson 2 and Jackson 3 serializers.
- Ensure the solution is integrated into the `JacksonSnapshotSerializer`.

## Proposed Solution
Introduce a mechanism (possibly a custom Jackson `BeanSerializerModifier` or a pre-serialization traversal) that tracks object identities using an `IdentityHashMap`. If an object is encountered that is already in the recursion stack, output the marker instead of recursing.

## References
- POC implementation in `io.github.finoid.snapshots.jackson3.serializers.v1.Main`.
