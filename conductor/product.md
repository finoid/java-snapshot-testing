# Initial Concept
A Java library designed to streamline testing by snapshot testing.

# Product Guide

## Overview
This project provides a robust, native Java alternative to traditional assertion methods by enabling snapshot testing. It's designed to reduce the overhead of writing complex, data-heavy assertions by comparing output against stored, human-readable snapshots.

## Target Audience
- **Java Developers:** Specifically those seeking to reduce boilerplate in their test code and manage large JSON or object structures with ease.

## Core Goals
- **Ease of Assertions:** Simplify the testing process by comparing objects against saved snapshots instead of writing numerous manual `assertEquals` calls.
- **Testing Efficiency:** Streamline the entire testing lifecycle, especially for data-heavy applications, by automating the verification of complex states.

## Key Features
- **Jackson Support:** Comprehensive support for both Jackson 2 and Jackson 3, including robust handling of circular references and field masking using JSON Path to redact sensitive or dynamic data.
- **JUnit Integration:** Seamless, native extensions for JUnit 5 and JUnit 6, ensuring that snapshot testing fits naturally into standard Java testing workflows.
- **Customizable Serialization:** A modular core that allows for extending and customizing how snapshots are generated and compared.

## Value Proposition
This library stands out by offering unparalleled framework flexibility within the Java ecosystem, combined with a focus on human-readable snapshots that improve both debugging and long-term maintainability of test suites.
