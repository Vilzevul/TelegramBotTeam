package pro.sky.TelegramBotTeam.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usersmenu")
public class UsersMenu {
    @Id
    private Long id;
    private String nameuser;
    private String idmenu;
    private String role;

    public UsersMenu(Long id, String nameuser, String idmenu, String role) {
        this.id = id;
        this.nameuser = nameuser;
        this.idmenu = idmenu;
        this.role = role;
    }

    public UsersMenu() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(String idmenu) {
        this.idmenu = idmenu;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}


