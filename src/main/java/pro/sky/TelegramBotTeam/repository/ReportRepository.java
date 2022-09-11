package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.TelegramBotTeam.model.Report;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT MAX(report_date) from reports WHERE id_adoption = :idAdoption", nativeQuery = true)
    Optional<Date> findMaxReportDateByIdAdoption(Long idAdoption);

   Optional<Report> findByReportDate(LocalDate date);
}
