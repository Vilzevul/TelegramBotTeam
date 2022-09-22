package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.repository.MemberRepository;

import javax.transaction.Transactional;
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

    public Member getMemberByIdUser(Long idUser) {
        return memberRepository.findFirstByUser_Id(idUser).orElse(null);
    }

    public Member getMemberUser(Long idUser, Long idShelter, Member.MemberRole memberRole) {
        return memberRepository.findFirstUser(idUser, idShelter, memberRole.toString()).orElse(null);
    }

    /**
     * Получить список участников приюта указанной роли.
     *
     * @param idShelter ID приюта.
     * @param memberRole роль участника.
     * @return список участников.
     */
    public List<Member> getMembersByRole(Member.MemberRole memberRole, Long idShelter) {
        return memberRepository.findAll().stream().
                filter(m -> m.getRole() == memberRole && m.getShelter().getId().equals(idShelter)).
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

    /**
     * Обновить роль участника приюта.
     *
     * @param idUser ID пользователя.
     * @param memberRole новая роль участника.
     * @return количество обновленных записей.
     */
    @Transactional
    public int updateMemberRole(Long idUser, Member.MemberRole memberRole) {
        int count = memberRepository.updateMemberRole(idUser, memberRole.toString());
        LOGGER.info("Count of updated members: {}", count);
        return count;
    }
}
