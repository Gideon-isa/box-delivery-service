# Box Delivery Service

A Spring Boot REST API for managing the lifecycle of delivery boxes and
the items assigned to them.

The application is implemented as a Clean
Architecture and Domain-Driven Design (DDD) approach. PostgreSQL is used for
persistence, Flyway owns the database schema, Redis is used where
caching is required, and Keycloak provides authentication and user
identity.

## Technology Stack

-   Java 25
-   Spring Boot 4.1.0
-   Maven
-   Spring Web MVC
-   Spring Data JPA / Hibernate
-   PostgreSQL
-   Flyway
-   Spring Security
-   OAuth 2.0 Resource Server / JWT
-   Keycloak
-   Docker / Docker Compose
-   Swagger / OpenAPI
-   Lombok

## Architecture

The project follows DDD principles.

The domain layer contains the business model and business rules and is
kept independent of Spring and JPA concerns.

## Authentication and Authorization

Keycloak is responsible for:

-   User identity - Pending
-   Password storage - Pending
-   Authentication - Pending
-   Issuing OAuth2/OIDC JWT access tokens

The application is responsible for:

-   Application roles
-   Application permissions
-   Mapping an authenticated Keycloak user to an application role

### Signup

The application exposes a signup endpoint. Signup is intentionally
simple for this assessment:

``` text
POST /signup
       |
       v
Spring Boot
       |
       v
Keycloak Admin API
       |
       +---- creates the Keycloak user
       |
       v
Keycloak user ID
       |
       v
user_role_assignments
       |
       +---- VIEWER
```

Every newly registered user is automatically assigned the default
`VIEWER` application role.

There is deliberately no user-management API in this application.
Keycloak remains the identity provider, while the application's database
stores the user's application-role assignment.

### Swagger Authentication

After signup, authenticate through Swagger UI using the Keycloak client
configured for the application.

The API validates the JWT issued by Keycloak. The JWT subject (`sub`) is
the Keycloak user ID. The application uses that ID to resolve the user's
application role and permissions before authorization decisions are
made.

### Important Keycloak Configuration

The supplied realm export is intended to make the project runnable
without requiring the reviewer to manually create users, clients, or
administrative permissions.

The realm should be imported as:

``` text
Realm: boxdelivery
```

The `Verify Profile` required action should remain disabled. With it
enabled, Keycloak can reject an otherwise valid password authentication
with:

``` text
Account is not fully set up
```

The realm configuration also contains the service-account permissions
required by the application to create users through the Keycloak Admin
API.

## Demo Users

The realm export contains the following demo accounts:

Username          Password          Purpose
  ----------------- ----------------- -----------------------
`demo-admin`      `demo-admin`      Demonstration account
`demo-operator`   `demo-operator`   Demonstration account
`demo-viewer`     `demo-viewer`     Demonstration account

Use a demo account in Swagger's OAuth2 authorization flow after the
infrastructure is running.

## Keycloak Clients

The realm contains the clients required by the application:

### `box-delivery-api`

Confidential client used for API authentication/testing.

### `box-delivery-public`

Public client intended for browser/Swagger-based authentication using
the authorization-code/PKCE flow.

### `box-delivery-admin`

Confidential service-account client used by the Spring Boot application
to call the Keycloak Admin API.

The service account is granted the Keycloak `realm-management`
permissions required to manage users, including:

``` text
manage-users
view-users
```

Do not manually create these clients or roles when using the supplied
realm export.

## Prerequisites

Install the following before running the application:

1.  JDK 25
2.  Maven (or use the Maven wrapper if included in the repository)
3.  Docker Desktop / Docker Engine with Docker Compose
4.  Git

Docker must be running before starting the infrastructure services.

## Clone the Repository

``` bash
git clone https://github.com/Gideon-isa/box-delivery-service
cd box-delivery-service
```

Replace `<https://github.com/Gideon-isa/box-delivery-service>` with the repository URL supplied with the
assessment.

## Start Infrastructure

From the project root:

``` bash
docker compose up -d
```

This starts the infrastructure defined by the project's
`docker-compose.yml`, including PostgreSQL and Keycloak.

Check running containers with:

``` bash
docker compose ps
```

To follow container logs:

``` bash
docker compose logs -f
```

If you want to stop the infrastructure:

``` bash
docker compose down
```

For a completely clean local environment, including persistent Docker
volumes:

``` bash
docker compose down -v
```

> `docker compose down -v` removes the local database/Keycloak data
> stored in Docker volumes. Use it only when you want a fresh
> environment.

## Keycloak Realm Import

The repository includes the Keycloak realm export used by the
application.

The Keycloak container should be configured by the supplied Docker
Compose configuration to import the realm automatically.

After starting the infrastructure, verify that the `boxdelivery` realm
is available in Keycloak.

Keycloak is expected to be available at:

``` text
http://localhost:8081
```

The application is configured to use:

``` text
Realm: boxdelivery
```

## Database

The application uses PostgreSQL.

The current application configuration connects to:

``` text
Host: localhost
Port: 5432
Database: boxdelivery
Username: boxdelivery
Password: boxdelivery
```

The database schema is managed by Flyway.

Hibernate is configured with:

``` text
ddl-auto: validate
```

Therefore, Hibernate does not create or modify the database schema.
Flyway migrations under:

``` text
src/main/resources/db/migration
```

are responsible for schema creation and evolution.

## Run the Application

After PostgreSQL and Keycloak are running, start the Spring Boot
application:

``` bash
mvn spring-boot:run
```

Alternatively, build the application first:

``` bash
mvn clean package
```

Then run the generated JAR:

``` bash
java -jar target/box-delivery-service-0.0.1-SNAPSHOT.jar
```

The API runs on:

``` text
http://localhost:8080
```

## Swagger UI

Open:

``` text
http://localhost:8080/swagger-ui.html
```

Use Swagger's **Authorize** button to authenticate with Keycloak.

For a simple end-to-end test:

1.  Start PostgreSQL and Keycloak.
2.  Start the Spring Boot application.
3.  Open Swagger UI.
4.  Authorize with one of the seeded demo users.
5.  Obtain an access token.
6.  Call the protected API endpoints.

## Signup Flow

A new user can be created through the application's signup endpoint.

The flow is:

``` text
Client
  |
  | POST /signup
  | username + email + password
  v
Spring Boot
  |
  | Keycloak Admin API
  v
Keycloak
  |
  | creates user
  v
Keycloak user ID
  |
  | assign default application role
  v
PostgreSQL
  |
  | keycloak_user_id -> VIEWER
  v
Signup response
```

After signup, the new user can authenticate through the configured
Keycloak/Swagger OAuth2 flow and use the API according to the
permissions assigned to the `VIEWER` role.

## Application Roles and Permissions

The application's authorization model is database-backed.

The relationship is:

``` text
Keycloak User
      |
      | keycloak_user_id
      v
user_role_assignments
      |
      | role_name
      v
roles
      |
      v
role_permissions
      |
      | permission_code
      v
permissions
```

The application resolves the authenticated user's Keycloak ID from the
JWT and uses it to obtain the application roles and permissions.

Spring Security authorities are generated in the form:

``` text
ROLE_<ROLE_NAME>
PERMISSION_<PERMISSION_CODE>
```

For example:

``` text
ROLE_VIEWER
PERMISSION_READ_BOX
```

## Health Check

Actuator health information is exposed by the application.

Use:

``` text
http://localhost:8080/actuator/health
```

The health endpoint is useful for confirming that the Spring Boot
application has started successfully and that configured infrastructure
health indicators are available.

## Build

Run:

``` bash
mvn clean package
```

Run tests:

``` bash
mvn test
```

The project uses Spring Boot's test support and JUnit Jupiter/Mockito
for automated tests.

## Configuration

The main application configuration is in:

``` text
src/main/resources/application.yml
```

Key configuration areas include:

-   PostgreSQL datasource
-   JPA/Hibernate
-   Flyway
-   OAuth2 Resource Server / JWT
-   Server port
-   Actuator
-   Swagger/OpenAPI
-   Keycloak Admin API

The application expects Keycloak's JWT signing keys at:

``` text
http://localhost:8081/realms/boxdelivery/protocol/openid-connect/certs
```

The Keycloak Admin API configuration is under:

``` yaml
keycloak:
  admin:
```

Do not change the configured client secrets unless the corresponding
values in the Keycloak realm configuration are changed as well.

## Fresh Setup / Assessment Verification

To simulate the reviewer's environment, perform a clean startup:

``` bash
docker compose down -v
docker compose up -d
```

Wait for PostgreSQL and Keycloak to become healthy, then start Spring
Boot:

``` bash
mvn spring-boot:run
```

Verify:

``` text
Keycloak:
http://localhost:8081

API:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui/index.html

Health:
http://localhost:8080/actuator/health
```

Then:

1.  Confirm the `boxdelivery` Keycloak realm exists.
2.  Authenticate in Swagger using a seeded demo user.
3.  Call a protected endpoint.
4.  Test the signup endpoint with a new username/email/password.
5.  Authenticate the newly created user through Swagger.
6.  Confirm the new user can access the endpoints permitted to `VIEWER`.

This clean-start test is recommended before submitting the project
because it verifies that the repository does not depend on manually
configured state from an existing local Keycloak or PostgreSQL instance.

## Troubleshooting

### `401 Unauthorized`

Check:

-   Keycloak is running.
-   The `boxdelivery` realm exists.
-   The JWT was issued by the expected Keycloak realm.
-   Swagger is using the configured client.
-   The access token has not expired.

### `Account is not fully set up`

Check the Keycloak realm's Required Actions.

`Verify Profile` must be disabled for this assessment's immediate-login
signup flow.

### Keycloak user creation fails

Check:

-   `box-delivery-admin` client exists.
-   Its service account is enabled.
-   The client secret matches `application.yml`.
-   The service account has the required `realm-management` permissions.
-   Keycloak is reachable at `http://localhost:8081`.

### Database/schema errors

Check:

``` bash
docker compose ps
```

Then inspect the application and PostgreSQL logs.

Because Hibernate uses:

``` text
ddl-auto=validate
```

schema changes must be introduced through Flyway migrations rather than
Hibernate auto-DDL.

## Project Structure

A simplified view of the project:

``` text
src/
├── main/
│   ├── java/
│   │   └── com/polarisdigitech/boxdeliveryservice/
│   │       ├── auth/
│   │       ├── box/
│   │       ├── item/
│   │       ├── config/
│   │       ├── controllers/
            |-- delivery
│   │       └── shared/
│   │
│   └── resources/
│       ├── db/
│       │   └── migration/
│       └── application.yml
│
└── test/
    └── java/
```

The exact package structure may contain additional modules/classes as
the implementation evolves.

## Design Notes

### Keycloak is the Identity Provider

The application does not store user passwords.

Keycloak owns authentication and user identity.

### Application Owns Authorization

Application roles and permissions are stored in PostgreSQL.

This avoids maintaining two independent sources of truth for application
authorization.

### Flyway Owns the Schema

Flyway migrations are the source of truth for the PostgreSQL schema.

Hibernate validates the schema rather than creating it.

### Domain-Driven Design

Business rules are kept in the domain model where practical. For
example, box loading validates the projected item weight against the
box's weight limit before allowing the box to transition to a loaded
state.

### Box Availability

A box is considered available for loading when it is in the `IDLE` state
and its battery is at or above the loading threshold.

## Assessment Scope

The application intentionally keeps user management small:

-   One signup endpoint is provided by the application.
-   Keycloak handles authentication.
-   Newly registered users receive the default `VIEWER` application
    role.
-   Swagger UI is used to authenticate and obtain tokens.
-   There is no application-level user administration feature.

The focus is on the box-delivery domain, authentication/authorization
integration, persistence, validation, and the requested API behavior.

## Stopping the Environment

Stop the Spring Boot application with `Ctrl+C`.

Stop Docker services with:

``` bash
docker compose down
```

For a complete reset:

``` bash
docker compose down -v
```
