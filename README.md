# Reservation System — Backend

A RESTful backend for managing classroom and laboratory reservations at a university campus. Built with **Java 17**, **Spring Boot 3**, and **MongoDB Atlas**, it exposes a clean API consumed by the institutional front-end application.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Target Audience & Use Cases](#target-audience--use-cases)
3. [Key Features](#key-features)
4. [Architecture](#architecture)
5. [Setup & Installation](#setup--installation)
6. [Environment Variables & Configuration](#environment-variables--configuration)
7. [API Endpoints](#api-endpoints)
8. [Useful Scripts](#useful-scripts)
9. [Testing](#testing)
10. [Deployment](#deployment)
11. [CI/CD Pipeline](#cicd-pipeline)
12. [Contributing](#contributing)
13. [License](#license)

---

## Project Overview

The **Reservation System Backend** provides a centralized service for booking university laboratories and classrooms. It eliminates scheduling conflicts by enforcing business rules at the API layer: a lab slot can only be reserved once per date and time block, and laboratories must exist before they can be booked.

**Value proposition:**
- Prevents double-booking and scheduling conflicts automatically.
- Offers real-time availability checks so users can query a slot before attempting to reserve it.
- Provides a clean, stateless REST API that integrates with any front-end technology.
- Designed for continuous deployment to Azure App Service via GitHub Actions.

---

## Target Audience & Use Cases

| Audience | Use Case |
|---|---|
| Students | Book a laboratory for a study session or project work |
| Professors | Reserve a lab for a class or experiment |
| Administrators | Register new laboratories and monitor all existing reservations |
| Front-end / Mobile clients | Consume the REST API to display availability calendars |

---

## Key Features

- **Laboratory management** — register laboratories with a name and building block; duplicates are rejected with a conflict response.
- **Reservation creation** — create a reservation for a specific lab, date, and time slot; the service validates that the lab exists and the slot is free.
- **Availability check** — query whether a specific lab is free on a given date and time before submitting a reservation.
- **Reservation retrieval** — fetch a single reservation by ID or list all reservations in the system.
- **Reservation cancellation** — delete an existing reservation by ID; returns a descriptive error when the ID is not found.
- **Structured error handling** — global exception handlers return meaningful HTTP status codes (`404 Not Found`, `409 Conflict`, `500 Internal Server Error`).
- **CORS support** — cross-origin requests are enabled on reservation endpoints, allowing any front-end origin to call the API.

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      REST API Layer                         │
│   ReservationController    │    LaboratoryController        │
│   /api/reservations        │    /api/laboratories           │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                     Service Layer                           │
│   MakeReservationService   │    LaboratoryService           │
│   (business rules &        │    (duplicate check &          │
│    availability checks)    │     lab management)            │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                  Repository Layer (Spring Data MongoDB)      │
│   ReservationRepository  │  LaboratoryRepository            │
│   HourRangeRepository                                        │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                      MongoDB Atlas                          │
│   Collections: reservations, laboratories, hourranges       │
└─────────────────────────────────────────────────────────────┘
```

**Package structure:**

| Package | Responsibility |
|---|---|
| `controller` | REST endpoints — maps HTTP verbs to service calls |
| `servicios` | Business logic and validation rules |
| `mongoConnection` | Spring Data MongoDB repository interfaces |
| `model` | Document and domain models (`Reservation`, `Laboratory`, `User`, `HoursRange`) |
| `exception` | Custom exceptions (`ReservationNotFoundException`) |

### Non-Functional Requirements

#### Security

- **Credential management** — the MongoDB connection URI must be supplied through environment variables or Azure App Service application settings; it must never be hardcoded in source-controlled files. The `application.properties` file in the repository should reference `${MONGODB_URI}` (see [Environment Variables](#environment-variables--configuration)).
- **CORS policy** — cross-origin requests are currently allowed from all origins (`*`). For production deployments, restrict the allowed origins to the known front-end domain via Spring Security's CORS configuration.
- **Input validation** — the service layer enforces business rules (no duplicate labs, no double-booking) before any write reaches the database, preventing data-integrity issues.
- **Transport security** — deployments on Azure App Service run behind HTTPS by default, ensuring all data in transit is encrypted with TLS.

#### High Availability

- **Stateless design** — the API holds no server-side session state, so multiple instances can be scaled horizontally behind a load balancer without sticky sessions.
- **MongoDB Atlas** — the database tier uses Atlas's managed replica-set clusters, providing automatic failover and data redundancy.
- **Azure App Service** — the production deployment target supports auto-scaling rules and health probes, enabling the application to remain available during traffic spikes and rolling deployments.
- **Continuous Deployment** — the GitHub Actions pipeline automatically deploys a freshly built and tested artifact to the Production slot on every push to `main`, minimising manual deployment risk and downtime.

---

## Setup & Installation

### Prerequisites

| Tool | Minimum Version |
|---|---|
| Java (JDK) | 17 |
| Apache Maven | 3.9+ (or use the included `mvnw` wrapper) |
| MongoDB | Atlas cluster or local instance |
| Git | Any recent version |

### Clone the repository

```bash
git clone https://github.com/Juanvelandia-p/Reservation-system-back-.git
cd Reservation-system-back-
```

### Configure the database connection

Copy your MongoDB connection URI into a local environment variable (see [Environment Variables](#environment-variables--configuration)).

### Build the project

```bash
# Using the Maven wrapper (recommended)
./mvnw clean install          # Linux / macOS
mvnw.cmd clean install        # Windows

# Or, if Maven is installed globally
mvn clean install
```

### Run locally

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Environment Variables & Configuration

The application reads its configuration from `src/main/resources/application.properties`. Sensitive values **must** be supplied through environment variables and must not be committed to version control.

| Property key | Environment variable | Description | Example value |
|---|---|---|---|
| `spring.data.mongodb.uri` | `MONGODB_URI` | Full MongoDB connection string | `mongodb+srv://user:pass@cluster.mongodb.net/ReservationSystem` |
| `spring.application.name` | — | Application name (informational) | `ReservationSystem` |

**`application.properties` (safe template):**

```properties
spring.application.name=ReservationSystem
spring.data.mongodb.uri=${MONGODB_URI}
```

When running locally, set the variable in your shell before starting the application:

```bash
# Linux / macOS
export MONGODB_URI="mongodb+srv://<user>:<password>@<cluster>.mongodb.net/ReservationSystem?retryWrites=true&w=majority"
./mvnw spring-boot:run

# Windows (PowerShell)
$env:MONGODB_URI="mongodb+srv://<user>:<password>@<cluster>.mongodb.net/ReservationSystem?retryWrites=true&w=majority"
.\mvnw.cmd spring-boot:run
```

In Azure App Service, add the variable under **Settings → Environment variables** (Application settings).

---

## API Endpoints

Base URL: `http://localhost:8080` (local) | `https://reservationsystem.azurewebsites.net` (production)

### Laboratories — `/api/laboratories`

#### `POST /api/laboratories` — Register a laboratory

Creates a new laboratory. Returns `409 Conflict` if a laboratory with the same name and block already exists.

**Request body:**
```json
{
  "name": "Lab-A",
  "block": "B"
}
```

**Response `201 Created`:**
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "name": "Lab-A",
  "block": "B"
}
```

---

#### `GET /api/laboratories` — List all laboratories

**Response `200 OK`:**
```json
[
  { "id": "64f1a2b3c4d5e6f7a8b9c0d1", "name": "Lab-A", "block": "B" },
  { "id": "64f1a2b3c4d5e6f7a8b9c0d2", "name": "Lab-B", "block": "C" }
]
```

---

### Reservations — `/api/reservations`

#### `POST /api/reservations` — Create a reservation

Returns `409 Conflict` if the lab is already booked for the specified date and time.

**Request body:**
```json
{
  "lab": "Lab-A",
  "reserveDate": "2025-06-15",
  "reserveTime": "08:00",
  "userName": "john.doe"
}
```

**Response `200 OK`:**
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d3",
  "lab": "Lab-A",
  "reserveDate": "2025-06-15",
  "reserveTime": "08:00",
  "userName": "john.doe"
}
```

---

#### `GET /api/reservations/all` — List all reservations

**Response `200 OK`:** array of reservation objects (same shape as above).

---

#### `GET /api/reservations` — Get reservation(s)

- Without query parameter: returns all reservations (same as `/all`).
- With `?id=<reservationId>`: returns a single reservation or `404 Not Found`.

**Example:** `GET /api/reservations?id=64f1a2b3c4d5e6f7a8b9c0d3`

**Response `200 OK`:**
```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d3",
  "lab": "Lab-A",
  "reserveDate": "2025-06-15",
  "reserveTime": "08:00",
  "userName": "john.doe"
}
```

---

#### `DELETE /api/reservations?id=<reservationId>` — Cancel a reservation

Returns `404 Not Found` when the ID does not exist.

**Example:** `DELETE /api/reservations?id=64f1a2b3c4d5e6f7a8b9c0d3`

**Response `200 OK`:** `Reserva eliminada exitosamente`

---

#### `GET /api/reservations/availability` — Check availability

Returns `true` if the lab is available (not reserved) or `false` if it is already booked.

**Query parameters:**

| Parameter | Type | Format | Description |
|---|---|---|---|
| `labName` | string | — | Laboratory name |
| `block` | string | — | Building block |
| `date` | string | ISO 8601 (`YYYY-MM-DD`) | Date to check |
| `time` | string | `HH:mm` | Time slot to check |

**Example:** `GET /api/reservations/availability?labName=Lab-A&block=B&date=2025-06-15&time=08:00`

**Response `200 OK`:** `true`

---

### HTTP Status Code Summary

| Code | Meaning |
|---|---|
| `200 OK` | Successful retrieval or operation |
| `201 Created` | Laboratory successfully registered |
| `404 Not Found` | Reservation ID does not exist |
| `409 Conflict` | Duplicate laboratory or double-booking attempt |
| `500 Internal Server Error` | Unexpected server error |

---

## Useful Scripts

The repository includes the Maven wrapper so no separate Maven installation is required.

| Script | Platform | Description |
|---|---|---|
| `./mvnw clean install` | Linux / macOS | Build the project and run all tests |
| `./mvnw spring-boot:run` | Linux / macOS | Start the application locally |
| `./mvnw test` | Linux / macOS | Run the test suite only |
| `./mvnw verify` | Linux / macOS | Run tests and generate JaCoCo coverage report |
| `mvnw.cmd clean install` | Windows | Build the project and run all tests |
| `mvnw.cmd spring-boot:run` | Windows | Start the application locally |

---

## Testing

The project uses **JUnit Jupiter** and **Mockito** for unit testing, and **JaCoCo** for code coverage reporting.

**Test packages:**

| Package | Scope |
|---|---|
| `modelTests` | Unit tests for domain models (`Reservation`, `Laboratory`, `User`, `HoursRange`) |
| `serviceTests` | Unit tests for service layer using Mockito mocks for repositories |
| `controllerTests` | Unit tests for REST controllers |

### Run the tests

```bash
./mvnw test                 # Run all tests
./mvnw verify               # Run tests + generate JaCoCo HTML coverage report
```

After running `mvnw verify`, the coverage report is available at:

```
target/site/jacoco/index.html
```

---

## Deployment

The application is deployed to **Azure App Service** as a JAR artifact.

### Manual deployment steps

1. Build the artifact:
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. The JAR is produced at `target/ReservationSystem-0.0.1-SNAPSHOT.jar`.
3. Deploy to Azure App Service using the Azure CLI or the Azure portal.
4. Set the `MONGODB_URI` application setting in **Azure App Service → Settings → Environment variables**.

Automated deployment is handled by the CI/CD pipeline described below.

---

## CI/CD Pipeline

The repository uses **GitHub Actions** to automate build, test, and deployment on every push to the `main` branch.

**Workflow file:** `.github/workflows/main_reservationsystem.yml`

### Trigger

| Event | Condition |
|---|---|
| `push` | Any push to the `main` branch |
| `workflow_dispatch` | Manual trigger from the GitHub Actions UI |

### Pipeline stages

```
push to main
    │
    ▼
┌─────────────────────────────────────────┐
│  build  (windows-latest, Java 17)       │
│  1. actions/checkout@v4                 │
│  2. actions/setup-java@v4               │
│     (Java 17, Microsoft distribution)   │
│  3. mvn clean install                   │
│     (compiles, runs tests, JaCoCo)      │
│  4. upload JAR artifact                 │
└──────────────────┬──────────────────────┘
                   │ artifact: java-app
                   ▼
┌─────────────────────────────────────────┐
│  deploy  (windows-latest)               │
│  1. download artifact                   │
│  2. azure/login@v2 (OIDC / JWT)         │
│  3. azure/webapps-deploy@v3             │
│     app: ReservationSystem              │
│     slot: Production                    │
└─────────────────────────────────────────┘
```

**Key automation points:**

- `mvn clean install` compiles the code, runs the full unit test suite, and enforces JaCoCo coverage thresholds — all in a single step. A build failure aborts deployment automatically.
- Azure authentication uses **OIDC (workload identity federation)** via `azure/login@v2`, so no long-lived service principal credentials are stored as secrets.
- The deployment credentials (`AZUREAPPSERVICE_CLIENTID`, `AZUREAPPSERVICE_TENANTID`, `AZUREAPPSERVICE_SUBSCRIPTIONID`) are stored as **GitHub Actions secrets** and never exposed in the workflow file.
- The `deploy` job only runs if `build` succeeds (`needs: build`), ensuring that a broken build never reaches production.

---

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository and create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. **Code** your changes following the existing package and naming conventions.
3. **Test** your changes: all existing tests must pass and new functionality should include unit tests.
   ```bash
   ./mvnw test
   ```
4. **Commit** using a clear, descriptive message:
   ```bash
   git commit -m "feat: add endpoint to retrieve reservations by user"
   ```
5. **Push** your branch and open a **Pull Request** against `main`.
6. Ensure the GitHub Actions pipeline passes before requesting a review.

### Code style guidelines

- Follow standard Java conventions (camelCase for variables and methods, PascalCase for classes).
- Keep controller methods thin — business logic belongs in the service layer.
- Do not commit secrets or database credentials to the repository.

---

## License

No license file is currently present in this repository. All rights are reserved by the project authors until an open-source license is added. If you intend to use or contribute to this project, please contact the maintainers.
