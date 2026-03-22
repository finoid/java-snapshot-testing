# Tech Stack

## Core Language & Runtime
- **Java 21:** Leveraging the latest LTS features for performance and developer productivity.
- **Maven:** The primary build and dependency management tool.

## Testing & Assertions
- **JUnit 5 & JUnit 6:** Native support for the most popular Java testing frameworks.
- **AssertJ:** Used for powerful and fluent assertions throughout the test suite.

## Serialization & Data Handling
- **Jackson 2 & Jackson 3:** Dual support for Jackson versions to ensure compatibility with a wide range of projects.
- **Json Path:** Used for flexible field masking and redaction in snapshots.
- **Lombok:** Reduces boilerplate code in data models and internal classes.

## Logging & Observability
- **Slf4j:** A flexible logging abstraction used for internal diagnostic messages.

## Architectural Patterns
- **Multi-module Maven:** A modular structure that separates the core logic from specific framework integrations (e.g., junit5, jackson2).
- **Service-Oriented Core:** A decoupled core that allows for easy extension and customization of snapshot behavior.
