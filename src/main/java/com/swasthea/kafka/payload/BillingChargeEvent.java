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
 * Flat billing charge event, published when a billable activity occurs
 * (consultation, procedure, pharmacy, lab). Money is BigDecimal, never float/double.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingChargeEvent {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    // ── Mandatory identifiers ──────────────────────────────────────────────
    private String organizationId;
    private String partnerId;
    private String userId;

    // ── Envelope & context ─────────────────────────────────────────────────
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    @Builder.Default
    private String eventType = EventType.BILLING_CHARGE_CREATED;

    /** ISO-8601 timestamp. */
    private String occurrenceDateTime;

    private String encounterId;
    private String appointmentId;
    private String patientId;
    private String patientName;
    private String practitionerId;
    private String practitionerName;

    /** CONSULTATION, PROCEDURE, PHARMACY, LAB. */
    private String activityType;

    private String serviceCode;
    private String serviceName;

    @Builder.Default
    private Integer quantity = 1;

    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

    /** ISO-4217 code, e.g. "INR". */
    @Builder.Default
    private String currency = "INR";

    private Map<String, Object> metadata;

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BillingChargeEvent fromJson(String json) {
        try {
            return MAPPER.readValue(json, BillingChargeEvent.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
