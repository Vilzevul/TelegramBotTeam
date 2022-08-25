package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

@Entity
@Table(name = "directory")
public class Directory {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
