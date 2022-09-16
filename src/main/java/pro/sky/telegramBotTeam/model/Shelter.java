package pro.sky.telegramBotTeam.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shelters")
@DynamicInsert
public class Shelter {
    public enum ShelterType {
        DOGS,
        CATS
    }

    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DOGS'")
    private ShelterType name;

    public Shelter(ShelterType name) {
        this.name = name;
    }

    public Shelter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShelterType getName() {
        return name;
    }

    public void setName(ShelterType name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id.equals(shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
