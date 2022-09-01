package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.TelegramBotTeam.api.KeyBoardButton;
import pro.sky.TelegramBotTeam.service.UsersService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * webEnvironment - запускает сервер Tomcat на случайном порту,
 * а также создает бин TestRestTemplate, который мы можем просто внедрить в тестовый класс: @Autowired.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotUpdatesListenerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private TelegramBot telegramBot;
    @MockBean
    private UsersService usersService;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;//получения экземпляра класса, который вы хотите протестировать

    @Autowired
    private TestRestTemplate restTemplate;//тестовый шаблон запросов

    @Test
    void telegramBotUpdatesListenerLoads() throws Exception {//проверяем проинициализировался ли наш класс
        Assertions.assertThat(telegramBotUpdatesListener).isNotNull();
    }

    @Test
    private User getUpdates(Update update) {

        return null;
    }

    @Test
    private void makeProcess(User user) {

    }

    @Test
    void process() {

    }

}