package pro.sky.TelegramBotTeam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Класс "Report" cоответствует структуре и названию таблицы "keepingpets" БД myDb
 * Используется для получения и сохранения в БД myDb отчета пользователей о питомце
 */
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "report_id")
    private Users users;

    @Column(name = "report_image")
    private byte[] report_image;

    private LocalDateTime report_date;
    private String report_message;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getReport_image() {
        return report_image;
    }

    public void setReport_image(byte[] report_image) {
        this.report_image = report_image;
    }

    public LocalDateTime getReport_date() {
        return report_date;
    }

    public void setReport_date(LocalDateTime report_date) {
        this.report_date = report_date;
    }

    public String getReport_message() {
        return report_message;
    }

    public void setReport_message(String report_message) {
        this.report_message = report_message;
    }

}
