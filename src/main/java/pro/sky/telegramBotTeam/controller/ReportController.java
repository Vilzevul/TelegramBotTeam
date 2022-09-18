package pro.sky.telegramBotTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBotTeam.service.UsersService;

@RestController
@RequestMapping("/reports")
@SuppressWarnings("unused")
public class ReportController {
    private final UsersService usersService;

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    public ReportController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public String testAPI() {
        return "Web API is working";
    }

//    /**
//     * Возвращает список пользователей и связанных с ними отчетами
//     */
//    @GetMapping(path = "/users_report")
//    public List<Report> getUsersWithReport() {
//        log.debug("Method - getUsersWithReport was called");
//        return usersService.getUsersWitReport();
//    }
}
