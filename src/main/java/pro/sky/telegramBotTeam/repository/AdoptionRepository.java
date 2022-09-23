package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Adoption;

import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    Optional<Adoption> findFirstByParent_Id(Long idParent);

    Optional<Adoption> findFirstByParent_IdAndStatus(Long idParent, Adoption.AdoptionStatus status);

    @Modifying
    @Query(value = "UPDATE adoptions SET status = :status WHERE id = :id", nativeQuery = true)
    void updateAdoptionStatusById(Long id, String status);

    @Modifying
    @Query(value = "UPDATE adoptions SET status = :status " +
            "WHERE id = (SELECT a.id " +
            "FROM adoptions a INNER JOIN members m " +
            "ON m.id = a.id_parent " +
            "WHERE m.id_user = :idUser AND a.status <> 'DECIDE' AND m.id_shelter = :idShelter)", nativeQuery = true)
    void updateAdoptionStatusByIdUserAndIdShelter(Long idUser, Long idShelter, String status);
}
