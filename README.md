# 🔬 Sistema de Reservas de Laboratorios

![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?logo=mongodb&logoColor=white)
![Azure](https://img.shields.io/badge/Azure-Web%20App-0078D4?logo=microsoftazure&logoColor=white)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?logo=githubactions&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow)

API REST para la gestión de reservas de laboratorios universitarios. Permite a los usuarios registrar laboratorios, hacer reservas, verificar disponibilidad y cancelar reservas, con validaciones de negocio robustas y persistencia en la nube (MongoDB Atlas). El backend está desplegado como servicio en **Azure Web App** mediante un pipeline de CI/CD con GitHub Actions.

---

## 📐 Arquitectura

El sistema sigue una arquitectura en capas (Layered Architecture) desacoplada y fácil de escalar:

```
Client (HTTP)
     │
     ▼
┌──────────────┐
│  Controller  │  ← Capa de presentación (REST endpoints)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Service    │  ← Lógica de negocio y validaciones
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Repository  │  ← Acceso a datos (Spring Data MongoDB)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  MongoDB     │  ← Persistencia (MongoDB Atlas)
│  Atlas       │
└──────────────┘
```

### Diagrama de clases

![Diagrama de diseño](https://github.com/user-attachments/assets/a8674e0a-a475-47c5-b6f0-41a77eea9d39)

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.4.2 | Framework web y de inyección de dependencias |
| Spring Data MongoDB | — | ORM para MongoDB |
| MongoDB Atlas | — | Base de datos NoSQL en la nube |
| Lombok | — | Reducción de código boilerplate |
| JUnit 5 | — | Pruebas unitarias |
| Mockito | — | Mocking en pruebas |
| JaCoCo | 0.8.12 | Reporte de cobertura de código |
| Maven | 3.x | Gestión de dependencias y construcción |
| Azure Web App | — | Plataforma de despliegue en la nube |
| GitHub Actions | — | Integración y entrega continua (CI/CD) |

---

## 📁 Estructura del Proyecto

```
src/
└── main/
│   └── java/edu/eci/cvds/ReservationSystem/
│       ├── controller/
│       │   ├── LaboratoryController.java    # Endpoints de laboratorios
│       │   └── ReservationController.java   # Endpoints de reservas
│       ├── model/
│       │   ├── Laboratory.java              # Entidad laboratorio
│       │   ├── Reservation.java             # Entidad reserva
│       │   ├── User.java                    # Entidad usuario
│       │   └── HoursRange.java              # Rangos horarios disponibles
│       ├── mongoConnection/
│       │   ├── LaboratoryRepository.java    # Repositorio MongoDB
│       │   ├── ReservationRepository.java   # Repositorio MongoDB
│       │   └── HourRangeRepository.java     # Repositorio MongoDB
│       ├── servicios/
│       │   ├── LaboratoryService.java       # Lógica de negocio para laboratorios
│       │   └── MakeReservationService.java  # Lógica de negocio para reservas
│       └── exception/
│           └── ReservationNotFoundException.java  # Excepciones personalizadas
└── test/
    └── java/edu/eci/cvds/ReservationSystem/
        ├── controllerTests/
        ├── modelTests/
        └── serviceTests/
```

---

## 🚀 API REST — Referencia de Endpoints

### Laboratorios · `/api/laboratories`

| Método | Endpoint | Descripción | Código de respuesta |
|---|---|---|---|
| `POST` | `/api/laboratories` | Crea un nuevo laboratorio | `201 Created` / `409 Conflict` |
| `GET` | `/api/laboratories` | Lista todos los laboratorios | `200 OK` |

#### POST `/api/laboratories` — Crear laboratorio

**Request body:**
```json
{
  "name": "Lab Sistemas",
  "block": "Bloque B"
}
```

**Response `201`:**
```json
{
  "id": "65f1a...",
  "name": "Lab Sistemas",
  "block": "Bloque B"
}
```

**Response `409`** si ya existe un laboratorio con el mismo nombre y bloque:
```json
"Ya existe un laboratorio con este nombre y bloque"
```

---

### Reservas · `/api/reservations`

| Método | Endpoint | Descripción | Código de respuesta |
|---|---|---|---|
| `POST` | `/api/reservations` | Crea una nueva reserva | `200 OK` / `409 Conflict` |
| `GET` | `/api/reservations/all` | Lista todas las reservas | `200 OK` |
| `GET` | `/api/reservations?id={id}` | Consulta una reserva por ID | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/reservations?id={id}` | Cancela una reserva | `200 OK` / `404 Not Found` |
| `GET` | `/api/reservations/availability` | Verifica disponibilidad de un laboratorio | `200 OK` |

#### POST `/api/reservations` — Crear reserva

**Request body:**
```json
{
  "lab": "Lab Sistemas",
  "reserveDate": "2025-05-20",
  "reserveTime": "08:00",
  "userName": "Juan Pérez"
}
```

**Response `200`:**
```json
{
  "id": "66a2b...",
  "lab": "Lab Sistemas",
  "reserveDate": "2025-05-20",
  "reserveTime": "08:00",
  "userName": "Juan Pérez"
}
```

**Response `409`** si el laboratorio ya está reservado en esa fecha y hora:
```json
"El laboratorio ya está reservado en esta fecha y hora"
```

#### GET `/api/reservations/availability` — Verificar disponibilidad

**Query params:** `labName`, `block`, `date` (ISO 8601: `yyyy-MM-dd`), `time`

**Ejemplo:**
```
GET /api/reservations/availability?labName=Lab Sistemas&block=Bloque B&date=2025-05-20&time=08:00
```

**Response `200`:**
```json
true   // true = disponible, false = ocupado
```

---

## 🗂️ Modelos de Datos

### Laboratory
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `String` | Identificador único generado por MongoDB |
| `name` | `String` | Nombre del laboratorio |
| `block` | `String` | Bloque o edificio donde se ubica |

### Reservation
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `String` | Identificador único generado por MongoDB |
| `lab` | `String` | Nombre del laboratorio reservado |
| `reserveDate` | `LocalDate` | Fecha de la reserva (ISO 8601) |
| `reserveTime` | `String` | Franja horaria de la reserva |
| `userName` | `String` | Nombre del usuario que realizó la reserva |

### HoursRange
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `String` | Identificador único |
| `aviableHours` | `String` | Horario disponible (ej. `"08:00"`) |

---

## ⚙️ Configuración y Ejecución Local

### Prerrequisitos

- **Java 17** ([Descargar](https://adoptium.net/))
- **Maven 3.8+** ([Descargar](https://maven.apache.org/download.cgi))
- Acceso a **MongoDB Atlas** (o una instancia local de MongoDB)

### 1. Clonar el repositorio

```bash
git clone https://github.com/Juanvelandia-p/Reservation-system-back-.git
cd Reservation-system-back-
```

### 2. Configurar la base de datos

Edita el archivo `src/main/resources/application.properties` con la URI de tu instancia de MongoDB:

```properties
spring.application.name=ReservationSystem
spring.data.mongodb.uri=mongodb+srv://<usuario>:<contraseña>@<cluster>.mongodb.net/ReservationSystem?retryWrites=true&w=majority
```

> **⚠️ Seguridad:** No expongas credenciales en el repositorio. Usa variables de entorno o un servicio de gestión de secretos en producción.

### 3. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La API quedará disponible en: `http://localhost:8080`

---

## 🧪 Pruebas

El proyecto incluye pruebas unitarias para los controladores, servicios y modelos, utilizando **JUnit 5** y **Mockito**.

### Ejecutar pruebas

```bash
mvn test
```

### Reporte de cobertura (JaCoCo)

```bash
mvn verify
```

El reporte HTML se genera en: `target/site/jacoco/index.html`

### Estructura de pruebas

```
src/test/
└── java/edu/eci/cvds/ReservationSystem/
    ├── controllerTests/
    │   ├── LaboratoryControllerTests.java
    │   └── ReservationControllerTests.java
    ├── modelTests/
    │   ├── HoursRangeTests.java
    │   ├── LaboratoryTests.java
    │   ├── ReservationTests.java
    │   └── UserTests.java
    └── serviceTests/
        ├── LaboratoryServiceTests.java
        └── MakeReservationServiceTests.java
```

---

## 🔄 CI/CD — Integración y Despliegue Continuo

El proyecto cuenta con un pipeline automatizado en **GitHub Actions** que se ejecuta en cada push a la rama `main`.

```
Push a main
    │
    ▼
┌─────────────┐
│    Build    │  ← mvn clean install (Java 17, Windows runner)
└──────┬──────┘
       │ Artifact (*.jar)
       ▼
┌─────────────┐
│   Deploy    │  ← Azure Login (OIDC) → Azure Web App Deploy
└─────────────┘
```

El pipeline:
1. **Compila** el proyecto con Maven (`mvn clean install`), ejecutando también las pruebas.
2. **Empaqueta** el artefacto `.jar` resultante.
3. **Autentica** en Azure usando credenciales federadas (OIDC), sin almacenar secretos de larga duración.
4. **Despliega** el `.jar` en el servicio **Azure Web App - ReservationSystem** en el slot de Producción.

---

## ☁️ Despliegue en Azure

La aplicación está desplegada como **Azure Web App (Java SE)** y es accesible públicamente.

- **Plataforma:** Azure App Service
- **Runtime:** Java 17 SE
- **Base de datos:** MongoDB Atlas (conexión segura vía URI con TLS)
- **Slot activo:** Production

---

## 📌 Características Principales

- ✅ **Gestión de laboratorios** — Registro y consulta, con validación de duplicados por nombre y bloque.
- ✅ **Gestión de reservas** — Creación con validaciones de existencia del laboratorio, disponibilidad de horario y conflictos de reserva.
- ✅ **Consulta y cancelación** — Búsqueda de reservas por ID y listado completo; cancelación con verificación previa.
- ✅ **Verificación de disponibilidad** — Consulta puntual por laboratorio, fecha y franja horaria.
- ✅ **Manejo de errores** — Excepciones personalizadas con mensajes claros y códigos HTTP apropiados (`404`, `409`, `500`).
- ✅ **Cobertura de pruebas** — Tests unitarios para controladores, servicios y modelos con JaCoCo.
- ✅ **Despliegue automatizado** — Pipeline de CI/CD completo con GitHub Actions y Azure.

---

## 👨‍💻 Autores

Proyecto desarrollado como parte del curso **Ciclos de Vida del Desarrollo de Software (CVDS)** en la **Escuela Colombiana de Ingeniería Julio Garavito (ECI)**.

