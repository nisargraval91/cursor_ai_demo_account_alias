## Account Alias App

Multi-module Spring Boot application that manages user accounts and their email aliases. Includes basic authentication, H2 in-memory database, and OpenAPI UI.

### Modules

- app-domain: Domain model and repositories
- app-web: REST API, security, services, and tests

### Prerequisites

- Java 17+
- Maven 3.9+

### Build

```bash
mvn clean verify
```

### Run (app-web)

```bash
# Run web module with dependencies built automatically
mvn -pl app-web -am spring-boot:run
```

- App URL: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:aliasdb`, user: `sa`, no password)
- OpenAPI UI: `http://localhost:8080/swagger-ui/index.html`

### Test

```bash
# Run unit tests
mvn test

# Or full build with coverage (JaCoCo)
mvn clean verify
```

### Authentication

- Register a user with email/password
- Use HTTP Basic Auth for protected endpoints

### API Endpoints (summary)

- Auth
  - POST `/api/auth/register` — body: `{ "email": "user@example.com", "password": "Password1!" }`
- Users
  - GET `/api/users/me` — requires Basic Auth
- Aliases (require Basic Auth)
  - POST `/api/aliases` — create: `{ "alias": "unique-alias" }`
  - GET `/api/aliases` — list my aliases
  - PUT `/api/aliases/{id}/status` — update status: `{ "active": true|false }`
  - DELETE `/api/aliases/{id}` — delete alias

### Quick start (cURL)

```bash
# Register
curl -s -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"Password1!"}'

# Create alias (Basic Auth)
curl -s -X POST http://localhost:8080/api/aliases \
  -u user@example.com:Password1! \
  -H 'Content-Type: application/json' \
  -d '{"alias":"unique-alias"}'

# List aliases
curl -s -u user@example.com:Password1! http://localhost:8080/api/aliases

# Disable alias (replace {id})
curl -s -X PUT http://localhost:8080/api/aliases/{id}/status \
  -u user@example.com:Password1! \
  -H 'Content-Type: application/json' \
  -d '{"active":false}'

# Delete alias
curl -s -X DELETE http://localhost:8080/api/aliases/{id} \
  -u user@example.com:Password1!
```

### Notes

- `.gitignore` excludes build artifacts (Maven `target/`) and IDE files.
- Tests use JUnit 5; Surefire is configured in the root `pom.xml`.