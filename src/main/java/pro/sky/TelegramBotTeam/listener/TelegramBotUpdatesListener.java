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
import pro.sky.TelegramBotTeam.KeyBoardButton;
import pro.sky.TelegramBotTeam.model.Users;
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
    User user;
    Message message;

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

    @Override
    public int process(List<Update> updates) throws RuntimeException {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // после нажатия на кнопки
            CallbackQuery callbackQuery = update.callbackQuery();
            if (callbackQuery != null) {
                text = btnCommand = callbackQuery.data();
                user = callbackQuery.from();
                userId = user.id();

                logger.info("- Processing telegramBot() - " + btnCommand);
            }

            //После ввода команды
            message = update.message();
            if (message != null) {
                user = message.from();
                userId = user.id();
                btnCommand = text = message.text();
                logger.info("- Processing telegramBot() - " + text);
            }
            text = keyBoardButton.getText(btnCommand);
            try {

                if (text.equals("/start")) {
                    SendResponse response = telegramBot.execute(new SendMessage(message.from().id(),
                            message.from().firstName() + ", hello!" + "\n" + keyBoardButton.getText("text"))
                            .parseMode(ParseMode.HTML)
                            .replyMarkup(keyBoardButton.getMainKeyboardMarkup()));

                    long chatId = update.message().from().id();
                    System.out.println(chatId);
                    String username = update.message().from().firstName();
                    System.out.println(username);
                    int phone = 0;
                    Users users = new Users(chatId, username, phone,1,1);
                    System.out.println(users);
                    usersService.createUsers(users);
                    return;
                }


                telegramBot.execute(new SendMessage(userId, text)
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                        //       .replyMarkup(keyboard)
                        .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                        //       .replyMarkup(replyKeyboardRemove)
                        .parseMode(ParseMode.HTML));

            } catch (RuntimeException e) {
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
