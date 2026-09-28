# PetCareBook — Complete Project Documentation

> An end-to-end reference for the PetCareBook clinical vaccination-tracking system:
> features, files, code flow, database, and how to run.

**Location:** `C:\test_project\project`
**Runtime:** Spring Boot 4.1.1 · Java 21 · H2 (default) / MySQL (optional)
**Frontend:** Vanilla HTML / CSS / JavaScript (no framework, no build step)

---

## Table of Contents

1. [What the App Does (Features)](#1-what-the-app-does-features)
2. [Tech Stack](#2-tech-stack)
3. [High-Level Architecture](#3-high-level-architecture)
4. [Database Architecture](#4-database-architecture)
5. [Request Lifecycle — worked example](#5-request-lifecycle--worked-example)
6. [File-by-File Walkthrough](#6-file-by-file-walkthrough)
7. [How to Run It](#7-how-to-run-it)
8. [Key Design Ideas](#8-key-design-ideas)

---

## 1. What the App Does (Features)

PetCareBook is a **veterinary clinic vaccination-tracking system**. A vet or clinic manager uses it to:

| Feature | Where |
|---|---|
| See a **clinical dashboard** with KPIs (active patients, clients, due-soon, overdue, total doses) + recent activity + upcoming schedule | `index.html` |
| Register **clients (pet owners)** with name / email / phone / address; view their linked pets | `owners.html` |
| Register **patients (pets)** with species, breed, DOB, gender, weight, owner; search/filter by species | `pets.html` |
| **Administer a vaccination** — pick pet + vaccine, enter admin date, batch #, notes; the app **auto-computes the next due date** from the vaccine's standard interval | `vaccinate.html` |
| See what's **due in the next 7 (or N) days** and what's **overdue** | `due-soon.html` |
| View the full **vaccination history / ledger** per pet or across the clinic | `history.html` |
| Manage the **vaccine catalog** (Rabies, DHPP, FVRCP, etc.) — name, target species, standard interval days, mandatory/elective | `vaccines.html` |
| Delete records safely; get validation errors as toasts; graceful offline fallback if backend is down | frontend + `GlobalExceptionHandler` |
| **Live status computation**: every record's `daysRemainingUntilDue` and `OVERDUE / DUE_SOON / UP_TO_DATE` is recomputed at query time — never stored stale | `VaccinationRecordService.mapToResponse` |

---

## 2. Tech Stack

- **Backend**: Java 21, Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA (Hibernate), Bean Validation (Jakarta), Lombok
- **Database**: H2 in-memory by default (zero setup); MySQL config included but commented in `application.properties`
- **Frontend**: Vanilla HTML5 + CSS3 + JavaScript. Single stylesheet + shared JS client.
- **Build**: Maven (with wrapper `mvnw` / `mvnw.cmd`)
- **Tests**: JUnit 5 via `spring-boot-starter-*-test`

---

## 3. High-Level Architecture

```
Browser (HTML / CSS / JS)
        │  HTTP JSON  (fetch → /api/*)
        ▼
┌───────────────────────────────────────────────┐
│  Controllers  (REST — @RestController)         │  ← Owner, Pet, VaccineType, VaccinationRecord, Dashboard
└───────────────────────────────────────────────┘
        │  DTOs in / DTOs out
        ▼
┌───────────────────────────────────────────────┐
│  Services  (business rules + @Transactional)   │  ← 5 services matching the controllers
└───────────────────────────────────────────────┘
        │  entities
        ▼
┌───────────────────────────────────────────────┐
│  Repositories  (Spring Data JPA)               │  ← findBy…, countBy…, custom @Query
└───────────────────────────────────────────────┘
        │  JPA / Hibernate
        ▼
┌───────────────────────────────────────────────┐
│  Database  (H2 in-memory  |  MySQL)            │  ← 4 tables
└───────────────────────────────────────────────┘

Cross-cutting:
  • GlobalExceptionHandler  (@RestControllerAdvice)
  • DataInitializer         (CommandLineRunner — seeds demo data at startup)
```

Every request flows: **Browser → Controller → Service → Repository → DB**, and back up as a DTO the browser can render.

---

## 4. Database Architecture

### 4.1 Tables (created by JPA from the entity classes)

**`owners`** — clinic clients

| column | type | notes |
|---|---|---|
| `id` | BIGINT PK | auto-increment |
| `full_name` | VARCHAR NOT NULL | |
| `email` | VARCHAR NOT NULL **UNIQUE** | |
| `phone_number` | VARCHAR NOT NULL | |
| `address` | VARCHAR | nullable |
| `created_at` | TIMESTAMP NOT NULL | set by `@PrePersist` |

**`pets`** — patients

| column | type | notes |
|---|---|---|
| `id` | BIGINT PK | |
| `name`, `species`, `breed` | VARCHAR NOT NULL | |
| `date_of_birth` | DATE NOT NULL | `@PastOrPresent` |
| `gender`, `weight` | VARCHAR / DOUBLE | nullable |
| `owner_id` | BIGINT NOT NULL FK → `owners(id)` | |
| `created_at` | TIMESTAMP NOT NULL | |

**`vaccine_types`** — the catalog

| column | type | notes |
|---|---|---|
| `id` | BIGINT PK | |
| `name` | VARCHAR NOT NULL **UNIQUE** | |
| `target_species` | VARCHAR NOT NULL | `"Dog"` / `"Cat"` / `"All"` |
| `description` | VARCHAR | |
| `standard_interval_days` | INT NOT NULL > 0 | drives auto next-due-date |
| `mandatory` | BOOLEAN default true | |
| `created_at` | TIMESTAMP NOT NULL | |

**`vaccination_records`** — the ledger

| column | type | notes |
|---|---|---|
| `id` | BIGINT PK | |
| `pet_id` | BIGINT NOT NULL FK → `pets(id)` | |
| `vaccine_type_id` | BIGINT NOT NULL FK → `vaccine_types(id)` | |
| `date_administered` | DATE NOT NULL | `@PastOrPresent` |
| `next_due_date` | DATE NOT NULL | **computed = `dateAdministered + standardIntervalDays`** |
| `administered_by` | VARCHAR | vet name / clinic |
| `batch_number` | VARCHAR | |
| `notes` | VARCHAR(1000) | |
| `created_at` | TIMESTAMP NOT NULL | |

### 4.2 Relationships

```
owners  1 ────< pets  1 ────< vaccination_records >──── 1  vaccine_types
        ManyToOne          ManyToOne                    ManyToOne
        (cascade)          (cascade + orphanRemoval)
```

- Delete an owner → cascades to their pets → cascades to those pets' vaccination records.
- Delete a pet → cascades to its vaccination records only.
- Delete a vaccine_type → blocked if records reference it (no cascade defined that way).

### 4.3 How the schema gets created

`application.properties` has `spring.jpa.hibernate.ddl-auto=update`. On startup Hibernate **inspects the `@Entity` classes and issues `CREATE TABLE` for anything missing** or `ALTER TABLE` for additions. No SQL migration files to write.

---

## 5. Request Lifecycle — worked example

Let's trace **"click Administer Booster"** — the highest-value flow because it exercises every layer plus business rules.

1. `vaccinate.html` submits `POST /api/vaccinations` with JSON:
   ```json
   { "petId": 1, "vaccineTypeId": 2, "dateAdministered": "2026-09-28",
     "batchNumber": "DHPP-2025-081", "notes": "…", "administeredBy": "Green Valley" }
   ```
2. `VaccinationRecordController.logVaccination(@Valid @RequestBody …)` — Spring MVC deserializes to `VaccinationRecordRequestDto` and runs Bean Validation (`@NotNull`, `@PastOrPresent`, …). If it fails, `GlobalExceptionHandler.handleValidationExceptions` returns `400` with a `validationErrors` map.
3. Controller delegates to `VaccinationRecordService.logVaccination()`. This is `@Transactional`, so DB reads/writes happen in one txn.
4. **Business rules** (`VaccinationRecordService.java:29-75`):
   - `dateAdministered` must not be in the future → `BusinessValidationException`
   - Pet is loaded via `PetService.getPetEntity(petId)` → `ResourceNotFoundException` if bad ID
   - Vaccination date must not be **before the pet's DOB**
   - Vaccine type loaded (by ID or by name)
   - Vaccine's `standardIntervalDays` must be present and positive
5. **Auto-computation**:
   ```java
   nextDueDate = dateAdministered.plusDays(vaccineType.getStandardIntervalDays());
   ```
   This is the core clinical logic that makes the app useful.
6. `VaccinationRecord` is built (Lombok `@Builder`), saved via `vaccinationRecordRepository.save(...)`. Hibernate issues `INSERT`, `@PrePersist` fills `createdAt`.
7. Service calls `mapToResponse(saved)` — this is where the **status** (`"OVERDUE"` / `"DUE_SOON"` / `"UP_TO_DATE"`) and `daysRemainingUntilDue` are computed **on read**, not stored. That's why the badge updates as time passes without a batch job.
8. Controller returns `201 CREATED` with the response DTO.
9. Frontend `vaccinate.html` fires `showToast('Immunization logged. Next booster scheduled for …')` and redirects.

---

## 6. File-by-File Walkthrough

Grouped by layer. Path prefix `src/main/java/com/example/project/` unless noted.

### 6.1 Entry point
- **`ProjectApplication.java`** — `@SpringBootApplication` main. Boots Tomcat on port 8080, scans this package, wires everything.

### 6.2 Entities (`entity/`) — JPA classes = table rows
- **`Owner.java`** — `owners` table. Owns a `List<Pet>` (`@OneToMany mappedBy="owner"`, cascade + orphanRemoval). `@JsonIgnore` on the pets list stops recursive JSON. `@PrePersist` sets `createdAt`.
- **`Pet.java`** — `pets` table. Belongs to one `Owner` (`@ManyToOne` EAGER — join fetched with pet). Owns a `List<VaccinationRecord>` (`@OneToMany`, cascade + orphanRemoval, `@JsonIgnore`).
- **`VaccineType.java`** — `vaccine_types` table. `name` is `@Column(unique=true)`. `standardIntervalDays` is the numeric interval that later drives due-date math.
- **`VaccinationRecord.java`** — `vaccination_records` table. `@ManyToOne` to both Pet and VaccineType, both EAGER. Both `dateAdministered` and `nextDueDate` are stored `LocalDate`s.

### 6.3 Repositories (`repository/`) — Spring Data interfaces (no implementation code)
- **`OwnerRepository`** — CRUD + `findByEmail` for uniqueness checks.
- **`PetRepository`** — `findByOwnerId`, `findBySpeciesIgnoreCase`, and a `@Query` `searchByNameOrBreed` using JPQL `LIKE`.
- **`VaccineTypeRepository`** — CRUD + `findByName`, filter by target species.
- **`VaccinationRecordRepository`** — the interesting one:
  - `findByPetIdOrderByDateAdministeredDesc` — history for a pet
  - `findByNextDueDateBetweenOrderByNextDueDateAsc(start, end)` — powers "due in 7 days"
  - `findByNextDueDateBeforeOrderByNextDueDateAsc(today)` — overdue list
  - `countByNextDueDateBetween` / `countByNextDueDateBefore` — dashboard KPIs (cheap counts, no result list)
  - `@Query` for distinct pet IDs due in a window

### 6.4 DTOs (`dto/`) — the wire format between browser and backend
Two per entity: **Request DTO** (what you POST/PUT — validated fields only) and **Response DTO** (what you GET — flattened with denormalized names like `ownerName`, `vaccineName`, `species` so the frontend doesn't need extra fetches). Plus `DashboardStatsDto` for the KPI payload.

**Key point:** the response DTO for a vaccination record includes **computed** `status` and `daysRemainingUntilDue` — not columns in the DB.

### 6.5 Services (`service/`) — business logic + transactions
- **`OwnerService`** — CRUD + duplicate-email check (throws `DuplicateResourceException`).
- **`PetService`** — CRUD, filter, search, `getPetEntity(id)` helper used by other services.
- **`VaccineTypeService`** — CRUD, lookup by name, positive-interval enforcement.
- **`VaccinationRecordService`** — the heart of the app. Rules covered above. Also `mapToResponse` (line 131) — this is where **status** and **daysRemainingUntilDue** get computed on every read using `LocalDate.now()`.
- **`DashboardService`** — one method: fires 6 cheap `count(...)` queries and packs them into `DashboardStatsDto`. Nothing more.

### 6.6 Controllers (`controller/`) — thin, transport-only
Each is `@RestController @RequestMapping("/api/…") @CrossOrigin(origins="*")`. All they do is bind HTTP → service call → HTTP.

| Controller | Base path | Endpoints |
|---|---|---|
| `OwnerController` | `/api/owners` | CRUD |
| `PetController` | `/api/pets` | CRUD + `?species=`, `?search=`, `?ownerId=` |
| `VaccineTypeController` | `/api/vaccine-types` | CRUD + `?species=` |
| `VaccinationRecordController` | `/api/vaccinations` | POST log, GET all, GET `/due-soon?days=N`, GET `/overdue`, GET `/{id}`, DELETE `/{id}` |
| `DashboardController` | `/api/dashboard/stats` | 6-metric KPI payload |

### 6.7 Exceptions (`exception/`) — clean error contracts
- Three typed exceptions: `ResourceNotFoundException` (→404), `BusinessValidationException` (→400), `DuplicateResourceException` (→409).
- `ErrorResponse` — the JSON shape `{timestamp, status, error, message, path, validationErrors}`.
- **`GlobalExceptionHandler`** (`@RestControllerAdvice`) — one `@ExceptionHandler` per exception type + one for `MethodArgumentNotValidException` (Bean Validation failures → 400 with a field→message map) + a catch-all `Exception` handler → 500. Frontend's `apiFetch` reads `.message` and `.validationErrors` to build the toast text.

### 6.8 Config (`config/`)
- **`DataInitializer`** (`CommandLineRunner`) — runs once at startup. If `vaccine_types` is empty, seeds: **6 vaccine types, 3 owners, 5 pets, 6 vaccination records** (mix of overdue, due-soon, and up-to-date so the dashboard has content). Dates are computed as `LocalDate.now().minusDays(…)` so the demo is always "current" relative to today.

### 6.9 `application.properties`
- H2 in-memory URL (`jdbc:h2:mem:petcaredb`), driver, dialect
- `ddl-auto=update` (Hibernate manages schema)
- H2 console enabled at `/h2-console`
- Static resources served from `classpath:/static/`
- MySQL block commented — flip it on for persistence

### 6.10 Frontend (`src/main/resources/static/`)

**Pages** (7, each is a self-contained HTML file that includes `css/styles.css` and `js/common.js`)

| Page | Purpose |
|---|---|
| `index.html` | Dashboard: KPIs, urgent notice banner, 7-day due table, recent activity table |
| `pets.html` | Patients list/grid with species tabs + search + register modal |
| `owners.html` | Clients grid + register modal + linked-pets viewer |
| `vaccinate.html` | Form: pick pet → pick vaccine → date → live "next due" preview (`dateAdministered + standardIntervalDays`) |
| `due-soon.html` | Tabs: 7-day due / overdue / 30-day due, each hits the corresponding backend endpoint |
| `history.html` | Full ledger; can filter to one pet via `?petId=…` |
| `vaccines.html` | Catalog with species tabs + create modal |

**`css/styles.css`** — one design system: nav, buttons, panels, tables, KPI tiles, modals, toasts. Uses fluid `clamp()` sizing and CSS custom properties so it scales across laptop widths.

**`js/common.js`** — shared client:
- `apiFetch(endpoint, options)` — the fetch wrapper. Adds JSON headers, parses errors from `ErrorResponse`, throws with `.status` and rich message. **If the backend is unreachable, it falls back to an embedded in-memory `FALLBACK_STORE`** so the pages still work standalone (great for demos). This is why the frontend has an "offline mode" banner path.
- `renderNavbar(activePage)` — dynamically highlights the active tab and updates the SCHEDULE badge count from `/api/dashboard/stats`.
- `renderFooter()`, `showToast()`, `escapeHtml()`, `formatDate()`, `getDueStatusBadge()`, `openModal()`, `closeModal()` — utilities.

### 6.11 Launchers
- **`run.bat`** / **`run.ps1`** — wrap `mvnw spring-boot:run` so you can double-click to launch.

### 6.12 Tests (`src/test/java/…`)
- **`ProjectApplicationTests`** — Spring context loads (a smoke test).
- **`VaccinationRecordServiceTest`** — 4 unit tests on business rules (future date rejection, DOB precedence, interval-based next-due-date math, delete-not-found).
- **`PetCareIntegrationTests`** — end-to-end with `@SpringBootTest` + real H2, exercising HTTP endpoints.

---

## 7. How to Run It

```powershell
# from C:\test_project\project
.\run.ps1
```

- App boots at **http://localhost:8080** (dashboard)
- H2 admin console at **http://localhost:8080/h2-console**
  - JDBC URL: `jdbc:h2:mem:petcaredb`
  - User: `sa`
  - Password: *(blank)*
- REST base at **http://localhost:8080/api/…**

To switch to MySQL: comment the H2 block in `application.properties` and uncomment the MySQL block below it — no code changes needed.

---

## 8. Key Design Ideas

One line each — the choices that make the codebase work the way it does.

1. **Next due date is stored** (`nextDueDate` column) so DB queries `WHERE next_due_date BETWEEN today AND today+7` are fast — no computation in SQL.
2. **Status is computed on read**, never stored, so `"OVERDUE"` turns on automatically at midnight without a scheduled job.
3. **DTOs are denormalized on the way out** — every vaccination-record response includes `petName`, `ownerName`, `vaccineName`, etc., so the frontend renders a table in one round-trip.
4. **Cascade rules mirror the real world**: deleting an owner deletes their pets deletes their records; deleting a vaccine type is blocked if it's referenced (protects the ledger).
5. **The frontend degrades gracefully** — if `run.ps1` isn't running, the pages still work off `FALLBACK_STORE` and show a banner.

---

*End of document.*
