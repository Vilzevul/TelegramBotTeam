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
    private Long idchat;
    private String nameuser;
    private int phone;
    private int role;

    @OneToOne(mappedBy = "users")//один отчет на 1-го пользователя
    private Report report;

    public Users(Long idchat, String nameuser, int phone, int role) {
        this.idchat = idchat;
        this.nameuser = nameuser;
        this.phone = phone;
        this.role = role;
    }

    public Users() {
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String toString() {
        return "Users{" +
                ", idchat='" + idchat + '\'' +
                ", nameuser='" + nameuser + '\'' +
                ", phone ='" + phone + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users task = (Users) o;
        return idchat == task.idchat && nameuser.equals(task.nameuser) && phone == task.phone && role == task.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idchat, nameuser, phone, role);
    }

}
