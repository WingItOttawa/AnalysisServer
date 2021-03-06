package com.wingit.analysisserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum that represents the different political leanings a document can have
 *
 * @author AveryVine
 * @since July 2018
 */
public enum Wing {
    FAR_LEFT("far-left"),
    MODERATE_LEFT("moderate-left"),
    CENTRIST("centrist"),
    MODERATE_RIGHT("moderate-right"),
    FAR_RIGHT("far-right");

    private final String value;

    /**
     * Constructs a Wing enum to be a part of the persistent set
     *
     * @param value the string value of the enum
     */
    Wing(String value) {
        this.value = value;
    }

    /**
     * Converts a string value into a Wing enum, if possible (required by Spring for JSON to POJO conversion)
     *
     * @param value the string value to convert
     * @return the appropriate Wing enum
     */
    @JsonCreator
    private static Wing fromValue(String value) {
        for (Wing wing : Wing.values()) {
            if (String.valueOf(wing.value).equals(value)) {
                return wing;
            }
        }
        return null;
    }

    /**
     * Converts a Wing enum into its string value (required by Spring for JSON to POJO conversion)
     *
     * @return the string value of the Wing enum
     */
    @JsonValue
    private String getValue() {
        return value;
    }
}
