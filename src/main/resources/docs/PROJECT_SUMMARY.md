# Discount Platform - Project Summary

A Spring Boot 3 REST API for managing a discount/benefits platform where **clients** living in **buildings** can redeem **offers** from **suppliers**.

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Package Structure](#package-structure)
3. [Entity Model & Relationships](#entity-model--relationships)
4. [DTOs](#dtos)
5. [Mappers](#mappers)
6. [Repositories](#repositories)
7. [Services](#services)
8. [Controllers & API Endpoints](#controllers--api-endpoints)
9. [Exception Handling](#exception-handling)
10. [Utilities](#utilities)
11. [Configuration](#configuration)
12. [Validation & Constraints](#validation--constraints)

---

## Architecture Overview

The project follows a layered architecture with a clear separation of concerns:

```diagram
Controller (HTTP)  -->  Service (Business Logic)  -->  Repository (Data Access)
     |                       |
     v                       v
    DTOs                  Entities
     ^                       ^
     |_________Mapper________|
```

**Tech stack:** Spring Boot 3, Spring Data JPA, MapStruct, Lombok, Swagger/OpenAPI, Jakarta Validation.

---

## Package Structure

```diagram
com.mygroup.discountplatform
|-- DiscountPlatformApplication.java        # Entry point (@SpringBootApplication)
|-- config/
|   |-- JpaConfig.java                      # @EnableJpaAuditing
|-- entities/
|   |-- BaseEntity.java                     # Abstract: createdAt, updatedAt (audited)
|   |-- BuildingEntity.java                 # Physical building location
|   |-- ClientEntity.java                   # Person/client
|   |-- SupplierEntity.java                 # Discount vendor
|   |-- OfferEntity.java                    # Discount offer
|   |-- RedemptionEntity.java               # Client-Offer redemption record
|   |-- BuildingSupplierEntity.java         # Building-Supplier join entity
|   |-- BuildingSupplierId.java             # Composite key for above
|   |-- Category.java                       # Supplier category
|   |-- HobbyEntity.java                    # Client hobby
|   |-- ProfessionEntity.java               # Client profession
|   |-- enums/
|   |   |-- Genre.java                      # MALE(1), FEMALE(2), OTHER(3)
|   |   |-- RedemptionStatus.java           # PENDING, REDEEMED, EXPIRED, CANCELLED
|   |-- converters/
|       |-- GenreConverter.java             # Genre <-> Integer JPA converter
|-- repositories/
|   |-- BuildingRepository.java
|   |-- ClientRepository.java
|   |-- HobbyRepository.java
|   |-- ProfessionRepository.java
|-- dtos/
|   |-- BuildingCreateRequestDTO.java
|   |-- BuildingResponseDTO.java
|   |-- BuildingListDTO.java
|   |-- BuildingByCityDTO.java
|   |-- ClientCreateRequestDTO.java
|   |-- ClientResponseDTO.java
|   |-- ClientListDTO.java
|   |-- HobbyDTO.java
|   |-- ProfessionDTO.java
|   |-- ErrorResponseDTO.java
|-- mappers/
|   |-- BuildingMapper.java                 # MapStruct interface
|   |-- ClientMapper.java                   # MapStruct interface
|-- services/
|   |-- BuildingService.java
|   |-- ClientService.java
|-- controllers/
|   |-- BuildingController.java
|   |-- ClientController.java
|-- exceptions/
|   |-- GlobalExceptionHandler.java         # @RestControllerAdvice
|-- utils/
    |-- CORS.java                           # CORS filter (allows all origins)
    |-- Util.java                           # RUT validation, time formatting
```

---

## Entity Model & Relationships

### Relational Diagram

```diagram
Category (1) ──────────< (N) SupplierEntity
                                   |
                                   | (1)
                                   v
                          BuildingSupplierEntity (N) >────── (1) BuildingEntity
                          [composite key:                           |
                           buildingId + supplierId]                 | (1)
                                   |                               v
                                   | (1)                    (N) ClientEntity
                                   v                         /     |     \
                          (N) OfferEntity               (M:N)   (M:N)   (1:N)
                                   |                   /         |         \
                                   | (1)         HobbyEntity  ProfessionEntity
                                   v
                          (N) RedemptionEntity (N) >────── (1) ClientEntity
```

### Entity Details

| Entity                   | Table               | PK Column       | Extends      | Key Fields                                                                                                                  |
|--------------------------|---------------------|-----------------|--------------|-----------------------------------------------------------------------------------------------------------------------------|
| `BaseEntity`             | (mapped superclass) | —               | —            | `createdAt`, `updatedAt` (audited)                                                                                          |
| `BuildingEntity`         | `building`          | `id_building`   | `BaseEntity` | `name`, `address`, `city`                                                                                                   |
| `ClientEntity`           | `clients`           | `id_client`     | `BaseEntity` | `rut` (unique), `firstName`, `lastNamePaternal`, `lastNameMaternal`, `email` (unique), `phone`, `departmentNumber`, `genre` |
| `SupplierEntity`         | `supplier`          | `id_supplier`   | `BaseEntity` | `name`, `address`, `contactEmail` (unique), `phone`                                                                         |
| `OfferEntity`            | `offer`             | `id_offer`      | `BaseEntity` | `title`, `description`, `discountPercentage`, `validFrom`, `validTo`, `quantityAvailable`                                   |
| `RedemptionEntity`       | `redemption`        | `id_redemption` | —            | `redeemedAt`, `status` (enum)                                                                                               |
| `BuildingSupplierEntity` | `building_supplier` | composite       | —            | `joinedAt`                                                                                                                  |
| `Category`               | `category`          | `id_category`   | —            | `name`                                                                                                                      |
| `HobbyEntity`            | `hobby`             | `id_hobby`      | —            | `name` (unique)                                                                                                             |
| `ProfessionEntity`       | `profession`        | `id_profession` | —            | `name` (unique)                                                                                                             |

### Join Tables

| Table               | Columns                                   | Purpose                                             |
|---------------------|-------------------------------------------|-----------------------------------------------------|
| `client_hobby`      | `id_client`, `id_hobby`                   | M:N between Client and Hobby                        |
| `client_profession` | `id_client`, `id_profession`              | M:N between Client and Profession                   |
| `building_supplier` | `id_building`, `id_supplier`, `joined_at` | M:N between Building and Supplier (with extra data) |

### Enums

- **`Genre`**: `MALE(1)`, `FEMALE(2)`, `OTHER(3)` — stored as integer via `GenreConverter`
- **`RedemptionStatus`**: `PENDING`, `REDEEMED`, `EXPIRED`, `CANCELLED` — stored as string (`@Enumerated(EnumType.STRING)`)

### Database Indexes

| Table        | Index Name              | Column(s)     |
|--------------|-------------------------|---------------|
| `building`   | `idx_building_city`     | `city`        |
| `clients`    | `idx_client_rut`        | `rut`         |
| `clients`    | `idx_client_email`      | `email`       |
| `clients`    | `idx_client_building`   | `id_building` |
| `supplier`   | `idx_supplier_email`    | `email`       |
| `supplier`   | `idx_supplier_category` | `id_category` |
| `offer`      | `idx_offer_supplier`    | `id_supplier` |
| `offer`      | `idx_offer_valid_from`  | `valid_from`  |
| `redemption` | `idx_redemption_client` | `id_client`   |
| `redemption` | `idx_redemption_offer`  | `id_offer`    |

---

## DTOs

### Building DTOs

| DTO                        | Purpose                 | Fields                                                    |
|----------------------------|-------------------------|-----------------------------------------------------------|
| `BuildingCreateRequestDTO` | Create request body     | `name`, `address`, `city` (all `@NotBlank`)               |
| `BuildingResponseDTO`      | Create/detail response  | `id`, `name`, `address`, `city`, `createdAt`, `updatedAt` |
| `BuildingListDTO`          | List item               | `name`, `address`, `city`                                 |
| `BuildingByCityDTO`        | City-filtered list item | `name`, `address`                                         |

### Client DTOs

| DTO                      | Purpose             | Fields                                                                                                                                               |
|--------------------------|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ClientCreateRequestDTO` | Create request body | `rut`, `firstName`, `lastNamePaternal`, `lastNameMaternal`, `email`, `phone`, `departmentNumber`, `genre`, `buildingId`, `hobbyIds`, `professionIds` |
| `ClientResponseDTO`      | Detail response     | All client fields + `buildingId`, `buildingName`, `hobbies` (List\<HobbyDTO\>), `professions` (List\<ProfessionDTO\>)                                |
| `ClientListDTO`          | List item           | `id`, `rut`, `firstName`, `lastNamePaternal`, `email`, `genre`, `buildingName`                                                                       |

### Auxiliary DTOs

| DTO                | Purpose         | Fields                           |
|--------------------|-----------------|----------------------------------|
| `HobbyDTO`         | Hobby info      | `id`, `name`                     |
| `ProfessionDTO`    | Profession info | `id`, `name`                     |
| `ErrorResponseDTO` | Error response  | `status`, `message`, `timestamp` |

---

## Mappers

MapStruct interfaces with `componentModel = SPRING`.

### `BuildingMapper`

| Method              | From                       | To                        | Notes                                     |
|---------------------|----------------------------|---------------------------|-------------------------------------------|
| `toEntity()`        | `BuildingCreateRequestDTO` | `BuildingEntity`          | Ignores `id`, audit fields, relationships |
| `toResponseDTO()`   | `BuildingEntity`           | `BuildingResponseDTO`     | —                                         |
| `toListDTO()`       | `BuildingEntity`           | `BuildingListDTO`         | —                                         |
| `toByCityDTO()`     | `BuildingEntity`           | `BuildingByCityDTO`       | —                                         |
| `toListDTOList()`   | `List<BuildingEntity>`     | `List<BuildingListDTO>`   | Batch                                     |
| `toByCityDTOList()` | `List<BuildingEntity>`     | `List<BuildingByCityDTO>` | Batch                                     |

### `ClientMapper`

| Method              | From                     | To                    | Notes                                                                 |
|---------------------|--------------------------|-----------------------|-----------------------------------------------------------------------|
| `toEntity()`        | `ClientCreateRequestDTO` | `ClientEntity`        | Ignores `id`, audit fields, relationships                             |
| `toResponseDTO()`   | `ClientEntity`           | `ClientResponseDTO`   | Maps `building.id` -> `buildingId`, `building.name` -> `buildingName` |
| `toListDTO()`       | `ClientEntity`           | `ClientListDTO`       | Maps `building.name` -> `buildingName`                                |
| `toListDTOList()`   | `List<ClientEntity>`     | `List<ClientListDTO>` | Batch                                                                 |
| `toHobbyDTO()`      | `HobbyEntity`            | `HobbyDTO`            | —                                                                     |
| `toProfessionDTO()` | `ProfessionEntity`       | `ProfessionDTO`       | —                                                                     |

---

## Repositories

All extend `JpaRepository` and are annotated with `@Repository`.

### `BuildingRepository` (extends `JpaRepository<BuildingEntity, Long>`)

| Method                    | Return Type            | Description                 |
|---------------------------|------------------------|-----------------------------|
| `findByCity(String city)` | `List<BuildingEntity>` | Find buildings by city name |

### `ClientRepository` (extends `JpaRepository<ClientEntity, Long>`)

| Method                              | Return Type              | Description           |
|-------------------------------------|--------------------------|-----------------------|
| `findByBuildingId(Long buildingId)` | `List<ClientEntity>`     | Clients in a building |
| `findByEmail(String email)`         | `Optional<ClientEntity>` | Lookup by email       |
| `findByRut(String rut)`             | `Optional<ClientEntity>` | Lookup by RUT         |
| `existsByEmail(String email)`       | `boolean`                | Email existence check |
| `existsByRut(String rut)`           | `boolean`                | RUT existence check   |

### `HobbyRepository` (extends `JpaRepository<HobbyEntity, Long>`)

Standard CRUD only.

### `ProfessionRepository` (extends `JpaRepository<ProfessionEntity, Long>`)

Standard CRUD only.

---

## Services

### `BuildingService`

| Method                | Return                    | Description                                                   |
|-----------------------|---------------------------|---------------------------------------------------------------|
| `findAll()`           | `List<BuildingListDTO>`   | All buildings                                                 |
| `createBuilding(dto)` | `BuildingResponseDTO`     | Create and persist a new building                             |
| `findByCity(city)`    | `List<BuildingByCityDTO>` | Buildings in a city; throws `EntityNotFoundException` if none |

### `ClientService`

| Method                         | Return                | Description                                     |
|--------------------------------|-----------------------|-------------------------------------------------|
| `findAll()`                    | `List<ClientListDTO>` | All clients                                     |
| `findById(id)`                 | `ClientResponseDTO`   | Client detail; throws `EntityNotFoundException` |
| `findByBuildingId(buildingId)` | `List<ClientListDTO>` | Clients in a building                           |
| `createClient(dto)`            | `ClientResponseDTO`   | Create client with validations (see below)      |

**`createClient` validations:**
1. Email must be unique (`IllegalArgumentException`)
2. RUT must be unique (`IllegalArgumentException`)
3. Building must exist (`EntityNotFoundException`)
4. Max 2 professions per client (`IllegalArgumentException`)
5. Associates hobbies and professions if IDs are provided

---

## Controllers & API Endpoints

### `BuildingController` — `/buildings`

| Method | Path         | Params            | Request Body               | Response                                             | Status |
|--------|--------------|-------------------|----------------------------|------------------------------------------------------|--------|
| `GET`  | `/buildings` | `city` (optional) | —                          | `List<BuildingListDTO>` or `List<BuildingByCityDTO>` | 200    |
| `POST` | `/buildings` | —                 | `BuildingCreateRequestDTO` | `BuildingResponseDTO`                                | 201    |

### `ClientController` — `/clients`

| Method | Path            | Params                  | Request Body             | Response              | Status |
|--------|-----------------|-------------------------|--------------------------|-----------------------|--------|
| `GET`  | `/clients`      | `buildingId` (optional) | —                        | `List<ClientListDTO>` | 200    |
| `GET`  | `/clients/{id}` | —                       | —                        | `ClientResponseDTO`   | 200    |
| `POST` | `/clients`      | —                       | `ClientCreateRequestDTO` | `ClientResponseDTO`   | 201    |

---

## Exception Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) handles:

| Exception                  | HTTP Status     | Logging |
|----------------------------|-----------------|---------|
| `EntityNotFoundException`  | 404 Not Found   | WARN    |
| `IllegalArgumentException` | 400 Bad Request | WARN    |

All error responses use `ErrorResponseDTO` with `status`, `message`, and `timestamp`.

---

## Utilities

### `CORS` Filter

- Runs at `HIGHEST_PRECEDENCE`
- Allows all origins (`*`)
- Allows methods: `POST`, `GET`, `PUT`
- Preflight cache: 3600 seconds
- Returns `200 OK` for `OPTIONS` requests

### `Util` Component

| Method             | Signature                                  | Description                                                 |
|--------------------|--------------------------------------------|-------------------------------------------------------------|
| `rutNormalization` | `String rutNormalization(String rut)`      | Removes dots, uppercases (`12.345.678-9` -> `12345678-9`)   |
| `rutValidation`    | `boolean rutValidation(String rut)`        | Validates Chilean RUT format and check digit (modulo-11)    |
| `formatTimeAgo`    | `String formatTimeAgo(LocalDateTime past)` | Converts timestamp to relative string ("2 hours ago", etc.) |

---

## Configuration

### `JpaConfig`

- **Location:** `config/JpaConfig.java`
- **Annotations:** `@Configuration`, `@EnableJpaAuditing`
- **Purpose:** Enables Spring Data JPA auditing so that `@CreatedDate` and `@LastModifiedDate` fields in `BaseEntity` are automatically populated on persist and update operations.

### `GenreConverter`

- **Location:** `entities/converters/GenreConverter.java`
- **Annotations:** `@Converter(autoApply = true)`
- **Implements:** `AttributeConverter<Genre, Integer>`
- **Purpose:** Automatically converts the `Genre` enum to its integer code when writing to the database and back to the enum when reading. Applied globally via `autoApply = true`.

### CORS Filter

- **Location:** `utils/CORS.java`
- **Annotations:** `@Component`, `@Order(Ordered.HIGHEST_PRECEDENCE)`
- **Purpose:** Servlet filter that adds CORS headers to every response, allowing cross-origin requests from any origin (`*`).

---

## Validation & Constraints

### Field-Level Validations

| Entity/DTO | Field                                     | Constraint                          |
|------------|-------------------------------------------|-------------------------------------|
| Building   | `name`, `address`, `city`                 | `@NotBlank`                         |
| Client     | `rut`                                     | `@NotBlank`, unique                 |
| Client     | `firstName`                               | `@NotBlank`, `@Size(min=2, max=50)` |
| Client     | `lastNamePaternal`                        | `@NotBlank`                         |
| Client     | `email`                                   | `@NotBlank`, `@Email`, unique       |
| Client     | `phone`                                   | `@Pattern(^\\+?[0-9]{9,12}$)`       |
| Client     | `professionIds`                           | `@Size(max=2)`                      |
| Client     | `buildingId`                              | `@NotNull`                          |
| Supplier   | `name`, `address`, `phone`                | `@NotBlank`                         |
| Supplier   | `contactEmail`                            | `@NotBlank`, `@Email`, unique       |
| Supplier   | `phone`                                   | `@Pattern(^\\+?[0-9]{9,12}$)`       |
| Offer      | `title`, `description`                    | `@NotBlank`                         |
| Offer      | `discountPercentage`                      | `@NotNull`                          |
| Redemption | `offer`, `client`, `redeemedAt`, `status` | `@NotNull`                          |

### Business Rules (enforced in `ClientService`)

- A client's email and RUT must be unique across the system
- A client can have at most two professions
- The building referenced by `buildingId` must exist before client creation

---

## Notes

- **Auditing**: `createdAt` and `updatedAt` are automatically managed by Spring Data JPA auditing (configured in `JpaConfig`)
- **Cascade behavior**: `BuildingEntity`, `ClientEntity`, `SupplierEntity`, and `OfferEntity` all use `CascadeType.ALL` with `orphanRemoval = true` on their child collections
- **Lazy loading**: All `@ManyToOne` relationships use `FetchType.LAZY`
- **API docs**: Swagger/OpenAPI annotations (`@Schema`, `@Tag`) are present for auto-generated documentation
- **Entities without full CRUD yet**: `SupplierEntity`, `OfferEntity`, `RedemptionEntity`, `Category`, `BuildingSupplierEntity` have entity definitions but no corresponding service/controller implementations
