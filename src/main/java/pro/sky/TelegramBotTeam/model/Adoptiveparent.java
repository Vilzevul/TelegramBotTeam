package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "adoptiveparent")
public class Adoptiveparent {
    @Id
    @Column(name = "idadoptiveparent")
    private Long idadoptiveparent;

    @Column(name = "idvolunteer")
    private Long idvolunteer;

    @Column(name = "start_date")
    private java.sql.Date startDate;

    @Column(name = "end_date")
    private java.sql.Date endDate;

    @Column(name = "status")
    private Long status;

    public Long getIdadoptiveparent() {
        return this.idadoptiveparent;
    }

    public void setIdadoptiveparent(Long idadoptiveparent) {
        this.idadoptiveparent = idadoptiveparent;
    }

    public Long getIdvolunteer() {
        return this.idvolunteer;
    }

    public void setIdvolunteer(Long idvolunteer) {
        this.idvolunteer = idvolunteer;
    }

    public java.sql.Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adoptiveparent that)) return false;
        return idadoptiveparent.equals(that.idadoptiveparent) && idvolunteer.equals(that.idvolunteer) && startDate.equals(that.startDate) && endDate.equals(that.endDate) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idadoptiveparent, idvolunteer, startDate, endDate, status);
    }

    @Override
    public String toString() {
        return "Adoptiveparent{" +
                "idadoptiveparent=" + idadoptiveparent +
                ", idvolunteer=" + idvolunteer +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
