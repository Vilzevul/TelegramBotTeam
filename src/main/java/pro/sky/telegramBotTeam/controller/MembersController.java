package pro.sky.telegramBotTeam.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.service.MemberService;
import pro.sky.telegramBotTeam.service.UsersService;

@RestController
@RequestMapping("/members")
public class MembersController {
    private final UsersService usersService;

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    public MembersController(UsersService usersService) {
        this.usersService = usersService;
    }

  /*  @PutMapping
 /**   public Member editMember(@RequestBody idChat bigint) {
        return studentRepository.findById(idChat).orElseThrow(() -> new StudentNotFoundException("Студент не найден")).toString();
        return MemberService.editMember(idChat);
    }*/
}
