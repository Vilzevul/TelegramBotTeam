/*
package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

@Entity
@Table(name = "adoptiveParent")
public class AdoptiveParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long id_volunteer;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name="id_user", referencedColumnName="idchat"),
            @JoinColumn(name="id_volunteer", referencedColumnName="idchat")
    })
    private Users users;

    int start_date;
    int end_date;
    int status;

    public AdoptiveParent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_volunteer() {
        return id_volunteer;
    }

    public void setId_volunteer(Long id_volunteer) {
        this.id_volunteer = id_volunteer;
    }

    public int getStart_date() {
        return start_date;
    }

    public void setStart_date(int start_date) {
        this.start_date = start_date;
    }

    public int getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
*/
