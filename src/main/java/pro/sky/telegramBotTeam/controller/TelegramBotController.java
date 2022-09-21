package pro.sky.telegramBotTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.service.*;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.time.LocalDate;

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

    @PutMapping(path = "/add_adoption")
    public ResponseEntity<Adoption> uploadAdoption(@RequestParam Long idUser, Long idShelter) throws Exception {
        LocalDate date = LocalDate.now();
        LocalDate date30 = date.plusDays(30);

        Member member = memberService.getMember(idUser, idShelter);
        if (member != null) {
            Adoption adoption = adoptionService.getAdoptionOnStatus(member.getId(),Adoption.AdoptionStatus.ACTIVE);
            if(adoption == null) {
                adoption = new Adoption();
                adoption.setParent(member);
                adoption.setVolunteer(member);
                adoption.setStartDate(java.sql.Date.valueOf(date));
                adoption.setEndDate(java.sql.Date.valueOf(date30));

                adoptionService.createAdoption(adoption);

                return ResponseEntity.ok(adoption);

            } else throw new NotSupportedException("Этот пользователь уже является усыновителем");
        }//if(member!=null)
        return ResponseEntity.notFound().build();
    }
}//class TelegramBotController
