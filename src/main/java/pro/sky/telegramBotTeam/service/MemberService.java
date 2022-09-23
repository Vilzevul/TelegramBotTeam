package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Member;
import pro.sky.telegramBotTeam.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с участниками усыновления.
 */
@Service
public class MemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Возвращает участника.
     *
     * @param idUser ID пользователя.
     * @return участник приюта. Может вернуть null, если такой отсутствует.
     */
    public Member getMember(Long idUser) {
        return memberRepository.findFirstByUser_Id(idUser).orElse(null);
    }

    /**
     * Возвращает участника указанного приюта.
     *
     * @param idUser ID пользователя.
     * @param idShelter ID приюта.
     * @return участник приюта. Может вернуть null, если такой отсутствует.
     */
    public Member getMemberOfShelter(Long idUser, Long idShelter) {
        return memberRepository.findFirstByUser_IdAndShelter_Id(idUser, idShelter).orElse(null);
    }

    /**
     * Возвращает участника указанного приюта указанной роли.
     *
     * @param idUser ID пользователя.
     * @param idShelter ID приюта.
     * @param role роль участника.
     * @return участник приюта. Может вернуть null, если такой отсутствует.
     */
    public Member getMemberOfShelterWithRole(Long idUser, Long idShelter, Member.MemberRole role) {
        return memberRepository.findFirstUser(idUser, idShelter, role.toString()).orElse(null);
    }

    /**
     * Получить список всех участников указанной роли.
     *
     * @param role роль участника.
     * @return список участников.
     */
    public List<Member> getMembersWithRole(Member.MemberRole role) {
        return memberRepository.findAll().stream().
                filter(v -> v.getRole() == role).
                collect(Collectors.toList());
    }

    /**
     * Получить список участников приюта указанной роли.
     *
     * @param idShelter ID приюта.
     * @param role роль участника.
     * @return список участников.
     */
    public List<Member> getMembersOfShelterWithRole(Long idShelter, Member.MemberRole role) {
        return memberRepository.findAll().stream().
                filter(m -> m.getRole() == role && m.getShelter().getId().equals(idShelter)).
                        collect(Collectors.toList());
    }

    /**
     * Сохранить/обновить данные участника приюта.
     *
     * @param member участник приюта.
     */
    public Member createMember(Member member) {
        Member memberBD = getMemberOfShelter(member.getUser().getId(), member.getShelter().getId());
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
     * @param role новая роль участника.
     * @return количество обновленных записей.
     */
    @Transactional
    public int updateMemberRole(Long idUser, Member.MemberRole role) {
        int count = memberRepository.updateMemberRole(idUser, role.toString());
        LOGGER.debug("Количество участников с обновленной ролью: {}", count);
        return count;
    }
}
