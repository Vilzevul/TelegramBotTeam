package pro.sky.telegramBotTeam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.exception.NotFoundException;
import pro.sky.telegramBotTeam.exception.TelegramBotNotFoundException;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.service.*;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.nio.channels.AlreadyConnectedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    int statusCode = HttpStatus.NOT_FOUND.value();
    @GetMapping
    public String testAPI() {
        return "Web API is working";
    }

    /**
     * ResponseEntity, ? любой Java объект.
     * Возвращает отчет по ID и времени отправки отчета
     * @param idAdoption id записи об усыновлении.
     * @param date       дата отчета.
     * @return отчет.
     * @throws NotFoundException если соответствие не найдено
     */
    @GetMapping(path = "/reportByIdAndDate")
    public ResponseEntity<?> getUsersWithReport(@RequestParam(required = true) Long idAdoption, @RequestParam(required = true) LocalDate date) throws NotFoundException {
        try {
            Report getReport = reportService.getReport(idAdoption, date);
            return new ResponseEntity<>(getReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(statusCode,
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Возвращает полные отчеты пользователя по ID и времени отправки отчета
     *
     * @param idAdoption id записи об усыновлении.
     * @param date       дата отчета.
     * @return полный отчет.
     * @throws NotFoundException если соответствие не найдено
     */
    @GetMapping(path = "/completedReport")
    public ResponseEntity<?> getCompletedReport(@RequestParam(required = true) Long idAdoption, @RequestParam(required = true) LocalDate date) throws NotFoundException {
        try {
            Report getReport = reportService.getCompletedReport(idAdoption, date);
            return new ResponseEntity<>(getReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(statusCode,
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Возвращает список всех имеющихся в базе отчетов
     *
     * @return отчеты имеющиеся в БД.
     * @throws NotFoundException если отчеты в БД отсутствуют
     */
    @GetMapping(path = "/getAllReports")
    public ResponseEntity<?> getAllReport() throws NotFoundException {
        try {
            List<Report> getReport = reportService.getAllReports();
            return new ResponseEntity<>(getReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(statusCode,
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Возвращает отчеты по статусу
     * @param status статус отчета
     * @return отчеты имеющиеся в БД.
     * @throws NotFoundException
     */
    @GetMapping(path = "/getAllReportsBy_Status")
    public ResponseEntity<?> findReportByAdoption_Status(String status) throws NotFoundException {
        try {
            return new ResponseEntity<>(reportService.findReportByAdoption_Status(status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(statusCode,
                    HttpStatus.NOT_FOUND);
        }
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
    @GetMapping("/member/get-by-role")
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
            LOGGER.error("No users with the specified id were found in the members table");
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
    /**
     * Добавляет пользователя в усыновители
     * @throws Exception возвращает если нет пользователя в базе и если ползователь уже усыновитель
     */
    @Operation(
            summary = "Добавить пользователя в таблицу усыновителей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь добавлен"),
                    @ApiResponse(responseCode = "400", description = "Пользователь с такими данными не найден")

            }
    )
    @PutMapping(path = "/add_adoption")
    public ResponseEntity<Adoption> uploadAdoption(@Parameter(description = "ИД чата пользователя") @RequestParam Long idUser,
                                                   @Parameter(description = "код приюта") @RequestParam Long idShelter,
                                                   @Parameter(description = "ИД чата волонтера, если известен") @RequestParam(required = false) Long idVol) throws Exception {
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
            } else throw new NotFoundException("Этот пользователь уже является усыновителем");
        } else throw new NotFoundException("Нет совпадений в таблице members для "
                + idUser.toString() + " " + idShelter.toString() + " USERS");
    }
}//class TelegramBotController
