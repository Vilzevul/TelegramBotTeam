package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Report;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT * FROM reports WHERE id_adoption = :idAdoption AND report_date = :date LIMIT 1", nativeQuery = true)
    Optional<Report> findByIdAdoptionAndReportDate(Long idAdoption, LocalDate date);

    @Query(value = "SELECT * FROM reports WHERE id_adoption = :idAdoption AND report_date = :date AND " +
            "report_image IS NOT NULL AND report_message IS NOT NULL LIMIT 1", nativeQuery = true)
    Optional<Report> findCompletedByIdAdoptionAndReportDate(Long idAdoption, LocalDate date);

    @Query(value = "SELECT MAX(report_date) from reports WHERE id_adoption = :idAdoption AND " +
            "report_image IS NOT NULL AND report_message IS NOT NULL", nativeQuery = true)
    Optional<Date> findLastCompletedReportDateByIdAdoption(Long idAdoption);

    @Modifying
    @Query(value = "DELETE FROM reports WHERE id_adoption = :idAdoption", nativeQuery = true)
    int deleteReportsByIdAdoption(Long idAdoption);
}
