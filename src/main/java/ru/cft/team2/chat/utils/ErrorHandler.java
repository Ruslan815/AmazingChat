package ru.cft.team2.chat.utils;

import ru.cft.team2.chat.model.User;

public class ErrorHandler {
    String code;

    public ErrorHandler(String errorMessage) {
        this.code = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static ErrorHandler handleUserNullData(User someUser) {
        if (someUser.getFirstName() == null && someUser.getLastName() == null) {
            return new ErrorHandler("NOT_FOUND_FIRSTNAME_AND_LASTNAME");
        } else if (someUser.getFirstName() == null) {
            return new ErrorHandler("NOT_FOUND_FIRSTNAME");
        } else if (someUser.getLastName() == null) {
            return new ErrorHandler("NOT_FOUND_LASTNAME");
        }
        return null;
    }
}
