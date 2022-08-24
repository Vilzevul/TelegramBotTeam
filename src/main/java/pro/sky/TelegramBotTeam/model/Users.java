package pro.sky.TelegramBotTeam.model;
import javax.persistence.*;
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;
    private Long messageId;
    private String text;

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long id) {
        this.UserId = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
