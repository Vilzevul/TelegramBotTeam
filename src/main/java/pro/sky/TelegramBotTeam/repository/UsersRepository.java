package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.TelegramBotTeam.model.Users;

import java.util.Collection;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

/**
     * Отобрать всех пользователей от которых поступило сообщения
     * @return
     */
//Collection<Users> findByNameUsers(Long Id);
}