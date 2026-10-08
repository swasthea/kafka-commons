package com.swasthea.kafka.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * FHIR R4 ChargeItem-aligned billable event, shared by all producing modules
 * (consultation, pharmacy, procedure, lab, radiology). Money is BigDecimal, never float/double.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeItemPayload {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Builder.Default
    private String resourceType = "ChargeItem";

    private String id;

    @Builder.Default
    private String status = "billable";

    /** CONSULTATION, PHARMACY, PROCEDURE, LAB, RADIOLOGY. */
    private String activityType;

    private String serviceCode;
    private String serviceName;

    /** Patient. */
    private Reference subject;

    /** Encounter (includes appointmentId). */
    private Reference context;

    /** Practitioner. */
    private Reference performer;

    /** Organization. */
    private Reference organization;

    @Builder.Default
    private Integer quantity = 1;

    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

    /** ISO-4217 code, e.g. "INR". */
    @Builder.Default
    private String currency = "INR";

    /** ISO-8601 timestamp. */
    private String occurrenceDateTime;

    private List<ChargeSubItem> items;
    private Map<String, Object> metadata;

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ChargeItemPayload fromJson(String json) {
        try {
            return MAPPER.readValue(json, ChargeItemPayload.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reference {
        /** e.g. "Patient/123". */
        private String reference;
        private String id;
        private String display;
        private String appointmentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChargeSubItem {
        private String itemId;
        private String code;
        private String name;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalAmount;
        private String batchNumber;
    }
}
