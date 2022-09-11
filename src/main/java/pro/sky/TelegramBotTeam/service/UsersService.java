package pro.sky.TelegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.ReportRepository;
import pro.sky.TelegramBotTeam.repository.UsersRepository;

import javax.transaction.Transactional;
import java.util.List;

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
