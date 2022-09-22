package pro.sky.telegramBotTeam.exception;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.NotSupportedException;
import java.nio.channels.AlreadyConnectedException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParamException extends RuntimeException{
    @ExceptionHandler({AccountNotFoundException.class})
    public ErrorMessage handleException(AccountNotFoundException exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
