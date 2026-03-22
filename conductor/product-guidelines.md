# Product Guidelines

## Prose Style: Direct & Clear
Our documentation and system messages are designed to be immediately understandable. We prioritize clarity over complex terminology, ensuring that developers can find the information they need and get back to coding quickly.

## UX Principles: Transparent Errors & Intuitive Flow
- **Transparent Errors:** When a snapshot fails or an error occurs, the library provides clear, informative feedback that helps developers diagnose the issue immediately. We avoid cryptic error codes in favor of actionable descriptions.
- **Intuitive Flow:** Every feature is designed to fit logically within the standard testing lifecycle. Snapshot creation and verification should feel like a natural extension of existing test suites, with minimal friction.

## Brand Personality: Robust & Reliable
This project is built for stability and consistent performance. Our users should feel confident that the library is a dependable part of their CI/CD pipeline, offering rock-solid results and a focused, functional experience.

## Documentation: Example Driven
Our primary method of teaching is through concrete, copy-pasteable examples. While we cover the underlying concepts, we believe that developers learn best by seeing the library in action within familiar contexts.

## Visual and Aesthetic Standards
- **Clean Output:** Test reports and snapshot files should be well-formatted and easy for both humans and machines to read.
- **Minimalist Design:** We focus on providing only the necessary information in CLI outputs, avoiding unnecessary noise.
- **Functional Beauty:** Aesthetics are used to enhance functionality, such as using color or formatting to highlight diffs in failed snapshot comparisons.

## Testing Standards: BDD Naming Convention
We adopt a standardized BDD-style naming convention for all test methods: `given[State]_when[Action]_then[Outcome]`.
- **CamelCase:** Use CamelCase for the descriptive parts of the name.
- **No Structural Comments:** Avoid using `// Given`, `// When`, or `// Then` comments within the test bodies, as the method name should provide sufficient context.
