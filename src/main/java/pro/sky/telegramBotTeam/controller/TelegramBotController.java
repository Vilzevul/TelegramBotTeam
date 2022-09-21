package pro.sky.telegramBotTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.exception.NotFoundException;
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
    int statusCode = HttpStatus.NOT_FOUND.value();
    @GetMapping
    public String testAPI() {
        return "Web API is working";
    }

    /**
     * ResponseEntity, где ? понимается любой Java объект.
     * Возвращает отчет по ID и времени отправки отчета
     * @param idAdoption id записи об усыновлении.
     * @param date дата отчета.
     * @return отчет.
     * @throws NotFoundException если соответствие не найдено
     */
    @GetMapping(path = "/reportByIdAndDate")
    public ResponseEntity<?> getUsersWithReport(@RequestParam(required = true) Long idAdoption, @RequestParam(required = true) LocalDate date)  throws NotFoundException {
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
     * @param idAdoption id записи об усыновлении.
     * @param date дата отчета.
     * @return полный отчет.
     * @throws NotFoundException если соответствие не найдено
     */
    @GetMapping(path = "/completedReport")
    public ResponseEntity<?> getCompletedReport(@RequestParam(required = true) Long idAdoption, @RequestParam(required = true) LocalDate date)  throws NotFoundException {
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
     * @return отчеты имеющиеся в БД.
     * @throws NotFoundException если отчеты в БД отсутствуют
     */
    @GetMapping(path = "/getAllReports")
    public ResponseEntity<?> getAllReport()  throws NotFoundException {
        try {
            List<Report> getReport = reportService.getAllReports();
            return new ResponseEntity<>(getReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(statusCode,
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/getAllReportsBy_Status")
    public ResponseEntity<?> findReportByAdoption_Status(String status)  throws NotFoundException {
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
      return  adoptionService.getAdoption(id);
    }

    /**
     * Обновляет статус усыновителя
     */
    @PutMapping("/adoption/{id},{status}")
    public String updateAdoptionStatus(@PathVariable Long id, @PathVariable Adoption.AdoptionStatus status) throws Exception {
        return  adoptionService.updateAdoptionStatus(id, status);
    }

    @GetMapping("/adoption/{id},{idShelter}")
    public   Optional<Adoption> searchAdoptionStatus(@PathVariable Long id, @PathVariable int idShelter) throws Exception {
        return  adoptionService.searchAdoptionStatus(id, idShelter);
    }
}
