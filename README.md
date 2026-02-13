Airline Seat Reservation System

Overview

This project implements a simplified Airline Seat Reservation System designed using production-oriented architectural principles.

The system demonstrates:

Modular monolithic architecture

Event-driven internal communication

Concurrency-safe seat booking

In-memory caching

Docker-based deployment

CI pipeline with automated image publishing

Architecture

The backend follows a modular monolith design with clearly separated domain modules:

Module	Responsibility
Inventory	Seat availability & locking
Booking	Booking lifecycle orchestration
Payment	Asynchronous payment processing

Components communicate using Spring Application Events, enabling event-driven behavior without external message brokers.

Architectural Characteristics

Synchronous seat locking

Asynchronous payment confirmation

Optimistic locking for concurrency control

Transactional consistency

REST-based API design

Container-first deployment

Booking Flow
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


This guarantees seat consistency under concurrent booking attempts.

Concurrency Strategy

Seat entity includes:

@Version
private Long version;


If two users attempt to book the same seat:

First transaction commits successfully

Second transaction fails due to version conflict

Seat remains consistent

This prevents double-booking.

Caching Strategy

Flight search results are cached using Caffeine (in-memory cache).

Setting	Value
TTL	10 minutes
Max Entries	100
Eviction	LRU

This simulates production-style read optimization without external dependencies.

Technology Stack
Backend

Java 17

Spring Boot 3

Spring Data JPA

Hibernate

H2 Database

Caffeine Cache

JUnit 5 / Mockito

DevOps

Docker (multi-stage build)

Docker Compose

GitHub Actions

DockerHub

Frontend (separate module)

Flutter

Project Structure
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

Running the Backend

From the airline directory:

mvn clean install
mvn spring-boot:run


H2 console:

http://localhost:8080/h2-console

Docker Deployment

Build image:

docker build -t nooruskhan/airline_backend:latest .


Run container:

docker run -p 8080:8080 nooruskhan/airline_backend:latest

CI Pipeline

The GitHub Actions workflow performs:

Maven build & test

Docker image build

DockerHub push

Published image:

nooruskhan/airline_backend:latest

Design Decisions

Modular monolith chosen over microservices for scope clarity

Internal event-driven design for loose coupling

Optimistic locking for concurrency correctness

In-memory cache to simulate production read scaling

Containerized from inception
