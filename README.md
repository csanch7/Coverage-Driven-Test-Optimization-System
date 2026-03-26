
## Iterative Test Coverage Improvement for Spring Petclinic


Project Goal: improve automated test coverage for Spring Petclinic through iterative, metric-driven test development using JaCoCo plus an MCP coverage heatmap tool.

---

## 1. Technical Documentation

### 1.1 MCP Tool / API Documentation

Core MCP Tool: mcp-server/coverage_heatmap

Purpose:
- Parse JaCoCo XML and return per-class line coverage to identify highest-value test gaps.

Input:
- jacoco_path (optional string): path to JaCoCo XML report.
- Default: target/site/jacoco/jacoco.xml

Output:
- JSON array sorted by line coverage (lowest to highest), where each item has:
  - class (string)
  - line_coverage (number)

Example output:

```json
[
  {
    "class": "org/springframework/samples/petclinic/owner/PetController",
    "line_coverage": 90.91
  },
  {
    "class": "org/springframework/samples/petclinic/system/WelcomeController",
    "line_coverage": 100.0
  }
]
```

Commands and usage:

```bash
cd projectAnalyzed/spring-petclinic-main
mvn clean test jacoco:report
```

Then run the MCP heatmap tool on:

```text
target/site/jacoco/jacoco.xml
```

How this was used in the project:
1. Run tests and generate jacoco.xml.
2. Invoke coverage_heatmap.
3. Select lowest covered concrete classes.
4. Add focused tests.
5. Repeat until coverage meaningfully improves.

---

### 1.2 Installation & Configuration Guide

Prerequisites:
1. Java 17
2. Maven 3.8+
3. Git
4. VS Code with GitHub Copilot (for MCP-assisted workflow)

Setup:

```bash
git clone https://github.com/csanch7/Coverage-Driven-Test-Optimization-System.git
cd Coverage-Driven-Test-Optimization-System/projectAnalyzed/spring-petclinic-main
```

Build and test:

```bash
mvn clean test
```

Generate coverage:

```bash
mvn jacoco:report
```

Run application:

```bash
mvn spring-boot:run
```

App URL:
- http://localhost:8080

Key files:
1. projectAnalyzed/spring-petclinic-main/pom.xml
2. projectAnalyzed/spring-petclinic-main/target/site/jacoco/jacoco.xml
3. .github/prompts/tester.prompt.md

---

### 1.3 Troubleshooting & FAQ

Q1: coverage_heatmap fails with missing jacoco.xml
- Fix: run mvn clean test jacoco:report first.

Q2: spring-javaformat validation fails
- Fix:

```bash
mvn spring-javaformat:apply
mvn test
```

Q3: coverage appears unchanged after adding tests
- Fix: run clean build and regenerate report:

```bash
mvn clean test jacoco:report
```

Q4: test fails around Owner/Pet/Visit setup
- Fix: verify pet IDs and new vs persisted entity behavior in test fixtures.

Q5: repository interfaces remain 0% line coverage
- Explanation: OwnerRepository, PetTypeRepository, and VetRepository are interfaces without concrete line bodies in unit scope.

---

## 2. Reflection Report 

### 2.1 Introduction

This project used a coverage-first process to improve Spring Petclinic tests. The objective was to identify low-coverage classes, add targeted tests, and verify measurable progress at each iteration.

The workflow combined:
1. JaCoCo report generation
2. MCP heatmap analysis
3. AI-assisted test authoring and debugging
4. branch-scoped, auditable Git commits

### 2.2 Methodology

1. Generate baseline metrics (mvn clean test jacoco:report).
2. Rank low-coverage classes using coverage_heatmap.
3. Add focused tests for controllers, formatter, and system/config classes.
4. Re-run tests, fix failures, and re-measure.
5. Commit one logical improvement per feature branch and push.

### 2.3 Results & Discussion

Coverage improvement patterns:
1. Largest gains came from controller tests (OwnerController, PetController, VisitController, VetController).
2. Fast wins came from small system/config classes (WelcomeController, CacheConfiguration, WebConfiguration).
3. Domain model tests helped push multiple classes to 100% line coverage.

Insights from AI-assisted development:
1. AI accelerated boilerplate test setup and branch-path expansion.
2. Human review remained critical for entity lifecycle edge cases and assertion quality.
3. The heatmap + AI loop was effective because each iteration had objective coverage feedback.

Recommendations for future enhancements:
1. Add mutation testing (PIT) to validate test strength beyond line coverage.
2. Add CI gates (minimum line/branch thresholds).
3. Expand integration tests for data/repository behavior where meaningful.
4. Improve application-entry coverage for PetClinicApplication where practical.

Technical challenges and debugging insights:
1. Formatting checks (spring-javaformat) required repeated cleanup during fast test iteration.
2. Owner/Pet/Visit behavior needed careful fixture setup for new vs persisted entities.
3. Repository interfaces read as 0% line coverage by design in unit-level reports.

What AI helped with vs what required manual insight:
1. Helpful: rapid test scaffolding, branch case generation, command sequencing.
2. Manual insight: choosing high-value assertions, validating domain semantics, deciding practical stopping points.

---

## 5. Current Project Snapshot (latest verified in workspace)

Recent heatmap results show near-complete practical coverage of concrete classes:
1. OwnerController: 96.23%
2. PetController: 90.91%
3. VisitController: 90.91%
4. PetTypeFormatter: 90.91%
5. PetClinicApplication: 66.67%
6. Multiple classes at 100% including VetController, Vet, Specialty, Vets, CrashController, WebConfiguration, WelcomeController, CacheConfiguration, PetClinicRuntimeHints, and core model classes.

Note: repository interfaces remain 0% line coverage in unit-focused analysis by design.

---

<img width="347" height="803" alt="Screenshot 2026-03-10 220328" src="https://github.com/user-attachments/assets/8a151d27-ed4d-4ac5-911f-6ea9e526f109" />
(prompt: Run tester.prompt.md on spring-petclinic-main)
