package pro.sky.TelegramBotTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.TelegramBotTeam.model.Keepingpets;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@SuppressWarnings("unused")
public class KeepingpetsController {
    private final UsersService usersService;

    private static final Logger log = LoggerFactory.getLogger(KeepingpetsController.class);

    public KeepingpetsController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Возвращает список пользователей и связанных с ними отчетами
     */
    @RequestMapping(path = "/users_report", method = RequestMethod.GET)
    public List<Keepingpets> getUsersWithReport() {
        log.debug("Method - getUsersWithReport was called");
        return usersService.getUsersWitReport();
    }
}
