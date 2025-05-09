# Director Filter API

---

## Features

- Fetches movies from: `https://challenge.iugolabs.com/api/movies/search?page=X`
- Filters directors based on a `threshold` parameter
- Returns a sorted list of director names in JSON format
- Includes:
    - Configurable Retry policy with exponential backoff using WebClient
    - Exception handling with `@RestControllerAdvice`
    - OpenAPI documentation
    - Unit tests

---

## Stack

- Java 17
- Spring Boot 3.x
- Spring WebFlux
- Reactor
- OpenAPI
- JUnit 5
- Mockito
- MockWebServer

---

## How to Use

### Endpoint

```http
GET /api/directors?threshold=X
```

#### Sample calls

```
$ curl http://localhost:8080/api/directors\?threshold=aasbn                                                                                                         raulbajales@pop-os
{"error":"Bad Request","message":"Invalid threshold: 'aasbn'","timestamp":"2025-05-08T16:33:17.406123834Z"}

$ curl http://localhost:8080/api/directors\?threshold=4                                                                                                             raulbajales@pop-os
{"directors":["Martin Scorsese","Woody Allen"]}

$ curl http://localhost:8080/api/directors\?threshold=-100                                                                                                          raulbajales@pop-os
{"directors":[]}
```

#### OpenApi docs

Visit http://localhost:8080/v3/api-docs

