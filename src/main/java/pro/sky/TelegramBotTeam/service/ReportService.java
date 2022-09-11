package pro.sky.TelegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.repository.ReportRepository;

@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void addReport(Report report) {
        reportRepository.save(report);
    }
}
