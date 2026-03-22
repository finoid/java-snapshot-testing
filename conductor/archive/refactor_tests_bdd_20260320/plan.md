# Plan - Refactor Tests to Given-When-Then Pattern

## Objective
Refactor existing test cases across all modules to follow the BDD `given..._when..._then...` naming convention and use `// Given`, `// When`, `// Then` comments for better readability and structure, aligning with the `testify-testing` standards.

## Scope
All test files in:
- `snapshot-testing-core`
- `snapshot-testing-jackson2`
- `snapshot-testing-jackson3`
- `snapshot-testing-junit5`
- `snapshot-testing-junit6`

## Implementation Steps

### Phase 1: snapshot-testing-jackson3
1.  **CircularReferenceTest.java**:
    - [x] Rename `shouldHandleCircularReference` to `givenCircularReference_whenSerializing_thenCircularRefMarkerIsPresent`. [84ff734]
    - [x] Add GWT comments. [84ff734]
2.  **FieldMaskingTest.java**:
    - [x] Rename `shouldMaskAnnotatedField` to `givenAnnotatedField_whenSerializing_thenFieldIsMasked`. [84ff734]
    - [x] Rename `shouldMaskAnnotatedClass` to `givenAnnotatedClass_whenSerializing_thenFieldsAreMasked`. [84ff734]
    - [x] Rename `shouldMaskProgrammatically` to `givenProgrammaticMask_whenSerializing_thenFieldIsMasked`. [84ff734]
    - [x] Add GWT comments. [84ff734]
3.  **DeterministicJacksonSnapshotSerializerTest.java**:
    - [x] Rename `shouldSerializeDifferentTypes` to `givenDifferentTypes_whenSerializingWithDeterministicSerializer_thenSnapshotMatches`. [84ff734]
    - [x] Rename `shouldSupportJsonFormat` to `givenDeterministicSerializer_whenGettingOutputFormat_thenIsJson`. [84ff734]
    - [x] Add GWT comments. [84ff734]
4.  **JacksonSnapshotSerializerTest.java**:
    - [x] Rename `shouldSerializeMap` to `givenMap_whenSerializing_thenSnapshotMatches`. [84ff734]
    - [x] Rename `shouldSerializeDifferentTypes` to `givenDifferentTypes_whenSerializing_thenSnapshotMatches`. [84ff734]
    - [x] Rename `shouldSupportJsonFormat` to `givenSerializer_whenGettingOutputFormat_thenIsJson`. [84ff734]
    - [x] Add GWT comments. [84ff734]

### Phase 2: snapshot-testing-jackson2
- [x] Mirror changes from `snapshot-testing-jackson3` for equivalent test files. [84ff734]

### Phase 3: snapshot-testing-core
- [x] Refactor `SnapshotMatcherTest`, `SnapshotUtilsTest`, `comparators/*`, `serializers/*`, etc. [84ff734]
- [x] Apply `given..._when..._then...` pattern. [84ff734]

### Phase 4: snapshot-testing-junit5 & junit6
- [x] Refactor `SnapshotParameterTest`, `FieldMaskingIntegrationTest`, `CircularReferenceIntegrationTest`, etc. [84ff734]

## Verification
- [x] Run `./mvnw test` across all modules to ensure all tests still pass. [84ff734]
- [x] Verify naming consistency. [84ff734]
