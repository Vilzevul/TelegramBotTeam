package pro.sky.telegramBotTeam.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TelegramBotNotFoundException extends RuntimeException {

    public TelegramBotNotFoundException(String s) {
        super(s);
    }
}

