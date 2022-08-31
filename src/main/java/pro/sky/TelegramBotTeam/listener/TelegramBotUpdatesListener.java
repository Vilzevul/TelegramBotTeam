package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.api.KeyBoardButton;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.model.UsersMenu;
import pro.sky.TelegramBotTeam.service.UsersService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final KeyBoardButton keyBoardButton;
    private final UsersService usersService;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    String text, btnCommand;
    Long userId;
    User userUpdate;
    Message message;
    CallbackQuery callbackQuery;
    String btnStatus = null;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(KeyBoardButton keyBoardButton, UsersService usersService) {
        this.keyBoardButton = keyBoardButton;
        this.usersService = usersService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * после команды или нажатия на кнопки перехватываем юзера и его возвращаем
     *
     * @param update
     * @return
     */
    User getUpdates(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            text = btnCommand = callbackQuery.data();
            userUpdate = callbackQuery.from();
            logger.info("- Processing telegramBot() - " + btnCommand);

        }
        Message message = update.message();
        if (message != null) {
            userUpdate = message.from();
            btnCommand = text = message.text();
            logger.info("- Processing telegramBot() - " + text);

        }
        return userUpdate;
    }

    /**
     * Запускаем после  getUpdates
     *
     * @param user - юзер -
     * @param user - юзер - возвращает getUpdates
     *             btnStatus - статус команды  idmenu
     *             createUsers - создает в БД пользователя или если существует - меняет последнюю команду
     *             getText - возвращает текст для сообщения в зависимости от выбранной команды
     * @return
     */
    Users makeProcess(User user) {
        Long userId = user.id();
        String userName = user.firstName();
        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
        String str = keyBoardButton.getText(btnCommand, btnStatus);
        if (str != null) text = str;
        else text = btnCommand;
/**
 * сюда вставляем функционал кнопок
 */
        // Запись в БД
        //так
        UsersMenu usersMenuBase = usersService.createUsers(new UsersMenu(userId, userName, btnStatus, "role"));
//или так
        System.out.println(userId);
        System.out.println(userName);
        Users users = new Users(userId, userName, btnStatus, 1);
        System.out.println(users);
        System.out.println(btnStatus);
        System.out.println("btnCommand " + btnCommand);
        usersService.createUsersAll(users);

        //if((str==null) && (users.getIdmenu().equals(keyBoardButton.STATE_SERVICE))) {
        //    if(!volunteersService.getVolunteers().isEmpty())
        //        userId = volunteersService.getVolunteers().get(0).getUserId();
        // }


        // Приветствие бота по имени пользователя.
        if (text.equals("/start")) {
            System.out.println("Попали в старт");
            SendResponse response = telegramBot.execute(new SendMessage(userId,
                    userName + ", Привет!")
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup()));
        } else {
            SendResponse response = telegramBot.execute(new SendMessage(userId, text)
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                    .parseMode(ParseMode.HTML)
            );
        }
        return null;
    }


    @Override
    public int process(List<Update> updates) throws RuntimeException {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
                userUpdate = getUpdates(update);
                //               userId = userUpdate.id();
                //          text = keyBoardButton.getText(btnCommand);
            } catch (RuntimeException e) {
                return;
            }

            try {
                makeProcess(userUpdate);
            } catch (RuntimeException e) {
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }//public int process(


}//class