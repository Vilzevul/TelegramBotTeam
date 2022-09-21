package pro.sky.telegramBotTeam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.exeption.TelegramBotNotFoundException;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;
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
        return adoptionService.getAdoption(id);
    }

    /**
     * Получает список участников приюта указанной роли.
     */
    @Operation(summary = "Получить список участников приюта указанной роли")
    @GetMapping ("/member/get-by-role")
    public ResponseEntity<List<Member>> getMembersByRole(@Parameter(description = "Роль участников") @RequestParam(required = true) Member.MemberRole memberRole,
                                                         @Parameter(description = "ID приюта") @RequestParam(required = true) Long idShelter) {
        return ResponseEntity.ok(memberService.getMembersByRole(memberRole, idShelter));
    }

    /**
     * Обновляет роль пользователя.
     */
    @Operation(
            summary = "Обновить роль пользователя с указанным ID. Возвращает количество обновленных записей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно обновлена"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не является участником приюта")
            }
    )
    @PutMapping("/member/update-role")
    public ResponseEntity<Integer> updateMemberRole(@Parameter(description = "ID пользователя") @RequestParam(required = true) Long idUser,
                                                    @Parameter(description = "Роль пользователя") @RequestParam(required = true) Member.MemberRole memberRole) {
        int count = memberService.updateMemberRole(idUser, memberRole);
        if (count == 0) {
            throw new TelegramBotNotFoundException("No users with the specified id were found in the members table");
        }
        return ResponseEntity.ok(count);
    }

    /**
     * Обновляет статус усыновителя
     */
    @ApiResponse(description = "Поиск данных по ИД чата и по ИД приюта и изменение статуса")
    @GetMapping("/adoption/update-status/{id}/{status}/{idShelter}")
    public void updateAdoptionStatus(@Parameter(description = "ИД чата") @PathVariable Long id, @Parameter(description = "Выберите статус, на который нужно перезаписать") @PathVariable Adoption.AdoptionStatus status, @Parameter(description = "ИД приюта(1-собаки, 2-кошки)") @PathVariable int idShelter) throws Exception {
         adoptionService.updateAdoptionStatus_(id, status, idShelter);
    }

    /**
     * Поиск данных по ИД чата и по ИД приюта
     */
    @ApiResponse(description = "Поиск данных по ИД чата и по ИД приюта")
    @GetMapping("/adoption/search/{id}/{idShelter}")
    public Optional<Adoption> searchAdoptionStatus(@Parameter(description = "ИД чата") @PathVariable Long id, @Parameter(description = "ИД приюта(1-собаки, 2-кошки)") @PathVariable int idShelter) throws Exception {
        return adoptionService.searchAdoptionStatus(id, idShelter);
    }
}
