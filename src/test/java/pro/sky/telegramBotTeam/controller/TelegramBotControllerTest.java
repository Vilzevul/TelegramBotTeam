package pro.sky.telegramBotTeam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegramBotTeam.service.ReportService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotControllerTest {
    @LocalServerPort
    private int port;
    @InjectMocks
    private TelegramBotController telegramBotController;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportRepository;
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

        Long id = 1L;
        LocalDate date = LocalDate.parse("2022-09-19");

        JSONObject userObject = new JSONObject();
        userObject.put(String.valueOf(id), date);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reports/reportByIdAndDate"+id+date)
                )
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedReport() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/completedReport", String.class))
                .isNotNull();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reports/completedReport")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getAllReport() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/getAllReports", String.class))
                .isNotNull();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reports/getAllReports")
                )
                .andExpect(status().isOk());
    }

}