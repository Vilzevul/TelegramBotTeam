package pro.sky.telegramBotTeam.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.repository.*;
import pro.sky.telegramBotTeam.service.*;

import static org.mockito.ArgumentMatchers.any;
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

//    @Test
//    void getUsersWithReport() throws Exception {
//        Assertions
//                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reports/users_report", String.class))
//                .isNotNull();
//    }
}