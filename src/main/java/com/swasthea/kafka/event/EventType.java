package com.swasthea.kafka.event;

/**
 * String constants for every domain event type in the Swasthea platform.
 *
 * <p>Add new constants here when new event types are introduced.
 * Keep the naming convention: {DOMAIN}_{VERB_PAST_TENSE}.
 */
public final class EventType {

    // ── Encounter events ───────────────────────────────────────────────────
    public static final String ENCOUNTER_CREATED        = "ENCOUNTER_CREATED";
    public static final String ENCOUNTER_STATUS_CHANGED = "ENCOUNTER_STATUS_CHANGED";

    // ── Billing events ─────────────────────────────────────────────────────
    public static final String BILLING_CHARGE_CREATED   = "BILLING_CHARGE_CREATED";
    public static final String CHARGE_ITEM              = "CHARGE_ITEM";
    public static final String OT_COMPLETED             = "OT_COMPLETED";

    // ── Procedure & Clinical events ─────────────────────────────────────────
    public static final String PROCEDURE_ORDERED         = "PROCEDURE_ORDERED";
    public static final String PROCEDURE_ORDER_CANCELLED = "PROCEDURE_ORDER_CANCELLED";

    // ── Referral & Consult events ───────────────────────────────────────────
    public static final String REFERRAL_REQUESTED         = "REFERRAL_REQUESTED";
    public static final String REFERRAL_ACCEPTED          = "REFERRAL_ACCEPTED";
    public static final String REFERRAL_DECLINED          = "REFERRAL_DECLINED";
    public static final String REFERRAL_COMPLETED         = "REFERRAL_COMPLETED";
    public static final String REFERRAL_CANCELLED         = "REFERRAL_CANCELLED";
    public static final String REFERRAL_ESCALATED         = "REFERRAL_ESCALATED";
    public static final String REFERRAL_REMINDER          = "REFERRAL_REMINDER";

    private EventType() { /* constants-only class */ }
}
