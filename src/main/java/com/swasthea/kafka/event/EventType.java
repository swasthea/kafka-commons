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

    private EventType() { /* constants-only class */ }
}
