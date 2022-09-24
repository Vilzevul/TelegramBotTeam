package pro.sky.telegramBotTeam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TelegramBotAlreadyExistsException extends RuntimeException {
    public TelegramBotAlreadyExistsException(String s) {
        super(s);
    }
}
