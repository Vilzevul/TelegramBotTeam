package pro.sky.telegramBotTeam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.exception.TelegramBotAlreadyExistsException;
import pro.sky.telegramBotTeam.exception.TelegramBotNotFoundException;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;

import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.service.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для упрощения управления данными приютов.
 */
@RestController
@SuppressWarnings("unused")
public class TelegramBotController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotController.class);

    private final ShelterService shelterService;
    private final UsersService usersService;
    private final MemberService memberService;
    private final ReportService reportService;
    private final AdoptionService adoptionService;

    public TelegramBotController(ShelterService shelterService,
                                 UsersService usersService,
                                 MemberService memberService,
                                 AdoptionService adoptionService,
                                 ReportService reportService) {
        this.shelterService = shelterService;
        this.usersService = usersService;
        this.memberService = memberService;
        this.reportService = reportService;
        this.adoptionService = adoptionService;
    }

    /**
     * Возвращает список участников приюта указанной роли.
     */
    @Operation(
            summary = "Получить список участников приюта указанной роли",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список участников успешно получен")
            }
    )
    @GetMapping("/member/get-by-role")
    public ResponseEntity<List<Member>> getMembersByRole(@Parameter(description = "Роль участников") @RequestParam(required = true) Member.MemberRole memberRole,
                                                         @Parameter(description = "ID приюта") @RequestParam(required = true) Long idShelter) {
        return ResponseEntity.ok(memberService.getMembersOfShelterWithRole(idShelter, memberRole));
    }

    /**
     * Обновляет роль пользователя.
     */
    @Operation(
            summary = "Обновить роль пользователя. Возвращает количество обновленных записей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно обновлена"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с такими данными не является участником приюта")
            }
    )
    @PutMapping("/member/update-role")
    public ResponseEntity<Integer> updateMemberRole(@Parameter(description = "ID чата пользователя") @RequestParam(required = true) Long idUser,
                                                    @Parameter(description = "Роль пользователя") @RequestParam(required = true) Member.MemberRole memberRole) {
        int count = memberService.updateMemberRole(idUser, memberRole);
        if (count == 0) {
            LOGGER.error("No users with the specified id were found in the members table");
            throw new TelegramBotNotFoundException("No users with the specified id were found in the members table");
        }
        return ResponseEntity.ok(count);
    }

    /**
     * Обновляет статус усыновителя.
     */
    @Operation(
            summary = "Обновить статус испытательного периода усыновителя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус успешно обновлен")
            }
    )
    @GetMapping("/adoption/update-status/{idUser}/{idShelter}/{status}")
    public ResponseEntity<Void> updateAdoptionStatus(@Parameter(description = "ID чата пользователя") @PathVariable Long idUser,
                                                     @Parameter(description = "ID приюта (1 - собаки, 2 - кошки)") @PathVariable Long idShelter,
                                                     @Parameter(description = "Новый статус испытательного периода") @PathVariable Adoption.AdoptionStatus status) {
        adoptionService.updateAdoptionStatus(idUser, idShelter, status);
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет пользователя в усыновители.
     */
    @Operation(
            summary = "Добавить пользователя в таблицу усыновителей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже является усыновителем"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с такими данными не найден")
            }
    )
    @PutMapping(path = "/adoption/add-parent")
    public ResponseEntity<Adoption> uploadAdoption(@Parameter(description = "ID чата пользователя") @RequestParam Long idUser,
                                                   @Parameter(description = "ID приюта (1 - собаки, 2 - кошки)") @RequestParam Long idShelter,
                                                   @Parameter(description = "ID чата волонтера, если известен") @RequestParam(required = false) Long idVol) {
        LocalDate date = LocalDate.now();
        LocalDate date30 = date.plusDays(30);
        Member memberU = memberService.getMemberOfShelterWithRole(idUser, idShelter, Member.MemberRole.USER);
        Member memberV = memberService.getMember(idVol);

        if (memberV == null) {
            memberV = memberU;
            List<Member> volunteersList = memberService.getMembersOfShelterWithRole(idShelter, Member.MemberRole.VOLUNTEER);
            if (!volunteersList.isEmpty()) {
                memberV = volunteersList.get(0);
            } else {
                volunteersList = memberService.getMembersWithRole(Member.MemberRole.VOLUNTEER);
                if (!volunteersList.isEmpty()) {
                    memberV = volunteersList.get(0);
                }
            }
        }

        if (memberU != null) {
            Adoption adoption = adoptionService.getAdoptionOnStatus(/*memberU.getId()*/1L, Adoption.AdoptionStatus.ACTIVE);
            if (adoption == null) {
                adoption = new Adoption(memberU, memberV, java.sql.Date.valueOf(date), java.sql.Date.valueOf(date30));
                adoptionService.createAdoption(adoption);
                return ResponseEntity.ok(adoption);
            } else {
                LOGGER.error("Этот пользователь уже является усыновителем");
                throw new TelegramBotAlreadyExistsException("Этот пользователь уже является усыновителем");
            }
        } else {
            LOGGER.error("Нет совпадений в таблице members для " + idUser + " " + idShelter + " USERS");
            throw new TelegramBotNotFoundException("Нет совпадений в таблице members для " + idUser + " " + idShelter + " USERS");
        }
    }

    /**
     * Возвращает список всех имеющихся в базе отчетов.
     */
    @Operation(
            summary = "Получить список отчетов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отчетов успешно получен")
            }
    )
    @GetMapping(path = "/report/get-all")
    public ResponseEntity<List<Report>> getAllReport() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    /**
     * Возвращает отчет по ID записи усыновления и времени отправки отчета.
     */
    @Operation(
            summary = "Получить отчет указанной даты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отчет успешно получен"),
                    @ApiResponse(responseCode = "404", description = "Отчет с такими данными не найден")
            }
    )
    @GetMapping(path = "/report/get-by-date/{idAdoption}/{date}")
    public ResponseEntity<Report> getReportByDate(@Parameter(description = "ID записи усыновления") @PathVariable(required = true) Long idAdoption,
                                                  @Parameter(description = "Дата отчета") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Report report = reportService.getReportForDate(idAdoption, date);
        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            LOGGER.error("Нет совпадений в таблице reports");
            throw new TelegramBotNotFoundException("Нет совпадений в таблице reports");
        }
    }

    /**
     * Возвращает <b>полный</b> отчет по ID записи усыновления и времени отправки отчета.
     */
    @Operation(
            summary = "Получить полный отчет указанной даты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отчет успешно получен"),
                    @ApiResponse(responseCode = "404", description = "Отчет с такими данными не найден")
            }
    )
    @GetMapping(path = "/report/get-by-date-completed/{idAdoption}/{date}")
    public ResponseEntity<?> getCompletedReportByDate(@Parameter(description = "ID записи усыновления") @PathVariable(required = true) Long idAdoption,
                                                      @Parameter(description = "Дата отчета") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Report report = reportService.getCompletedReportForDate(idAdoption, date);
        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            LOGGER.error("Нет совпадений в таблице reports");
            throw new TelegramBotNotFoundException("Нет совпадений в таблице reports");
        }
    }
}
