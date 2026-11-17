package com.swasthea.kafka.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event published when an IPD referral or specialist consultation moves through its lifecycle
 * (requested, accepted, in progress, completed, declined, cancelled, escalated).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferralEventPayload {

    // ── Mandatory identifiers ──────────────────────────────────────────────
    private String organizationId;
    private String partnerId;
    private String userId;

    // ── Envelope & context ─────────────────────────────────────────────────
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    private String eventType;

    /** ISO-8601 timestamp. */
    private String occurrenceDateTime;

    // ── Referral Core Fields ────────────────────────────────────────────────
    private String referralId;
    private String admissionId;
    private String encounterId;

    private String patientId;
    private String patientName;
    private String wardName;
    private String bedName;

    /** "CONSULT" | "CO_MANAGE" | "TRANSFER_OF_CARE" */
    private String handover;
    private String priority;

    private String requesterId;
    private String requesterName;

    private String performerId;
    private String performerName;

    /** ISO-8601 deadline timestamp. */
    private String respondBy;

    private String reason;
    private String outcomeNote;
    private String declineReason;

    private Integer escalationLevel;
}
