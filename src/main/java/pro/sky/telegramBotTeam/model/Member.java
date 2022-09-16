package pro.sky.telegramBotTeam.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "members")
@DynamicInsert
public class Member {
    public enum MemberRole {
        USER,
        VOLUNTEER,
        ADOPTION
    }

    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_shelter")
    private Shelter shelter;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private MemberRole role;

    public Member(Users user, Shelter shelter) {
        this.user = user;
        this.shelter = shelter;
    }

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", user=" + user +
                ", shelter=" + shelter +
                ", role=" + role +
                '}';
    }
}
