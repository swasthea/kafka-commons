# kafka-commons

Shared Kafka event model and publisher for the Swasthea HMIS platform.

## Overview

`kafka-commons` is a Spring Boot auto-configured library that provides:

- **`DomainEvent<T>`** — generic envelope carrying any payload with consistent metadata
- **`KafkaEventPublisher`** — thin wrapper around `KafkaTemplate` that never throws
- **`EventType`** — constants for all event type strings
- **Payload classes** — `EncounterCreatedPayload`, `EncounterStatusChangedPayload`

Every event on the shared topic (`swasthea-dev` by default) has the same envelope shape:

```json
{
  "eventId": "uuid",
  "eventType": "ENCOUNTER_CREATED",
  "source": "enep-service",
  "version": "1",
  "occurredAt": "2026-05-07T08:31:00Z",
  "tenantId": null,
  "correlationId": null,
  "payload": { ... }
}
```

Consumers route on `eventType` and ignore events they don't handle.

---

## Adding to a service

### 1. Add dependency to `pom.xml`

```xml
<dependency>
    <groupId>com.swasthea</groupId>
    <artifactId>kafka-commons</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

Add the GitHub Packages repository:

```xml
<repositories>
    <repository>
        <id>github-kafka-commons</id>
        <url>https://maven.pkg.github.com/swasthea/kafka-commons</url>
    </repository>
</repositories>
```

### 2. Configure `~/.m2/settings.xml`

GitHub Packages requires authentication even for reads:

```xml
<settings>
  <servers>
    <server>
      <id>github-kafka-commons</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>  <!-- needs read:packages scope -->
    </server>
  </servers>
</settings>
```

### 3. Configure `application-dev.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:kafka-3396eab8-orglance-bfdf.f.aivencloud.com:11697}
    properties:
      security.protocol: SSL
      ssl.keystore.type: PEM
      ssl.keystore.certificate.chain: "${KAFKA_CLIENT_CERT}"
      ssl.keystore.key: "${KAFKA_CLIENT_KEY}"
      ssl.truststore.type: PEM
      ssl.truststore.certificates: "${KAFKA_CA_CERT}"
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      properties:
        spring.json.add.type.headers: false

swasthea:
  kafka:
    topic: swasthea-dev
```

### 4. Set environment variables for local dev

```bash
export KAFKA_BOOTSTRAP_SERVERS="kafka-3396eab8-orglance-bfdf.f.aivencloud.com:11697"
export KAFKA_CLIENT_CERT="$(cat /path/to/access.cert)"
export KAFKA_CLIENT_KEY="$(cat /path/to/access.key)"
export KAFKA_CA_CERT="$(cat /path/to/ca.pem)"
```

> **Never commit certificate files or keys to git.**

---

## Publishing events

`KafkaEventPublisher` is auto-wired when `spring.kafka.bootstrap-servers` is configured. Inject it with `@Autowired(required = false)` so the service starts cleanly without Kafka in test/local environments:

```java
@Autowired(required = false)
private KafkaEventPublisher kafkaEventPublisher;

private <T> void publishEvent(DomainEvent<T> event) {
    if (kafkaEventPublisher != null) {
        kafkaEventPublisher.publish(event);
    }
}
```

### ENCOUNTER_CREATED

```java
publishEvent(DomainEvent.<EncounterCreatedPayload>builder()
    .eventType(EventType.ENCOUNTER_CREATED)
    .source("enep-service")
    .payload(EncounterCreatedPayload.builder()
        .encounterId(saved.getId())
        .status(saved.getStatus())
        // ...
        .build())
    .build());
```

### ENCOUNTER_STATUS_CHANGED

```java
publishEvent(DomainEvent.<EncounterStatusChangedPayload>builder()
    .eventType(EventType.ENCOUNTER_STATUS_CHANGED)
    .source("enep-service")
    .payload(EncounterStatusChangedPayload.builder()
        .encounterId(saved.getId())
        .previousStatus(oldStatus)
        .newStatus(newStatus)
        .changedAt(Instant.now().toString())
        .build())
    .build());
```

---

## Adding a new event type

1. Add a constant to `EventType.java`
2. Create a new payload class in `com.swasthea.kafka.payload`
3. Bump the library version and publish

---

## Consuming events

```java
@KafkaListener(topics = "swasthea-dev", groupId = "my-service")
public void consume(DomainEvent<JsonNode> event) {
    switch (event.getEventType()) {
        case EventType.ENCOUNTER_CREATED -> handleCreated(event);
        case EventType.ENCOUNTER_STATUS_CHANGED -> handleStatusChange(event);
        // ignore everything else
    }
}
```

For typed deserialization, configure a `RecordMessageConverter` or use `@Payload` with the specific payload class.

---

## Publishing a new version

Trigger the GitHub Actions workflow:

```bash
gh workflow run publish.yml --repo swasthea/kafka-commons
```

Or create a GitHub Release — the workflow triggers automatically.
