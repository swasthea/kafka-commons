package com.swasthea.kafka.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for the {@code ENCOUNTER_STATUS_CHANGED} event.
 *
 * <p>Emitted whenever an encounter transitions from one status to another.
 * Both the old and new status are included so consumers can react to specific
 * transitions (e.g. any → COMPLETED triggers billing).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EncounterStatusChangedPayload {

    /** MongoDB document ID of the encounter. */
    private String encounterId;

    /** Status before the change. */
    private String previousStatus;

    /** Status after the change. */
    private String newStatus;

    /** FHIR patient reference, e.g. {@code "Patient/abc123"}. */
    private String subjectReference;

    /** Human-readable patient name. */
    private String subjectDisplay;

    /** Linked scheduling appointment ID (if any). */
    private String appointmentId;

    /** ISO-8601 UTC timestamp of when the status changed (same as DomainEvent.occurredAt). */
    private String changedAt;
}
