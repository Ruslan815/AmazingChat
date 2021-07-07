package ru.cft.team2.chat.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.model.User;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ValidationResult handleAnyException(Throwable exception) {
        return ValidationResult.UNKNOWN_ERROR;
    }

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
