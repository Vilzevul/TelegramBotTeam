package pro.sky.telegramBotTeam.controller;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import javax.transaction.NotSupportedException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)

public class ParamException extends RuntimeException{

    @ExceptionHandler(NotSupportedException.class)
    public ErrorMessage handleException(NotSupportedException exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
