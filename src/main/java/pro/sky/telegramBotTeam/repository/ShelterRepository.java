package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Shelter;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    @Query(value = "SELECT * FROM shelters WHERE name = :name", nativeQuery = true)
    Optional<Shelter> findByName(String name);
}
