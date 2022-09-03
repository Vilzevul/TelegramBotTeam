package pro.sky.TelegramBotTeam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.TelegramBotTeam.repository.ReportRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KeepingpetsControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReportController keepingpetsController;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final ObjectMapper om = new ObjectMapper();
    //ObjectMapper - это класс в пакете jackson-databind,
    //который предоставляет функцию чтения и записи JSON, может легко преобразовывать объекты и JSON.

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(keepingpetsController).isNotNull();
    }

    @Test
    void getUsersWithReport() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/users_report", String.class))
                .isNotNull();
    }
}