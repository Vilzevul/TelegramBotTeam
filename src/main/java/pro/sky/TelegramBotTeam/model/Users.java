package pro.sky.TelegramBotTeam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Содержит информацию поступивших сообщений от пользователей
 */
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    private String nameuser;
    private Long idchat;
    private String menu;
    private int role;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private Collection<Report> report;

    public Users(Long idchat, String nameuser, String menu, int role) {
        this.idchat = idchat;
        this.nameuser = nameuser;
        this.menu = menu;
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

    public String getmenu() {
        return menu;
    }

    public void setmenu(String menu) {
        this.menu = menu;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String toString() {
        return "Users{" +
                "id=" + id +
                ", idchat='" + idchat + '\'' +
                ", nameuser='" + nameuser + '\'' +
                ", menu='" + menu + '\'' +
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
        return Objects.hash(id, idchat, nameuser, menu, role);
    }

}
