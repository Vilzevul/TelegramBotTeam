package pro.sky.telegramBotTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
@SuppressWarnings("unused")
public class TelegramBotController {
    private final UsersService usersService;
    private final ReportService reportService;

    private final AdoptionService adoptionService;

    private final MemberService memberService;

    private final ShelterService shelterService;

    private static final Logger log = LoggerFactory.getLogger(TelegramBotController.class);

    public TelegramBotController(UsersService usersService, ReportService reportService, AdoptionService adoptionService, MemberService memberService, ShelterService shelterService) {
        this.usersService = usersService;
        this.reportService = reportService;
        this.adoptionService = adoptionService;
        this.memberService = memberService;
        this.shelterService = shelterService;
    }

    @GetMapping
    public String testAPI() {
        return "Web API is working";
    }

    /**
     * Возвращает список пользователей и связанных с ними отчетами
     */
    @GetMapping(path = "/users_report")
    public Report getUsersWithReport(@RequestParam(required = true) Long idAdoption, LocalDate date) {
        if (idAdoption > 0)
            log.debug("Method - getUsersWithReport was called");
        return reportService.getReport(idAdoption, date);
    }

    @PutMapping(path = "/update_reportById")
    public Report updateUsersWithReport(@RequestParam(required = true) Long idAdoption, LocalDate date) {


        if (idAdoption > 0)
            log.debug("Method - getUsersWithReport was called");
        return reportService.getReport(idAdoption, date);
    }

    /**
     * Возвращает список таблицы adoptions
     */
    @GetMapping(path = "/list_adoptions")
    public List<Adoption> printList() {
        return adoptionService.getAllAdoptions();
    }

    /**
     * Возвращает список таблицы adoptions по id
     */
    @GetMapping("/adoption/{id}")
    public Adoption getAdoptionInfo(@PathVariable Long id) throws Exception {
      return  adoptionService.getAdoption(id);
    }

    /**
     * Обновляет статус усыновителя
     */
    @GetMapping("/adoption/{id},{status}")
    public String updateAdoptionStatus(@PathVariable Long id, @PathVariable Adoption.AdoptionStatus status) throws Exception {
        return  adoptionService.updateAdoptionStatus(id, status);
    }

    @GetMapping("/adoption/{id},{idShelter}")
    public   Optional<Adoption> searchAdoptionStatus(@PathVariable Long id, @PathVariable int idShelter) throws Exception {
        return  adoptionService.searchAdoptionStatus(id, idShelter);
    }
}
