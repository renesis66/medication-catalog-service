# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Medication Catalog Service** is a Kotlin-based microservice built with Micronaut framework following Domain-Driven Design (DDD) principles. It manages medication master data within the "Medication Master Data" bounded context, providing APIs for medication catalog operations with DynamoDB persistence.

## Essential Commands

### Build & Run
```bash
./gradlew run              # Start the application (localhost:8080)
./gradlew build            # Build the project  
./gradlew shadowJar        # Create executable fat JAR
./gradlew test             # Run all tests
./gradlew test --tests MedicationCatalogApiControllerTest  # Run specific test class
```

### Development
```bash
./gradlew compileKotlin    # Compile Kotlin sources
./gradlew processResources # Process resources only
```

## Architecture Overview

### Layer Structure (Clean Architecture + DDD)

1. **Domain Layer** (`domain/`): Core business logic, zero external dependencies
   - **Aggregate Roots**: `MedicationCatalog` (primary), `Medication` (extended model)
   - **Value Objects**: `MedicationName`, `FrequencyHours`, `DosageAmount`, `ActiveIngredient`
   - **Domain Services**: `MedicationCatalogDomainService`, `MedicationDomainService`
   - **Repository Interfaces**: Define data access contracts

2. **Application Layer** (`application/`): Use case orchestration
   - **Application Services**: Coordinate domain services and repositories
   - **DTOs**: Request/Response objects with domain mapping
   - Entry point for all business operations

3. **Infrastructure Layer** (`infrastructure/`): External concerns
   - **DynamoDB**: Entity mapping, repository implementations
   - **Web**: REST controllers with security annotations
   - **Config**: DI bindings, DynamoDB client setup, data initialization

### Key Architectural Patterns

- **Repository Pattern**: `MedicationCatalogRepository` interface with DynamoDB implementation
- **Aggregate Design**: `MedicationCatalog` encapsulates business rules and invariants
- **Value Objects**: Immutable types with validation (e.g., `MedicationName`, `FrequencyHours`)
- **Domain Services**: Complex business logic that doesn't belong to a single entity
- **Application Services**: Transaction boundaries and use case orchestration

## DynamoDB Design

### Single Table: `medication-catalog`

**Access Patterns**:
1. **Get medication by name**: `PK = MED#{name}`, `SK = METADATA`
2. **List by category**: `GSI1PK = CATEGORY#{category}` (uses GSI1)

**Key Structure**:
- **PK**: `MED#{medicationName}` 
- **SK**: `METADATA`
- **GSI1PK**: `CATEGORY#{category}`
- **GSI1SK**: `MED#{medicationName}`

### Data Model
```kotlin
// Core domain aggregate
MedicationCatalog {
    medicationName: MedicationName      // Value object with validation
    category: MedicationCategory        // Enum (ANTIBIOTIC, ANALGESIC, etc.)
    standardDosages: List<Int>          // Standard dose amounts
    defaultDosageUnits: List<DosageUnit> // Units (mg, tablets, etc.)
    frequencyOptions: List<FrequencyHours> // Dosing intervals (4,6,8,12,24h)
    maxDailyDose: Int                   // Safety limit
    contraindications: List<String>     // Safety warnings
    status: MedicationStatus            // ACTIVE, INACTIVE, etc.
}
```

## API Endpoints

### Public (Anonymous Access)
- `GET /medications/catalog` - All active medications
- `GET /medications/catalog/{medicationName}` - Single medication by name
- `GET /medications/catalog/category/{categoryName}` - Medications by category
- `GET /medications/catalog/categories` - Available categories list

### Admin (Requires ADMIN role)
- `POST /medications/catalog` - Create new medication

## Development Context

### Framework Stack
- **Micronaut 4.3.8**: Reactive microservices with dependency injection
- **Kotlin 1.9.23**: Primary language with coroutines for async operations
- **DynamoDB Enhanced Client**: Type-safe NoSQL operations
- **Micronaut Security**: Role-based access control
- **JUnit 5**: Testing with Micronaut Test integration

### Business Rules & Validation
The domain layer enforces these key business rules:
- Medications must have ≥1 standard dosage and frequency option
- Maximum daily dose must be positive and validated against dosage combinations
- Medication names are validated (alphanumeric + spaces/hyphens only)
- Frequency options restricted to 4, 6, 8, 12, or 24 hours
- Duplicate medication names are prevented

### Configuration Notes
- **Local Development**: DynamoDB configured for `localhost:8000`
- **Security**: Uses role-based security with ADMIN role for mutations
- **Data Initialization**: Sample medications auto-loaded on startup
- **Async Operations**: All repository and service methods use `suspend` functions

### Testing Strategy
- **Integration Tests**: Full HTTP testing with `@MicronautTest`
- **Controller Tests**: Real HTTP client testing against endpoints
- **Domain Tests**: Business rule validation in domain layer
- **Test Environment Isolation**: Uses in-memory repository implementation
- Use `MedicationCatalogApiControllerTest` as template for new endpoint tests

#### Test Configuration (✅ WORKING)
- All tests pass successfully (100% success rate)
- In-memory repository replaces DynamoDB during tests
- No external dependencies required for testing
- Proper logging configuration prevents test noise

#### Test Environment Setup
```kotlin
// Production beans excluded from test environment
@Requires(notEnv = ["test"])
class DynamoDbConfig { /* ... */ }

@Requires(notEnv = ["test"]) 
class RepositoryConfig { /* ... */ }

@Requires(notEnv = ["test"])
class MedicationCatalogRepositoryImpl { /* ... */ }

// Test-only implementation
@Requires(env = ["test"])
class InMemoryMedicationCatalogRepository { /* ... */ }
```

### Common Development Patterns

**Adding New Medication Properties**:
1. Update `MedicationCatalog` domain model with validation
2. Modify `MedicationCatalogEntity` for DynamoDB mapping  
3. Update DTOs and mappers in application layer
4. Extend API responses as needed

**Adding New Business Rules**:
1. Implement in domain model or domain service
2. Add validation in `MedicationCatalogDomainService`
3. Write tests to verify rule enforcement
4. Update API error handling if needed

**Adding New Access Patterns**:
1. Design DynamoDB keys/indexes for query pattern
2. Add repository method with proper DynamoDB query
3. Create application service method
4. Expose via controller if needed

## Project Status & Recent Changes

### ✅ Current Status (Latest Session Completed)
- **Build Status**: ✅ All builds successful
- **Test Status**: ✅ 100% test success rate (1/1 tests passing)
- **Repository**: ✅ Committed and pushed to GitHub
- **Dependencies**: ✅ All runtime dependencies resolved

### Recent Fixes Applied
1. **Test Configuration Issues Resolved**:
   - Fixed repository bean injection conflicts
   - Added proper environment-based bean selection with `@Requires` annotations
   - Created `InMemoryMedicationCatalogRepository` for test isolation
   - Added comprehensive logging configuration (`logback.xml`, `logback-test.xml`)

2. **Infrastructure Improvements**:
   - Added comprehensive `.gitignore` for Micronaut Kotlin projects
   - Properly excluded build artifacts from version control
   - Organized test resources and configuration files

3. **Dependency Resolution**:
   - Added missing `snakeyaml` dependency for YAML configuration support
   - Added `kotlinx-coroutines-core` for suspend function support
   - Included proper test dependencies (JUnit 5, Logback)

### Known Working Commands (Verified)
```bash
./gradlew test              # ✅ All tests pass
./gradlew build             # ✅ Build succeeds
./gradlew run               # ✅ Application starts (requires DynamoDB local)
git status                  # ✅ Clean working directory
```

### Key Lessons Learned
1. **Always use environment-specific beans** instead of `@Replaces` for test isolation
2. **Include YAML parser dependency** (`snakeyaml`) when using application.yml
3. **Separate test logging configuration** to reduce noise during test execution
4. **Exclude production infrastructure** from test environment with `@Requires(notEnv = ["test"])`

### Next Development Areas
- Add more comprehensive API tests for edge cases
- Implement additional medication categories and business rules
- Add validation for medication interactions
- Consider adding caching layer for frequently accessed medications

### Git Repository
- **URL**: https://github.com/renesis66/medication-catalog-service
- **Branch**: main (up to date)
- **Last Commit**: Test configuration fixes and gitignore addition