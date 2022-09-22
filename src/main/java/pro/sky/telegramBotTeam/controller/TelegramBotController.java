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
import java.util.List;
import java.util.Objects;

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
     * Добавляет пользователя в усыновители
     * @throws Exception возвращает если нет пользователя в базе и если ползователь уже усыновитель
     */
    @PutMapping(path = "/add_adoption")
    public ResponseEntity<Adoption> uploadAdoption(@RequestParam Long idUser, Long idShelter, @RequestParam(required = false) Long idVol) throws Exception {
        LocalDate date = LocalDate.now();
        LocalDate date30 = date.plusDays(30);
        Member memberU = memberService.getMemberUser(idUser, idShelter, Member.MemberRole.USER);
        Member memberV = memberService.getMemberByIdUser(idVol);

        if(memberV==null) {
         memberV = memberU;
        List<Member> volunteersList = memberService.getMembersByRole(Member.MemberRole.VOLUNTEER)
                .stream().filter(member -> Objects.equals(member.getShelter().getId(), idShelter)).toList();
        if (!volunteersList.isEmpty()) {
            memberV = volunteersList.get(0);
        } else {
            volunteersList = memberService.getMembersByRole(Member.MemberRole.VOLUNTEER);
            if (!volunteersList.isEmpty()) memberV = volunteersList.get(0);
        }}

        if (memberU != null) {
            Adoption adoption = adoptionService.getAdoptionOnStatus(memberU.getId(), Adoption.AdoptionStatus.ACTIVE);
            if (adoption == null) {
                adoption = new Adoption(memberU, memberV, java.sql.Date.valueOf(date), java.sql.Date.valueOf(date30));
                adoptionService.createAdoption(adoption);
                return ResponseEntity.ok(adoption);
            } else throw new NotSupportedException("Этот пользователь уже является усыновителем");
        } else throw new NotSupportedException("Нет совпадений в таблице members для "
                + idUser.toString() + " " + idShelter.toString() + " USERS");
        //if(member!=null)
        //  return ResponseEntity.notFound().build();
    }
}//class TelegramBotController
