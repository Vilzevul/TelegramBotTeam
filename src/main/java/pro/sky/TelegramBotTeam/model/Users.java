package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "iduser")//он же id chat
    private Long idUser;

    @Column(name = "nameuser")
    private String nameuser;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private int role;

    public Users(Long idUser, String nameuser, String phone, int role) {
        this.idUser = idUser;
        this.nameuser = nameuser;
        this.phone = phone;
        this.role = role;
    }

    public Users() {

    }

    public Long getIduser() {
        return this.idUser;
    }

    public void setIduser(Long iduser) {
        this.idUser = idUser;
    }

    public String getNameuser() {
        return this.nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return this.role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users users)) return false;
        return role == users.role && idUser.equals(users.idUser) && nameuser.equals(users.nameuser) && Objects.equals(phone, users.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, nameuser, phone, role);
    }

    @Override
    public String toString() {
        return "Users{" +
                "idUser=" + idUser +
                ", nameuser='" + nameuser + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}
