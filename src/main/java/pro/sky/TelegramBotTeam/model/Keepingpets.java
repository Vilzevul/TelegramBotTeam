package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

@Entity
@Table(name = "keepingpets")
public class Keepingpets {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "idpets")
    private Long idpets;

    @Column(name = "idusers")
    private Long idusers;

    @Column(name = "foto")
    private String foto;

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

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
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
