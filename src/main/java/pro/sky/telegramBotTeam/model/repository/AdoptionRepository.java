package pro.sky.telegramBotTeam.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Adoption;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    Optional<Adoption> findFirstByParent_Id(Long idParent);

    @Query(value = "SELECT * FROM adoptions WHERE id_parent= :idParent AND status = :status LIMIT 1", nativeQuery = true)
    Optional<Adoption> findFirstByParent_IdAndStatus(Long idParent, String status);

    @Modifying
    @Query(value = "UPDATE adoptions SET status = :status WHERE id = :id", nativeQuery = true)
    void updateAdoptionStatus(Long id, String status);

    @Modifying
    @Query(value = "UPDATE adoptions SET status = :status " +
            "WHERE id = (SELECT a.id " +
            "FROM adoptions a " +
            "inner join members m on m.id=a.id_parent " +
            "where m.id_user=:id and a.status <>'DECIDE'" +
            " and m.id_shelter=:idShelter)", nativeQuery = true)
    void updateAdoptionStatus_(Long id, String status, int idShelter);

}
