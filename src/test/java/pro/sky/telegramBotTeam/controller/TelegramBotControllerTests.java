package pro.sky.telegramBotTeam.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegramBotTeam.model.*;
import pro.sky.telegramBotTeam.repository.*;
import pro.sky.telegramBotTeam.service.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
    void contextLoads() {
        Assertions.assertThat(telegramBotController).isNotNull();
    }

    @Test
    void shouldReturnOkWhenGetMembersByRole() throws Exception {
        when(memberService.getMembersOfShelterWithRole(any(Long.class), any(Member.MemberRole.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/member/get-by-role")
                        .param("memberRole", Member.MemberRole.USER.toString())
                        .param("idShelter", "1")
                )
                .andExpect(status().isOk());
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
    public void shouldReturnOkWhenUpdateAdoptionStatus() throws Exception {
        doNothing().when(adoptionService).updateAdoptionStatus(any(Long.class), any(Long.class), any(Adoption.AdoptionStatus.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/adoption/update-status/{idUser}/{idShelter}/{status}",
                                "1", "1", Adoption.AdoptionStatus.SUCCESS.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOkWhenAddUserInAdoption() throws Exception {
        when(memberRepository.findFirstByUser_IdAndShelter_IdAndRole(any(Long.class), any(Long.class), any(Member.MemberRole.class))).thenReturn(Optional.of(new Member()));
        when(adoptionRepository.findFirstByParent_IdAndStatus(any(Long.class), any(Adoption.AdoptionStatus.class))).thenReturn(Optional.empty());
        when(adoptionService.createAdoption(any(Adoption.class))).thenReturn(new Adoption());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/adoption/add-parent")
                        .param("idUser", "1")
                        .param("idShelter", "2"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundWhenAddUserInAdoption() throws Exception {
        when(memberRepository.findFirstByUser_IdAndShelter_IdAndRole(any(Long.class), any(Long.class), any(Member.MemberRole.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/adoption/add-parent")
                        .param("idUser", "1")
                        .param("idShelter", "2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAllReports() throws Exception {
        when(reportService.getAllReports()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/get-all")
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkWhenGetReport() throws Exception {
        final Long idAdoption = 1L;
        final LocalDate date = LocalDate.of(2022, 9, 19);

        when(reportRepository.findFirstByAdoption_IdAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.of(new Report()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/get-by-date/{idAdoption}/{date}", idAdoption, date))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetReport() throws Exception {
        final Long idAdoption = 1L;
        final LocalDate date = LocalDate.of(2022, 9, 19);

        when(reportRepository.findFirstByAdoption_IdAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/get-by-date/{idAdoption}/{date}", idAdoption, date))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetCompletedReport() throws Exception {
        Long idAdoption = 1L;
        LocalDate date = LocalDate.of(2022, 9, 19);

        when(reportRepository.findFirstCompetedByAdoption_IdAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.of(new Report()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/get-by-date-completed/{idAdoption}/{date}", idAdoption, date))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetCompletedReport() throws Exception {
        final Long idAdoption = 1L;
        final LocalDate date = LocalDate.of(2022, 9, 19);

        when(reportRepository.findFirstCompetedByAdoption_IdAndReportDate(any(Long.class), any(LocalDate.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/get-by-date-completed/{idAdoption}/{date}", idAdoption, date))
                .andExpect(status().isNotFound());
    }
}