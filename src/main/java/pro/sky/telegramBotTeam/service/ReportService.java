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
     * Возвращает отчет указанной даты для указанной записи по усыновлению.
     *
     * @param date дата отчета.
     * @param idAdoption id записи об усыновлении.
     * @return отчет. Может вернуть null, если такой отчет отсутствует.
     */
    public Report getReport(Long idAdoption, LocalDate date) {
        return reportRepository.findByIdAdoptionAndReportDate(idAdoption, date).orElse(null);
    }

    /**
     * Сохранить/обновить данные отчета.
     * @param report отчет.
     */
    public Report createReport(Report report){
        Report reportDate = getReport(report.getAdoption().getId(), LocalDate.now());
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
