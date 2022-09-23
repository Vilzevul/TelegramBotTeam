package pro.sky.telegramBotTeam.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //    @Query(value = "SELECT * FROM members WHERE id_user = :idUser AND id_shelter = :idShelter LIMIT 1", nativeQuery = true)
    Optional<Member> findFirstByUser_IdAndShelter_Id(Long idUser, Long idShelter);

    @Modifying
    @Query(value = "UPDATE members SET role = :role WHERE id_user = :idUser", nativeQuery = true)
    int updateMemberRole(Long idUser, String role);

    @Query(value = "SELECT * FROM members WHERE id_user = :idUser AND id_shelter = :idShelter AND role = :role LIMIT 1", nativeQuery = true)
    Optional<Member> findFirstUser(Long idUser, Long idShelter, String role);

    Optional<Member> findFirstByUser_Id(Long idUser);

}
