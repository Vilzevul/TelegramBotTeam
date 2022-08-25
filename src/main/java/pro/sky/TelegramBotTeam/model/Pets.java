package pro.sky.TelegramBotTeam.model;

import javax.persistence.*;

@Entity
@Table(name = "pets")
public class Pets {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private Long type;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Long age;

    @Column(name = "comment")
    private String comment;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return this.age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
