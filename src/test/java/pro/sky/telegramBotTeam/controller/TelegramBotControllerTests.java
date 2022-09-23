package pro.sky.telegramBotTeam.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.model.Report;
import pro.sky.telegramBotTeam.model.repository.*;
//import pro.sky.telegramBotTeam.repository.*;
import pro.sky.telegramBotTeam.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TelegramBotController.class)
class TelegramBotControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ShelterRepository shelterRepository;
    @MockBean
    UsersRepository usersRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    ReportRepository reportRepository;
    @MockBean
    AdoptionRepository adoptionRepository;

    @SpyBean
    private ShelterService shelterService;
    @SpyBean
    private UsersService usersService;
    @SpyBean
    private MemberService memberService;
    @SpyBean
    private ReportService reportService;
    @SpyBean
    private AdoptionService adoptionService;

    @InjectMocks
    private TelegramBotController telegramBotController;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(telegramBotController).isNotNull();
    }

    @Test
    public void shouldReturnCountEntriesWhenUpdateRole() throws Exception {
        when(memberRepository.updateMemberRole(any(Long.class), any(String.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/member/update-role")
                        .param("idUser", "0")
                        .param("memberRole", Member.MemberRole.USER.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdateRole() throws Exception {
        when(memberRepository.updateMemberRole(any(Long.class), any(String.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/member/update-role")
                        .param("idUser", "0")
                        .param("memberRole", Member.MemberRole.USER.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAdoptionStatusTest() throws Exception {
        doNothing().when(adoptionService).updateAdoptionStatus_(any(Long.class), any(Adoption.AdoptionStatus.class), any(int.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/adoption/update-status/{id}/{status}/{idShelter}", 1313205863, Adoption.AdoptionStatus.SUCCESS.toString(), "1"))
                .andExpect(status().isOk());

    }

    @Test
    void reportByIdAndDate() throws Exception {
        Long id = 1L;
        LocalDate date = LocalDate.of(2022, 9, 19);

        Report report = new Report(null, date, null, "hello");

        when(reportRepository.findFirstByAdoption_IdAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.of(report));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/reportByIdAndDate/{idAdoption}/{date}", id, date))
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedReport() throws Exception {
        Long id = 1L;
        LocalDate date = LocalDate.of(2022, 9, 19);
        Adoption adoption = new Adoption();
        Report report = new Report(adoption, date, null, "hello");

        when(reportRepository.findCompletedByIdAdoptionAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.of(report));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/completedReport/{idAdoption}/{date}", id, date))
                .andExpect(status().isOk());
    }

    @Test
    void getAllReport() throws Exception {
        List<Report> list = List.of();

        when(reportService.getAllReports()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/getAllReports")
                )
                .andExpect(status().isOk());
    }
}