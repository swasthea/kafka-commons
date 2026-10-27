package com.swasthea.kafka.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swasthea.kafka.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Unified procedure order event published when a procedure is ordered
 * (from IPD, OPD/Consultation, or Emergency).
 *
 * Consumed by:
 * 1. Billing Service -> creates a PROCEDURE line item on patient invoice.
 * 2. OT Service      -> stages an OtRequest if requiresOT == true.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcedureOrderEvent {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    // ── Mandatory identifiers ──────────────────────────────────────────────
    private String organizationId;
    private String partnerId;
    private String userId;

    // ── Envelope & context ─────────────────────────────────────────────────
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    @Builder.Default
    private String eventType = EventType.PROCEDURE_ORDERED;

    /** ISO-8601 timestamp. */
    private String occurrenceDateTime;

    private String procedureOrderId;
    private String encounterId;
    private String patientId;
    private String patientName;
    private String orderingClinicianId;
    private String orderingClinicianName;

    // ── Procedure Master Details ───────────────────────────────────────────
    private String procedureCode;
    private String procedureName;
    private String department;

    // ── Financial / Billing Fields ─────────────────────────────────────────
    @Builder.Default
    private String activityType = "PROCEDURE";

    @Builder.Default
    private Integer quantity = 1;

    private BigDecimal standardFee;
    private BigDecimal surgeonFee;
    private BigDecimal anesthetistFee;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

    /** ISO-4217 code, e.g. "INR". */
    @Builder.Default
    private String currency = "INR";

    // ── Clinical & OT Scheduling Fields ────────────────────────────────────
    @Builder.Default
    private Boolean requiresOT = false;

    @Builder.Default
    private Integer standardDurationMinutes = 15;

    /** Scheduled surgery/procedure date: YYYY-MM-DD. */
    private String scheduledDate;

    /** Scheduled start time: ISO-8601 or HH:mm. */
    private String scheduledStartTime;

    /** Scheduled end time: ISO-8601 or HH:mm. */
    private String scheduledEndTime;

    /** ROUTINE, URGENT, STAT. */
    private String priority;

    /** Clinical notes, special instructions, pre-op clearance. */
    private String notes;

    private Map<String, Object> metadata;

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProcedureOrderEvent fromJson(String json) {
        try {
            return MAPPER.readValue(json, ProcedureOrderEvent.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
