package pro.sky.TelegramBotTeam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

/**
 * Класс "Report" cоответствует структуре и названию таблицы "keepingpets" БД myDb
 * Используется для получения и сохранения в БД myDb отчета пользователей о питомце
 */
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idpets")
    private Long idpets;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Users users;

    @Column(name = "idusers")
    private Long idusers;

    @Column(name = "foto")
    private byte[] foto;
    @Column(name = "diet")
    private String diet;

    @Column(name = "wellbeing")
    private String wellbeing;

    @Column(name = "behavior")
    private String behavior;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getDiet() {
        return this.diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getWellbeing() {
        return this.wellbeing;
    }

    public void setWellbeing(String wellbeing) {
        this.wellbeing = wellbeing;
    }

    public String getBehavior() {
        return this.behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
