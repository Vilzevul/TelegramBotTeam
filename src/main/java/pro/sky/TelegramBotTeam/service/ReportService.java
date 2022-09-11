package pro.sky.TelegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.repository.ReportRepository;

import java.util.Date;

@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Добавить отчет.
     * @param report отчет.
     */
    public void addReport(Report report) {
        reportRepository.save(report);
    }

    /**
     * Получить дату последнего отчета.
     * @param idAdoption id записи об усыновлении.
     * @return дата последнего отчета. Может вернуть null, если такая запись отсутствует.
     */
    public Date getLastReportDate(Long idAdoption) {
        return reportRepository.findMaxReportDateByIdAdoption(idAdoption).orElse(null);
    }
}
