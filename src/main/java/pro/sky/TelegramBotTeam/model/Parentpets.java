package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

/**
 * Использется для получения информации о владельце питомца
 */
@Entity
@Table(name = "parentpets")
public class Parentpets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idpets")
    private Long idpets;

    @Column(name = "idusers")
    private Long idusers;

    @Column(name = "fio")
    private String fio;

    @Column(name = "mail")
    private String mail;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdpets() {
        return this.idpets;
    }

    public void setIdpets(Long idpets) {
        this.idpets = idpets;
    }

    public Long getIdusers() {
        return this.idusers;
    }

    public void setIdusers(Long idusers) {
        this.idusers = idusers;
    }

    public String getFio() {
        return this.fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
