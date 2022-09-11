package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.UsersRepository;
import pro.sky.TelegramBotTeam.service.UsersService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private TelegramBotUpdatesListener telegramBotUpdatesListener;//получения экземпляра класса, который хотим протестировать

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private TestRestTemplate restTemplate;//тестовый шаблон запросов

    @Test
    void telegramBotUpdatesListenerLoads() throws Exception {//проверяем проинициализировался ли наш класс
        Assertions.assertThat(telegramBotUpdatesListener).isNotNull();
    }

    @Test
    User getUpdates() {

        return null;
    }

    @Test
    void makeProcess() {
        Users users = new Users(1L, "L", null, Users.UserRole.USER);
        Assertions.assertThat(users).isNotNull();
        when(usersRepository.save(any(Users.class))).thenReturn(users);
    }


    @Test
    void process() {

    }

}