# Specification - Refactor Tests to Given-When-Then Pattern

## Goal
Improve the readability, maintainability, and consistency of the test suite by adopting a standardized BDD-style naming convention (`given..._when..._then...`) and using internal comments (`// Given`, `// When`, `// Then`) to structure test logic.

## Requirements
- **Naming Convention:** All test method names must start with `given`, followed by the initial state, then `_when` with the action, and finally `_then` with the expected outcome.
- **Internal Structure:** Each test method must be partitioned with `// Given`, `// When`, and `// Then` comments.
- **Consistency:** The refactoring must be applied uniformly across all modules.
- **Regression:** No functional changes should be made to the tests; they must continue to pass.

## Success Criteria
- All tests in the project follow the new naming convention and internal structure.
- The build (`./mvnw test`) passes successfully across all modules.
- Code coverage remains unchanged or improves.
