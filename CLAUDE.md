# aaa-POC.xorcery.dev (Aurora POC Data Generator)

## Purpose
Data generator and POC demonstration tool for the Xorcery Aurora platform. Generates realistic consultancy event streams (project management, resource allocation, client relationships, skills/certifications) and analyzes them to demonstrate Aurora's temporal analytics and "why" question answering capabilities.

## Tech Stack
- Language: Java 21
- Build: Maven
- Package: `com.aurorapoc.datagenerator`

## Architecture
Event generation pipeline: scenario configuration → domain generators → event stream writer → Aurora analysis. Includes report generation (executive summary, "why" analysis). Key generators: ClientRelationship, ResourceAllocation, ProjectManagement, SkillsCertification.

## Key Entry Points
- `src/main/java/com/aurorapoc/datagenerator/generators/` — event generators
- `src/main/java/com/aurorapoc/datagenerator/reports/` — analytics reports
- `src/main/java/com/aurorapoc/datagenerator/io/EventStreamLoader/Writer` — I/O

## Development
```bash
# Build
mvn clean install

# Run data generation
mvn exec:java
```

## Domain Context
POC/demo asset for the Xorcery AAA product. Generates synthetic consultancy data to demonstrate Aurora's temporal intelligence (root cause analysis, resource tracking, project causality). Used in sales demos and proof-of-concept engagements.
