package com.swasthea.kafka.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for the {@code ENCOUNTER_CREATED} event.
 *
 * <p>Contains the core identifiers and status of a newly created encounter.
 * Consumers that need full encounter details should call the enep-service REST API
 * using {@code encounterId}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EncounterCreatedPayload {

    /** MongoDB document ID of the encounter. */
    private String encounterId;

    /** FHIR encounter status at creation time. */
    private String status;

    /** Encounter type string, e.g. {@code "consultation"}. */
    private String encounterType;

    /** FHIR patient reference, e.g. {@code "Patient/abc123"}. */
    private String subjectReference;

    /** Human-readable patient name. */
    private String subjectDisplay;

    /** Linked scheduling appointment ID. */
    private String appointmentId;

    /** ISO-8601 period start timestamp. */
    private String periodStart;

    /** FHIR reference of the primary practitioner, e.g. {@code "Practitioner/doc456"}. */
    private String practitionerReference;

    /** Human-readable practitioner name. */
    private String practitionerDisplay;

    /** Display name of the service provider / organisation. */
    private String serviceProviderDisplay;
}
