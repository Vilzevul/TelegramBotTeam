package pro.sky.telegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.api.KeyBoardButton;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.model.Users;
import pro.sky.telegramBotTeam.service.AdoptionService;
import pro.sky.telegramBotTeam.service.ReportService;
import pro.sky.telegramBotTeam.service.UsersService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static pro.sky.telegramBotTeam.api.Code.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final KeyBoardButton keyBoardButton;
    private final UsersService usersService;
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final TelegramBot telegramBot;

    String userContacts = null;
    String userFileId = null;
    String btnStatus = "undefined";
    String btnCommand = "undefined";

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KeyBoardButton keyBoardButton,
                                      UsersService usersService,
                                      AdoptionService adoptionService,
                                      ReportService reportService) {
        this.telegramBot = telegramBot;
        this.keyBoardButton = keyBoardButton;
        this.usersService = usersService;
        this.adoptionService = adoptionService;
        this.reportService = reportService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Возвращает пользователя, который прислал сообщение/нажал на кнопку.
     *
     * @param update обновления в чате телеграмм-бота.
     * @return пользователь, инициирующий обновление. Может быть null.
     * @throws NullPointerException параметр <code>update</code> равен null.
     */
    @Nullable
    private User getUpdates(Update update) {
        userContacts = null;
        userFileId = null;

        if (update == null) {
            LOGGER.error("Update structure is null");
            throw new NullPointerException("Update structure is null");
        }

        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            btnCommand = (callbackQuery.data() == null) ? "undefined" : callbackQuery.data();
            LOGGER.info("- getUpdates(callbackQuery) - " + btnCommand);
            return callbackQuery.from();
        }

        Message message = update.message();
        if (message != null) {
            if (message.contact() != null) {
                btnCommand = KeyBoardButton.CONTACTS;
                userContacts = message.contact().phoneNumber();
                return message.from();
            }

            if (message.document() != null) {
                btnCommand = btnCommand;
                userFileId = message.document().fileId();
                return message.from();
            }

            if (message.photo() != null) {
                btnCommand = btnCommand;
                userFileId = message.photo()[message.photo().length - 1].fileId();
                return message.from();
            }

            if (message.text() != null) {
                btnCommand = message.text();
                return message.from();
            }
        } else {
            btnCommand = "undefined";
        }

        LOGGER.info("getUpdates: {}", update);
        return null;
    }

    /**
     * Обрабатывает сообщение от пользователя.
     *
     * @param user пользователь.
     * @throws NullPointerException параметр <code>user</code> равен null.
     */
    private void makeProcess(User user) {
        if (user == null) {
            LOGGER.error("User is null");
            throw new NullPointerException("User is null");
        }

        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
        String btnMessage = keyBoardButton.getMessage(btnCommand);
        String message = (btnMessage != null) ? btnMessage : btnCommand;

        LOGGER.info("begin makeProcess - команда: {} статус: {} текст: {}", btnCommand, btnStatus, message);

        Long userId = user.id();
        String userName = user.firstName();

        switch (btnCommand) {
            //Запись в БД
            case KeyBoardButton.DOGMAIN,
                    KeyBoardButton.CATMAIN -> {
                Users users = new Users(userId, userName, userContacts);
                usersService.createUser(users);
            }
            case KeyBoardButton.SERVICE -> {
                String messageService = (btnMessage != null) ? btnMessage : btnCommand;

                List<Users> VolunteersList = usersService.getUsersByRole(Users.UserRole.VOLUNTEER);
                Random random = new Random();

                if (!VolunteersList.isEmpty()) {
                    Integer randomVolunteer = (random.nextInt(VolunteersList.size()));

                    if (VolunteersList.size() == 1) {
                        Long ID = VolunteersList.get(0).getId();
                        LOGGER.info("ID волонтера: " + ID);
                        telegramBot.execute(new SendMessage(ID,
                                "Ваше сообщение отправленно: " + messageService)
                                .parseMode(ParseMode.HTML));
                    } else if (VolunteersList.size() > 1) {
                        LOGGER.info("Сообщение: ", messageService, randomVolunteer);
                        telegramBot.execute(new SendMessage(VolunteersList.get(randomVolunteer).getId(),
                                "Сообщение: " + messageService)
                                .parseMode(ParseMode.HTML));
                    }
                } else if (VolunteersList.isEmpty()) {
                    message = "Свободных волонтеров нет. Попробуйте связаться позже";
                    telegramBot.execute(new SendMessage(userId, message)
                            .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                            .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                            .parseMode(ParseMode.HTML));
                }
            }

            //Блок отправки отчета
            //Пользователь отправляет фото
            case KeyBoardButton.DOGSEND_MSG,
                    KeyBoardButton.CATSEND_MSG -> {
                try {
                    Adoption adoption = adoptionService.getAdoption(userId);
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        if (userFileId != null) {
                            byte[] reportContent = getFileContent(telegramBot, userFileId);
                            //reportContent сохраняем в БД
                            if (reportContent != null) {
                                switch (btnCommand) {
                                    case KeyBoardButton.DOGSEND_MSG -> {

                                        Report report = new Report(adoption, LocalDate.now(), reportContent, null);
                                        report = reportService.createReport(report);
                                        btnCommand = KeyBoardButton.DOGSEND_TXT;
                                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                                        message = "❗️Файл принят\n";
                                        message += keyBoardButton.getMessage(btnCommand);
                                        LOGGER.info("report: {}", report);
                                    }
                                    case KeyBoardButton.CATSEND_MSG -> {
                                        btnCommand = KeyBoardButton.CATSEND_TXT;
                                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                                        message = "❗️Файл принят\n";
                                        message += keyBoardButton.getMessage(btnCommand);
                                    }
                                }//switch
                            }//if (reportContent != null)
                        }
                    } else {
                        btnCommand = KeyBoardButton.DOGMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет\n";
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case KeyBoardButton.DOGSEND_TXT,
                    KeyBoardButton.CATSEND_TXT -> {
            }
            default -> {
                //Пользователь отправляет текст
                if (btnStatus.equals(KeyBoardButton.DOGSEND_TXT)) {
                    String reportText = message;
                    Adoption adoption = adoptionService.getAdoption(userId);
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        Report report = new Report(adoption, LocalDate.now(), null, reportText);
                        report = reportService.createReport(report);
                        btnCommand = KeyBoardButton.DOGMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "❗️Отчет принят\n";
                        LOGGER.info("report: {}", report);
                    } else {
                        btnCommand = KeyBoardButton.DOGMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет";
                    }
                }
                if (btnStatus.equals(KeyBoardButton.CATSEND_TXT)) {
                    String reportText = message;
                    Adoption adoption = adoptionService.getAdoption(userId);
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        btnCommand = KeyBoardButton.CATMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "❗️Отчет принят\n";
                    } else {
                        btnCommand = KeyBoardButton.CATMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет";
                    }
                }


            }//default
        }//case

        switch (btnCommand) {
            case KeyBoardButton.START -> {
                Users users = new Users(userId, userName, userContacts);
                usersService.createUser(users);
                telegramBot.execute(new SendMessage(userId, userName + ", привет!")
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                        .parseMode(ParseMode.HTML));
            }
            //main keyBoard
            case KeyBoardButton.CONTACTS,
                    KeyBoardButton.ESCAPE -> {
                message = keyBoardButton.getMessage(btnCommand);
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup()));
                btnCommand = btnStatus;
                message = keyBoardButton.getMessage(btnCommand);
                if (btnCommand == KeyBoardButton.CONTACTS) message = "Продолжить работу с программой";
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                        .parseMode(ParseMode.HTML));


            }

            case KeyBoardButton.INLINECONTACTS -> {
                telegramBot.execute(new SendMessage(userId, KeyBoardButton.CONTACTS)
                        .replyMarkup(keyBoardButton.getContactKeyboardMarkup())
                );
            }
            default -> {
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                        .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                        .parseMode(ParseMode.HTML)
                );
            }//default

        }//case
        LOGGER.info("end makeProcess - команда: {} статус: {} текст: {}", btnCommand, btnStatus, message);
        LOGGER.info("end makeProcess - file id: {} ", userFileId);
    }

    @Override
    public int process(List<Update> updates) throws RuntimeException {
        updates.forEach(update -> {
            LOGGER.info("Processing update: {}", update);
            if (update != null) {
                User user = getUpdates(update);
                if (user != null) {
                    makeProcess(user);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void addAdoptions() {
        List<Users> usersList = usersService.getUsersByRole(Users.UserRole.ADOPTION);
        if (!usersList.isEmpty()) {
            Users usersAdaptive = usersList.get(0);
            usersAdaptive.setRole(Users.UserRole.USER);
            usersAdaptive = usersService.createUser(usersAdaptive);

            Adoption adoption = new Adoption();
            LocalDate date = LocalDate.now();
            LocalDate date30 = date.plusDays(30);

            adoption.setParent(usersAdaptive);
            adoption.setVolunteer(usersAdaptive);
            adoption.setStartDate(java.sql.Date.valueOf(date));
            adoption.setEndDate(java.sql.Date.valueOf(date30));
            LOGGER.info("Users: {}", usersAdaptive);
            LOGGER.info("adoption: {}", adoption);
            adoptionService.createAdoption(adoption);
        }
    }

    /**
     * Конвертировать Date в LocalDate.
     *
     * @param date дата типа Date.
     * @return дата типа LocalDate. Если date = null, возвращает null.
     */
    private LocalDate convertDateToLocalDate(Date date) {
        return (date == null) ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Ежедневно в 00:00 проходится по всем усыновителям и рассылает
     * сообщения участникам в соответствии с текущим статусом усыновления.
     */
    @Scheduled(cron = "@daily")
    public void notifyParticipantsOfAdoption() {
        adoptionService.getAllAdoptions().forEach(adoption -> {
            final Long volunteerId = adoption.getVolunteer().getId();
            final Long parentId = adoption.getParent().getId();
            final String parentName = adoption.getParent().getName();

            switch (adoption.getStatus()) {
                case SUCCESS -> {
                    telegramBot.execute(new SendMessage(parentId,
                            "Поздравляем, " + parentName + "! Вы прошли испытательный период")
                            .parseMode(ParseMode.HTML));
                    adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.NOT_ACTIVE);
                    reportService.deleteReports(adoption.getId());
                }

                case FAILED -> {
                    telegramBot.execute(new SendMessage(parentId,
                            "Сожалеем, " + parentName + "! Вы не прошли испытательный период")
                            .parseMode(ParseMode.HTML));
                    adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.NOT_ACTIVE);
                    reportService.deleteReports(adoption.getId());
                }

                case DECIDE -> {
                    telegramBot.execute(new SendMessage(volunteerId,
                            "Испытательный период для усыновителя [" + parentId + "] закончился. " +
                                    "Требуется Ваше решение о дальнейшей судьбе питомца")
                            .parseMode(ParseMode.HTML));
                }

                case ACTIVE -> {
                    final LocalDate currentDate = LocalDate.now();
                    final LocalDate adoptionStartDate = convertDateToLocalDate(adoption.getStartDate());
                    final LocalDate adoptionEndDate = convertDateToLocalDate(adoption.getEndDate());

                    if (currentDate.isAfter(adoptionEndDate)) {
                        telegramBot.execute(new SendMessage(parentId,
                                "Ваш испытательный период закончился, ожидается решение волонтера")
                                .parseMode(ParseMode.HTML));
                        adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.DECIDE);
                    } else {
                        //Если пользователь в текущие сутки уже слал отчет - не беспокоим его.
                        //Иначе шлем стандартную напоминалку
                        Report todayReport = reportService.getReport(adoption.getId(), currentDate);
                        if (todayReport == null) {
                            telegramBot.execute(new SendMessage(parentId,
                                    "Пожалуйста, не забудьте прислать отчет о самочувствии питомца")
                                    .parseMode(ParseMode.HTML));

                            //Если отчетов нет уже более 2 дней - даем знать об этой ситуации волонтерам
                            LocalDate lastReportDate = convertDateToLocalDate(reportService.getLastReportDate(adoption.getId()));
                            final boolean notifyVolunteer = (lastReportDate == null) ?
                                    currentDate.minusDays(2).isAfter(adoptionStartDate) :
                                    (ChronoUnit.DAYS.between(lastReportDate, currentDate) >= 2);

                            if (notifyVolunteer) {
                                telegramBot.execute(new SendMessage(volunteerId,
                                        "Усыновитель [" + parentId + "] уже более двух дней не присылает отчет о питомце")
                                        .parseMode(ParseMode.HTML));
                            }
                        }
                    }
                }
            }
        });
    }
}