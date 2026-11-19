package com.swasthea.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Standard source module enumeration representing the origin or domain of billable charges.
 */
public enum SourceModule {
    OPD("opd"),
    IPD("ipd"),
    OT("ot"),
    PHARMACY("pharmacy"),
    LAB("lab");

    private final String value;

    SourceModule(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SourceModule fromString(String val) {
        if (val == null || val.isBlank()) return null;
        for (SourceModule sm : values()) {
            if (sm.value.equalsIgnoreCase(val.trim()) || sm.name().equalsIgnoreCase(val.trim())) {
                return sm;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
