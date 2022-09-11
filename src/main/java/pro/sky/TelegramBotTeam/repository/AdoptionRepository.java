package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.TelegramBotTeam.model.Adoptiveparent;

import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoptiveparent, Long>  {
    @Query(value = "SELECT * FROM adoptions WHERE id_parent = :idParent", nativeQuery = true)
    Optional<Adoptiveparent> findByIdParent(Long idParent);
}
