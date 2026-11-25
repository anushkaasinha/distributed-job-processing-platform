
# Asynchronous Job Processing Platform

A Spring Boot-based backend system for submitting, queuing and
processing background jobs asynchronously using RabbitMQ and MySQL.

The platform separates job submission from job execution, allowing
long-running tasks to be processed in the background while clients
track their execution status through REST APIs.

## Architecture

```text
                 Client
                   |
                   v
             REST API
            (Spring Boot)
                   |
             Job Submission
                   |
                   v
                MySQL
                   |
                   v
               RabbitMQ
                   |
                   v
             Job Consumer
                   |
             Job Execution
                   |
                   v
             Status Update
                   |
                   v
                MySQL
```

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot 3 |
| API | Spring Web |
| Security | Spring Security, JWT |
| Messaging | RabbitMQ |
| Database | MySQL 8 |
| Persistence | Spring Data JPA, Hibernate |
| API Documentation | Swagger / OpenAPI |
| Containerization | Docker, Docker Compose |
| Testing | JUnit 5, Mockito, MockMvc |
| Build Tool | Maven |

## Core Functionality

The project is designed around the following workflow:

1. A client submits a job through a REST API.
2. The application validates the request and persists job details.
3. The job is published to RabbitMQ.
4. A background consumer receives the message.
5. The job is executed asynchronously.
6. The execution status is persisted for subsequent retrieval.

## Engineering Focus

- REST API design and request validation
- Asynchronous communication using RabbitMQ
- Database persistence and job lifecycle management
- Authentication and authorization
- Unit and integration testing
- Containerized application deployment

## Planned Reliability Improvements

- Configurable retry policies for failed jobs
- Dead Letter Queue for messages that exhaust retries
- Idempotent job processing
- Health checks for infrastructure dependencies
- Structured logging for job execution tracing

> The reliability improvements above are a development roadmap,
> not a claim that every feature is currently implemented.

## Running Locally

### Prerequisites

- Java 17+
- Maven
- Docker Desktop
- Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/anushkaasinha/distributed-job-processing-platform.git

cd distributed-job-processing-platform
```

### 2. Configure environment variables

Create a `.env` file using `.env.example` as a reference.

Configure your local database credentials and application settings.

### 3. Start infrastructure

```bash
docker compose up -d mysql rabbitmq
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

### 5. Run tests

```bash
./mvnw test
```

## API Documentation

Swagger UI will be available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The exact API endpoints and request schemas will be documented
here as the API reference is completed.

## Future Enhancements

- Retry and dead-letter handling
- Job execution metrics
- Multiple worker instances
- Container health monitoring
- CI/CD automation

## Author

**Anushka Sinha**

Backend & Cloud Engineering

[GitHub](https://github.com/anushkaasinha)
