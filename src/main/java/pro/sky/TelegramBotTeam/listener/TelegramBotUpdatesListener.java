package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.api.KeyBoardButton;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.model.UsersMenu;
import pro.sky.TelegramBotTeam.service.UsersService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final KeyBoardButton keyBoardButton;
    private final UsersService usersService;
    private final TelegramBot telegramBot;

    String btnCommand;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KeyBoardButton keyBoardButton,
                                      UsersService usersService) {
        this.telegramBot = telegramBot;
        this.keyBoardButton = keyBoardButton;
        this.usersService = usersService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Функция возвращает пользователя, который прислал сообщение/нажал на кнопку.
     *
     * @param update обновления в чате телеграмм-бота.
     * @return пользователь, инициирующий обновление. Может быть null.
     * @throws NullPointerException параметр <code>update</code> равен null.
     */
    @Nullable
    private User getUpdates(Update update) {
        if (update == null) {
            LOGGER.error("Update structure is null");
            throw new NullPointerException("Update structure is null");
        }

        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            btnCommand = callbackQuery.data();
            LOGGER.info("- Processing telegramBot() - " + btnCommand);
            return callbackQuery.from();
        }

        Message message = update.message();
        if (message != null) {
            btnCommand = message.text();
            LOGGER.info("- Processing telegramBot() - " + btnCommand);
            return message.from();
        }

        return null;
    }

    /**
     * Функция обрабатывает сообщение от пользователя.
     *
     * @param user пользователь.
     * @throws NullPointerException параметр <code>user</code> равен null.
     */
    private void makeProcess(User user) {
        if (user == null) {
            LOGGER.error("User is null");
            throw new NullPointerException("User is null");
        }

        String btnStatus = keyBoardButton.getState(btnCommand);
        String btnMessage = keyBoardButton.getMessage(btnCommand);
        String message = (btnMessage != null) ? btnMessage : btnCommand;

        Long userId = user.id();
        String userName = user.firstName();


        // Запись в БД
        //так
        UsersMenu usersMenuBase = usersService.createUsers(new UsersMenu(userId, userName, btnStatus, "role"));
//или так
        System.out.println(userId);
        System.out.println(userName);
        System.out.println(btnStatus);
        System.out.println("btnCommand " + btnCommand);
        Users users = new Users(userId, userName, btnStatus, 1);
        System.out.println(users);
        usersService.createUsersAll(users);




        if (message.equals("/start")) {
            telegramBot.execute(new SendMessage(userId,userName + ", привет!")
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .parseMode(ParseMode.HTML));
        } else {
            telegramBot.execute(new SendMessage(userId, message)
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                    .parseMode(ParseMode.HTML)
            );
        }
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