# Implementation Plan - Refactor Tests to Given-When-Then Naming Pattern

## Phase 1: snapshot-testing-core Refactoring
- [x] Task: Identify all test files in `snapshot-testing-core`
- [x] Task: Refactor test method names in `snapshot-testing-core` to `given..._when..._then...` pattern
- [x] Task: Verify `snapshot-testing-core` builds and tests pass locally
- [x] Task: Conductor - User Manual Verification 'Phase 1: snapshot-testing-core Refactoring' (Protocol in workflow.md)

## Phase 2: Jackson Modules Refactoring (jackson2 & jackson3)
- [x] Task: Identify all test files in `snapshot-testing-jackson2` and `snapshot-testing-jackson3`
- [x] Task: Refactor test method names in `snapshot-testing-jackson2` to `given..._when..._then...` pattern
- [x] Task: Refactor test method names in `snapshot-testing-jackson3` to `given..._when..._then...` pattern
- [x] Task: Verify jackson modules build and tests pass locally
- [x] Task: Conductor - User Manual Verification 'Phase 2: Jackson Modules Refactoring' (Protocol in workflow.md)

## Phase 3: JUnit Modules Refactoring (junit5 & junit6)
- [x] Task: Identify all test files in `snapshot-testing-junit5` and `snapshot-testing-junit6`
- [x] Task: Refactor test method names in `snapshot-testing-junit5` to `given..._when..._then...` pattern
- [x] Task: Refactor test method names in `snapshot-testing-junit6` to `given..._when..._then...` pattern
- [x] Task: Verify junit modules build and tests pass locally
- [x] Task: Conductor - User Manual Verification 'Phase 3: JUnit Modules Refactoring' (Protocol in workflow.md)

## Phase 4: Final Global Verification
- [x] Task: Run full project build and tests: `./mvnw clean test`
- [x] Task: Ensure no `// Given`, `// When`, or `// Then` comments were accidentally introduced
- [x] Task: Conductor - User Manual Verification 'Phase 4: Final Global Verification' (Protocol in workflow.md)
