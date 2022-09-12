package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Users;
import pro.sky.telegramBotTeam.repository.UsersRepository;

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
     * Сохранить нового пользователя, интересующегося приютом.
     * @param user новый пользователь.
     */

    @Transactional
    public Users createUsersAll(Users users) {
        Users baseUsers = usersRepository.findById(users.getId())
                .orElse(new Users(users.getId(), users.getName(), users.getPhone(),users.getRole()));
        return usersRepository.save(users);
    }

    public List<Users> getUsersByRole(Users.UserRole userRole) {
        return usersRepository.findAll().stream().
                filter(v -> v.getRole() == Users.UserRole.ADOPTION).
                collect(Collectors.toList());
    }


    public void addUser(Users user) {
        if (!usersRepository.existsById(user.getId())) {
            LOGGER.info("Добавлен новый пользователь: {}", user.getId());
            usersRepository.save(user);
        }
    }

    /**
     * Обновить телефон пользователя.
     * @param phone телефон пользователя.
     * @id id пользователя.
     */
    @Transactional
    public void updateUserPhone(String phone, Long id) {
        LOGGER.info("Пользователь {} указал контакты: {}", id, phone);
        if (usersRepository.existsById(id)) {
            usersRepository.setUserPhoneById(phone, id);
        }
    }
}
