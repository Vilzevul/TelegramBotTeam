package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Report;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT * FROM reports WHERE id_adoption = :idAdoption AND report_date = :date LIMIT 1", nativeQuery = true)
    Optional<Report> findByIdAdoptionAndReportDate(Long idAdoption, LocalDate date);
}
