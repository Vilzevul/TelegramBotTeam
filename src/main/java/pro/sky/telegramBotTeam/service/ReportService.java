package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.repository.ReportRepository;

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
     * Возвращает отчет указанной даты.
     *
     * @param date дата отчета.
     * @return отчет. Может вернуть null, если такой отчет отсутствует.
     */
    public Report getReport(LocalDate date) {
        return reportRepository.findByReportDateSql(date).orElse(null);
    }
public Report getReportDateAdopt(LocalDate reportDate, Long adoption_id ){
        return reportRepository.findByReportDateAndAdoption_Id(reportDate,adoption_id).orElse(null);
}
    /**
     * Сохранить/обновить данные отчета.
     * @param report отчет.
     */
    public Report createReport(Report report){
        Report reportDate = getReportDateAdopt(LocalDate.now(),report.getAdoption().getId());
        if (reportDate == null) {
            LOGGER.info("Добавлен новый отчет");
            return reportRepository.save(report);
        } else {
            LOGGER.info("Отчет обновлен");
            if(report.getReportMessage() != null) {
                reportDate.setReportMessage(report.getReportMessage());
            }
            if(report.getReportImage() != null) {
                reportDate.setReportImage(report.getReportImage());
            }
            return reportRepository.save(reportDate);
        }
    }
}
