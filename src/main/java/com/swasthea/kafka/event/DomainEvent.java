package com.swasthea.kafka.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Generic envelope for all Swasthea domain events.
 *
 * <p>Every event published to a shared Kafka topic uses this wrapper.
 * Consumers inspect {@code eventType} (a constant from {@link EventType})
 * to decide whether to deserialise and process the {@code payload}.
 *
 * @param <T> the strongly-typed payload for this event
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DomainEvent<T> {

    /** Unique event identifier (UUID v4). Auto-generated when using the builder. */
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    /**
     * Discriminator used by consumers to route/ignore this event.
     * Use constants from {@link EventType}.
     */
    private String eventType;

    /** Originating service name, e.g. {@code "enep-service"}. */
    private String source;

    /** Payload schema version — increment when the contract changes. */
    @Builder.Default
    private String version = "1";

    /** ISO-8601 UTC timestamp of when the event occurred. */
    @Builder.Default
    private String occurredAt = Instant.now().toString();

    /** Tenant / organisation identifier (for multi-tenant filtering). */
    private String tenantId;

    /** Optional correlation / trace ID for cross-service request tracking. */
    private String correlationId;

    /** Typed event payload — deserialise according to {@code eventType}. */
    private T payload;
}
