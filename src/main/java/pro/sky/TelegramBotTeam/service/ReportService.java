package pro.sky.TelegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.ReportRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Добавить отчет.
     *
     * @param report отчет.
     */
    public Report addReport(Report report) {
        reportRepository.findByReportDate(LocalDate.now()).
                orElse(new Report(report.getAdoption(), report.getReportDate(), report.getReportImage(), report.getReportMessage()));
        return reportRepository.save(report);
    }


    public List<Report> getReportByDate(LocalDate date) {
        return reportRepository.findAll().stream().
                filter(v -> v.getReportDate().equals(date)).
                collect(Collectors.toList());

    }

    /**
     * Получить дату последнего отчета.
     *
     * @param idAdoption id записи об усыновлении.
     * @return дата последнего отчета. Может вернуть null, если такая запись отсутствует.
     */
    public Date getLastReportDate(Long idAdoption) {
        return reportRepository.findMaxReportDateByIdAdoption(idAdoption).orElse(null);
    }
}
