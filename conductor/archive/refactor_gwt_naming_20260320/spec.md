# Specification - Refactor Tests to Given-When-Then Naming Pattern

## Overview
This track involves refactoring the entire test suite across all Maven modules to adopt a standardized BDD-style naming convention: `given[State]_when[Action]_then[Outcome]`. This change aims to improve test readability and alignment with BDD principles without introducing internal structural comments like `// Given`, `// When`, or `// Then`.

## Functional Requirements
- **Naming Convention:** Every test method must be renamed to follow the `given[State]_when[Action]_then[Outcome]` pattern using CamelCase for the descriptive parts.
- **Global Scope:** The refactoring must be applied to all modules:
    - `snapshot-testing-core`
    - `snapshot-testing-jackson2`
    - `snapshot-testing-jackson3`
    - `snapshot-testing-junit5`
    - `snapshot-testing-junit6`
- **Constraint:** Do NOT add `// Given`, `// When`, or `// Then` comments within the test method bodies.

## Non-Functional Requirements
- **Regression Testing:** All tests must pass after refactoring. No functional changes to the test logic are permitted.
- **Maintainability:** Standardizing test names across the library improves the onboarding experience for new contributors.

## Acceptance Criteria
- [ ] All test methods in all modules follow the `given..._when..._then...` naming pattern.
- [ ] No `// Given`, `// When`, or `// Then` comments are present in the refactored tests.
- [ ] The entire project builds successfully with `./mvnw clean test`.
- [ ] Code coverage remains unchanged.

## Out of Scope
- Adding new test cases.
- Changing test logic or assertions.
- Refactoring application code.
