package ru.cft.team2.chat.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ValidationResult {
    USER_NOT_FOUND,
    NOT_FOUND_FIRSTNAME_AND_LASTNAME,
    NOT_FOUND_FIRSTNAME,
    NOT_FOUND_LASTNAME,
    NO_ERROR,
    ;

    @JsonSerialize
    String getCode() {
        return name();
    }
}
