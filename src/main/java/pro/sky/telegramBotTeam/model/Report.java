package pro.sky.telegramBotTeam.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_adoption")
    private Adoption adoption;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "report_image")
    private byte[] reportImage;

    @Column(name = "report_message")
    private String reportMessage;

    public Report(Adoption adoption, LocalDate reportDate, byte[] reportImage, String reportMessage) {
        this.reportDate = reportDate;
        this.adoption = adoption;
        this.reportImage = reportImage;
        this.reportMessage = reportMessage;
    }

    public Report() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adoption getAdoption() {
        return this.adoption;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public byte[] getReportImage() {
        return reportImage;
    }

    public void setReportImage(byte[] reportImage) {
        this.reportImage = reportImage;
    }

    public String getReportMessage() {
        return this.reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report report)) return false;
        return id.equals(report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", adoption=" + adoption +
                ", reportDate=" + reportDate +
                ", reportImage=" + Arrays.toString(reportImage) +
                ", reportMessage='" + reportMessage + '\'' +
                '}';
    }
}
