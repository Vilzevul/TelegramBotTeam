package pro.sky.telegramBotTeam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.telegramBotTeam.repository.ReportRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TelegramBotController telegramBotController;

    @MockBean
    private ReportRepository reportRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(telegramBotController).isNotNull();
    }
    @Test
    void reportByIdAndDate() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/reportByIdAndDate", String.class))
                .isNotNull();
    }
    @Test
    void getCompletedReport() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/completedReport", String.class))
                .isNotNull();
    }
    @Test
    void getAllReport() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/getAllReports", String.class))
                .isNotNull();
    }
}