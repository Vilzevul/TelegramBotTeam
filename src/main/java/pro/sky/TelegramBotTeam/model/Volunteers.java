package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

@Entity
@Table(name = "volunteers")
public class Volunteers {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "fio")
    private String fio;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return this.fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
