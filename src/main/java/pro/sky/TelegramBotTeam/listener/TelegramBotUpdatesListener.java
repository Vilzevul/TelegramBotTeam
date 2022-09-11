package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.api.KeyBoardButton;
import pro.sky.TelegramBotTeam.model.Adoption;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.service.AdoptionService;
import pro.sky.TelegramBotTeam.service.ReportService;
import pro.sky.TelegramBotTeam.service.UsersService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static pro.sky.TelegramBotTeam.api.Code.*;

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
                    KeyBoardButton.CATMAIN,
                    KeyBoardButton.CONTACTS,
                    KeyBoardButton.START -> {
                Users users = new Users(userId, userName,userContacts, Users.UserRole.USER);
                usersService.createUsersAll(users);
            }

            //Блок отправки отчета
            //Пользователь отправляет фото
            case KeyBoardButton.DOGSEND_MSG -> {
                try {
                    if (userFileId != null) {
                        byte[] reportContent = getFileContent(telegramBot, userFileId);
                        //reportContent сохраняем в БД
                        btnCommand = KeyBoardButton.DOGSEND_TXT;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = (reportContent != null) ? "❗️Файл принят\n" : "❌  Это не фото отчета\n";
                        message += keyBoardButton.getMessage(btnCommand);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case KeyBoardButton.DOGSEND_TXT -> {  }
            default -> {
                //Пользователь отправляет текст
                if (btnStatus.equals(KeyBoardButton.DOGSEND_TXT)) {
                    String reportText = message;
                    //reportText сохраняем в БД
                    btnCommand = KeyBoardButton.DOGMAIN;
                    btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                    message = "❗️Отчет принят\n";

                    Adoption adoption = adoptionService.getAdoption(userId);
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        Date lastReportDate = reportService.getLastReportDate(adoption.getId());
                        reportService.addReport(new Report(adoption, new Date(), null, reportText));
                    }
                    //Конец блока отправки отчета
                }
            }
        }// switch (btnCommand)


        if (message.equals("/start")) {
            telegramBot.execute(new SendMessage(userId, userName + ", привет!")
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .parseMode(ParseMode.HTML));
        } else {
            telegramBot.execute(new SendMessage(userId, message)
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                    .parseMode(ParseMode.HTML)
            );
        }

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
}