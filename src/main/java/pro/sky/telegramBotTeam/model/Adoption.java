package pro.sky.telegramBotTeam.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "adoptions")
@DynamicInsert
public class Adoption {
    public Adoption(Adoption adoption) {

    }

    public Adoption() {

    }

    public enum AdoptionStatus {
        NOT_ACTIVE,     //Испытательный период неактивен (запись для отслеживания статистики)
        ACTIVE,         //Испытательный период активен, от усыновителя ожидаются ежедневные отчеты
        DECIDE,         //Испытательный период закончился, по пользователю ожидается решение волонтера
        SUCCESS,        //Усыновитель не прошел испытательный период
        FAILED          //Усыновитель не прошел испытательный период
    }

    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Users parent;

    @ManyToOne
    @JoinColumn(name = "id_volunteer")
    private Users volunteer;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private AdoptionStatus status;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getParent() {
        return this.parent;
    }

    public void setParent(Users parent) {
        this.parent = parent;
    }

    public Users getVolunteer() {
        return this.volunteer;
    }

    public void setVolunteer(Users volunteer) {
        this.volunteer = volunteer;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public AdoptionStatus getStatus() {
        return this.status;
    }

    public void setStatus(AdoptionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adoption that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AdoptiveParent{" +
                "id=" + id +
                ", parent=" + parent +
                ", volunteer=" + volunteer +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
