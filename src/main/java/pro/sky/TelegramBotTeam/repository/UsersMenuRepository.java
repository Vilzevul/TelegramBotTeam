package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.TelegramBotTeam.model.UsersMenu;

public interface UsersMenuRepository extends JpaRepository<UsersMenu,Long> {
}
