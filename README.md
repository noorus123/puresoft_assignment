***

# Airline Seat Reservation System

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green) ![Docker](https://img.shields.io/badge/Docker-Enabled-blue) ![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

## Overview

This project implements a simplified Airline Seat Reservation System designed using production-oriented architectural principles.

The system demonstrates:
*   **Modular monolithic architecture**
*   **Event-driven internal communication**
*   **Concurrency-safe seat booking**
*   **In-memory caching**
*   **Docker-based deployment**
*   **CI pipeline with automated image publishing**

---

## Architecture

The backend follows a modular monolith design with clearly separated domain modules:

| Module | Responsibility |
| :--- | :--- |
| **Inventory** | Seat availability & locking |
| **Booking** | Booking lifecycle orchestration |
| **Payment** | Asynchronous payment processing |

Components communicate using **Spring Application Events**, enabling event-driven behavior without external message brokers.

### Architectural Characteristics
*   Synchronous seat locking
*   Asynchronous payment confirmation
*   Optimistic locking for concurrency control
*   Transactional consistency
*   REST-based API design
*   Container-first deployment

---

## Booking Flow

The following flow guarantees seat consistency under concurrent booking attempts:

```text
Client
   │
   ▼
Seat Lock (synchronous)
   │
   ▼
Booking Created (PENDING_PAYMENT)
   │
   ▼
Payment Event Triggered (async)
   │
   ├── Success → CONFIRMED + Seat BOOKED
   └── Failure → FAILED + Seat Released
```

---

## Concurrency Strategy

To prevent double-booking, the `Seat` entity utilizes optimistic locking via JPA:

```java
@Version
private Long version;
```

**Scenario:** If two users attempt to book the same seat:
1.  First transaction commits successfully.
2.  Second transaction fails due to a version conflict (`OptimisticLockException`).
3.  The seat remains consistent.

---

## Caching Strategy

Flight search results are cached using **Caffeine** (in-memory cache) to simulate production-style read optimization without external dependencies.

| Setting | Value |
| :--- | :--- |
| **TTL** | 10 minutes |
| **Max Entries** | 100 |
| **Eviction** | LRU (Least Recently Used) |

---

## Technology Stack

### Backend
*   Java 17
*   Spring Boot 3
*   Spring Data JPA
*   Hibernate
*   H2 Database
*   Caffeine Cache
*   JUnit 5 / Mockito

### DevOps
*   Docker (multi-stage build)
*   Docker Compose
*   GitHub Actions
*   DockerHub

### Frontend (Separate Module)
*   Flutter

---

## Project Structure

```text
puresoft_assignment/
│
├── .github/workflows/      # CI pipeline
├── airline/                # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── airline_app/            # Flutter client
└── docker-compose.yml
```

---

## Getting Started

### Running the Backend

Navigate to the `airline` directory:

```bash
mvn clean install
mvn spring-boot:run
```

**Access H2 Console:**
URL: `http://localhost:8080/h2-console`

### Docker Deployment

**Build the image:**
```bash
docker build -t nooruskhan/airline_backend:latest .
```

**Run the container:**
```bash
docker run -p 8080:8080 nooruskhan/airline_backend:latest
```

---

## CI Pipeline

The GitHub Actions workflow performs the following steps automatically:
1.  Maven build & test
2.  Docker image build
3.  DockerHub push

**Published Image:**
[`nooruskhan/airline_backend:latest`](https://hub.docker.com/r/nooruskhan/airline_backend)

---

## Design Decisions

*   **Modular Monolith:** Chosen over microservices for scope clarity and ease of development while maintaining separation of concerns.
*   **Internal Event-Driven Design:** Ensures loose coupling between the Booking and Payment modules.
*   **Optimistic Locking:** Selected for strict concurrency correctness to ensure no two users book the same seat.
*   **In-Memory Cache:** Used to simulate production read scaling.
*   **Containerization:** The application was containerized from inception for consistent deployment environments.
