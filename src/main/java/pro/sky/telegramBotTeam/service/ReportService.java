package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.repository.ReportRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Сервис для работы с отчетами.
 */
@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Возвращает дату последнего заполненного отчета.
     *
     * @param idAdoption id записи об усыновлении.
     * @return дата последнего заполненного отчета. Может вернуть null, если такая запись отсутствует.
     */
    public Date getLastCompletedReportDate(Long idAdoption) {
        return reportRepository.findLastCompletedReportDateByIdAdoption(idAdoption).orElse(null);
    }

    /**
     * Возвращает отчет указанной даты.
     *
     * @param idAdoption id записи об усыновлении.
     * @param date дата отчета.
     * @return отчет. Может вернуть null, если такой отчет отсутствует.
     */
    public Report getReportForDate(Long idAdoption, LocalDate date) {
        return reportRepository.findFirstByAdoption_IdAndReportDate(idAdoption, date).orElse(null);
    }

    /**
     * Возвращает отчет указанной даты.
     * При этом ведется поиск полного отчета, т.е. в нем должны быть заполнены
     * оба поля: изображение и сообщение.
     *
     * @param idAdoption id записи об усыновлении.
     * @param date дата отчета.
     * @return полный отчет. Может вернуть null, если такой отчет отсутствует.
     */
    public Report getCompletedReportForDate(Long idAdoption, LocalDate date) {
        return reportRepository.findCompletedByIdAdoptionAndReportDate(idAdoption, date).orElse(null);
    }

    /**
     * Возвращает все отчеты присутствующие в базе.
     *
     * @return список всех отчетов.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Сохранить/обновить данные отчета.
     *
     * @param report отчет.
     */
    public Report createReport(Report report) {
        Report reportDate = getReportForDate(report.getAdoption().getId(), LocalDate.now());
        if (reportDate == null) {
            LOGGER.info("Добавлен новый отчет");
            return reportRepository.save(report);
        } else {
            LOGGER.info("Отчет обновлен");
            if (report.getReportMessage() != null) {
                reportDate.setReportMessage(report.getReportMessage());
            }
            if (report.getReportImage() != null) {
                reportDate.setReportImage(report.getReportImage());
            }
            return reportRepository.save(reportDate);
        }
    }

    /**
     * Удалить все отчеты, связанные с указанной записью об усыновлении.
     *
     * @param idAdoption id записи об усыновлении.
     */
    @Transactional
    public void deleteReports(Long idAdoption) {
        int count = reportRepository.deleteReportsByIdAdoption(idAdoption);
        LOGGER.info("{} отчетов удалено для записи об усыновлении {}", count, idAdoption);
    }
}
