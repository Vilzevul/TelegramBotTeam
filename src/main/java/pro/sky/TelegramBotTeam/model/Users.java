package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Содержит информацию поступивших сообщений от пользователей
 */
@Entity
@Table(name = "users")
public class Users {
    @Id
    private Long id;
    private String nameuser;
    private String idmenu;
    private String role;

    public Users(Long id, String nameuser, String idmenu, String role) {
        this.id = id;
        this.nameuser = nameuser;
        this.idmenu = idmenu;
        this.role = role;
    }

    public Users() {

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
