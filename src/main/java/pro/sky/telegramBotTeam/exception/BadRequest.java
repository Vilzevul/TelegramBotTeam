package pro.sky.telegramBotTeam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException{
    public BadRequest() {
        super("BAD_REQUEST");
    }

}
