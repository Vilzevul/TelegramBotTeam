package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.repository.UsersRepository;
import pro.sky.telegramBotTeam.model.Users;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Возвращает пользователя по указанному ID.
     *
     * @param id ID (id chat) пользователя.
     * @return пользователь. Может вернуть null, если такой пользователь отсутствует.
     */
    public Users getUser(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    /**
     * Сохранить/обновить данные пользователя, интересующегося приютом.
     * @param user пользователь.
     */
    public Users createUser(Users user) {
        Users userBD = getUser(user.getId());
        if (userBD == null) {
            LOGGER.info("Добавлен новый пользователь");
            return usersRepository.save(user);
        } else {
            LOGGER.info("Пользователь обновлен");
            if (user.getPhone() != null) {
                userBD.setPhone(user.getPhone());
            }
            if (user.getRole() != null) {
                userBD.setRole(user.getRole());
            }
            return usersRepository.save(userBD);
        }
    }

    /**
     * Получить список пользователей указанной роли.
     * @param userRole роль пользователя.
     * @return список пользователей.
     */
    public List<Users> getUsersByRole(Users.UserRole userRole) {
        return usersRepository.findAll().stream().
                filter(v -> v.getRole() == Users.UserRole.ADOPTION).
                collect(Collectors.toList());
    }
}
