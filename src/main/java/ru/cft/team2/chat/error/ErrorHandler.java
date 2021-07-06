package ru.cft.team2.chat.error;

import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.model.User;

public class ErrorHandler {

    public static ValidationResult validateUser(User someUser) {
        if (someUser.getFirstName() == null && someUser.getLastName() == null) {
            return ValidationResult.NOT_FOUND_FIRSTNAME_AND_LASTNAME;
        } else if (someUser.getFirstName() == null) {
            return ValidationResult.NOT_FOUND_FIRSTNAME;
        } else if (someUser.getLastName() == null) {
            return ValidationResult.NOT_FOUND_LASTNAME;
        }
        return ValidationResult.NO_ERROR;
    }

    public static ValidationResult validateMessage(Message someMessage, boolean isUserExist) {
        if (!isUserExist) {
            return ValidationResult.USER_NOT_FOUND;
        }
        if (someMessage.getText() == null) {
            return ValidationResult.NOT_FOUND_TEXT;
        }
        return ValidationResult.NO_ERROR;
    }
}
