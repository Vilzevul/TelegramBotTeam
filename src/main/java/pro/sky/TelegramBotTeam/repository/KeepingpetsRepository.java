package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.TelegramBotTeam.model.Keepingpets;

@Repository
public interface KeepingpetsRepository  extends JpaRepository<Keepingpets, Long> {

}
