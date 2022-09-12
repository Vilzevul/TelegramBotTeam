package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.repository.UsersRepository;
import pro.sky.telegramBotTeam.model.Users;

import javax.transaction.Transactional;
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
     * Сохранить/обновить данные пользователя, интересующегося приютом.
     * @param user пользователь.
     */
    @Transactional
    public Users createUsers(Users user) {
        return usersRepository.save(user);
    }

    /**
     * Получить список пользователей с указанной ролью.
     * @param userRole роль пользователя.
     * @return список пользователей.
     */
    public List<Users> getUsersByRole(Users.UserRole userRole) {
        return usersRepository.findAll().stream().
                filter(v -> v.getRole() == Users.UserRole.ADOPTION).
                collect(Collectors.toList());
    }
}
