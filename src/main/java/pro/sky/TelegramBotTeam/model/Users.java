package pro.sky.TelegramBotTeam.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@DynamicInsert
public class Users {
    public enum UserRole {
        USER,
        VOLUNTEER,
        ADOPTION
    }

    @Id
    @Column(name = "id")
    //Он же id chat
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private UserRole role;

    public Users(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Users(Long id, String name, String phone, UserRole role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Users() {
    }

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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users users)) return false;
        return id.equals(users.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}
