package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Возвращает участника заданного приюта по указанному ID пользователя.
     *
     * @param idUser ID пользователя.
     * @param idShelter ID приюта.
     * @return участник приюта. Может вернуть null, если такой отсутствует.
     */
    public Member getMember(Long idUser, Long idShelter) {
        return memberRepository.findFirstByUser_IdAndShelter_Id(idUser, idShelter).orElse(null);
    }

    /**
     * Получить список участников приюта указанной роли.
     *
     * @param memberRole роль участника.
     * @return список участников.
     */
    public List<Member> getMembersByRole(Member.MemberRole memberRole) {
        return memberRepository.findAll().stream().
                filter(v -> v.getRole() == memberRole).
                        collect(Collectors.toList());
    }

    /**
     * Сохранить/обновить данные участника приюта.
     *
     * @param member участник приюта.
     */
    public Member createMember(Member member) {
        Member memberBD = getMember(member.getUser().getId(), member.getShelter().getId());
        if (memberBD == null) {
            LOGGER.info("Добавлен новый участник");
            return memberRepository.save(member);
        } else {
            LOGGER.info("Участник приюта обновлен");
            if (member.getRole() != null) {
                memberBD.setRole(member.getRole());
            }
            return memberRepository.save(memberBD);
        }
    }

 /*   public Member editMember(idChat bigint) {
        logger.info("Изменение роли пользователя");
        return memberRepository.save(idChat);
    }*/
}
