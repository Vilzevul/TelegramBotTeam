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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameuser;
    private Long idchat;
    private int phone;
    private int idmenu;
    private int role;

    public Users(Long idchat, String nameuser, int phone, int idmenu, int role) {
        this.idchat = idchat;
        this.nameuser = nameuser;
        this.phone = phone;
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

    public Long getIdchat() {
        return idchat;
    }

    public void setIdchat(Long idchat) {
        this.idchat = idchat;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(int idmenu) {
        this.idmenu = idmenu;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.idmenu = role;
    }

    public String toString() {
        return "Users{" +
                "id=" + id +
                ", idchat='" + idchat + '\'' +
                ", nameuser='" + nameuser + '\'' +
                ", phone='" + phone + '\'' +
                ", idmenu='" + idmenu + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users task = (Users) o;
        return idchat == task.idchat && id.equals(task.id) && nameuser.equals(task.nameuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idchat, nameuser, phone, idmenu, role);
    }

}
