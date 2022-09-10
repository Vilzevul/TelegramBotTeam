package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idadoptiveparent")
    private Long idadoptiveparent;

    @Column(name = "report_date")
    private java.sql.Date reportDate;

    @Column(name = "report_image")
    private byte[] report_image;

    @Column(name = "report_message")
    private String reportMessage;


    public Long getIdreport() {
        return this.id;
    }

    public void setIdreport(Long idreport) {
        this.id = idreport;
    }

    public Long getIdadoptiveparent() {
        return this.idadoptiveparent;
    }

    public void setIdadoptiveparent(Long idadoptiveparent) {
        this.idadoptiveparent = idadoptiveparent;
    }

    public java.sql.Date getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(java.sql.Date reportDate) {
        this.reportDate = reportDate;
    }

    public byte[] getReport_image() {
        return report_image;
    }

    public void setReportImage(byte[] report_image) {
        this.report_image = report_image;
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
        return id.equals(report.id) && idadoptiveparent.equals(report.idadoptiveparent) && reportDate.equals(report.reportDate) && Arrays.equals(report_image, report.report_image) && Objects.equals(reportMessage, report.reportMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, idadoptiveparent, reportDate, reportMessage);
        result = 31 * result + Arrays.hashCode(report_image);
        return result;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", idadoptiveparent=" + idadoptiveparent +
                ", reportDate=" + reportDate +
                ", report_image=" + Arrays.toString(report_image) +
                ", reportMessage='" + reportMessage + '\'' +
                '}';
    }
}
