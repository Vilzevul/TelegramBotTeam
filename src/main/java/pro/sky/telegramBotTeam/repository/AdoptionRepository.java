package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Adoption;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long>  {
    @Query(value = "SELECT * FROM adoptions WHERE id_parent = :idParent  LIMIT 1", nativeQuery = true)
    Optional<Adoption> findByIdParent(Long idParent);
}
