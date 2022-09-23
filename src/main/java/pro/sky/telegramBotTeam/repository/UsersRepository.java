package pro.sky.telegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegramBotTeam.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}