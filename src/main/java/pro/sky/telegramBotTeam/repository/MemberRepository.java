package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findFirstByUser_Id(Long idUser);

    Optional<Member> findFirstByUser_IdAndShelter_Id(Long idUser, Long idShelter);

    Optional<Member> findFirstByUser_IdAndShelter_IdAndRole(Long idUser, Long idShelter, Member.MemberRole role);

    @Modifying
    @Query(value = "UPDATE members SET role = :role WHERE id_user = :idUser", nativeQuery = true)
    int updateMemberRole(Long idUser, String role);
}
