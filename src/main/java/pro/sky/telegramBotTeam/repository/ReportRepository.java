package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Report;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * @deprecated use {@link #findByReportDateAndAdoption_Id(LocalDate, Long)}
     */
    @Query(value = "SELECT * FROM reports WHERE report_date = :date LIMIT 1", nativeQuery = true)
    Optional <Report> findByReportDateSql(LocalDate date);


    @Query(value = "SELECT * FROM reports WHERE report_date = :reportDate AND id_adoption = :adoption_id LIMIT 1", nativeQuery = true)
    Optional <Report> findByReportDateAndAdoption_Id(LocalDate reportDate, Long adoption_id);
}
