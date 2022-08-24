package pro.sky.TelegramBotTeam.service;

import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.UsersRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void addLastOperationOfUser(String message,
                                       Long userId,
                                       Long messageId) {
        Users users = new Users();
        users.setText(message);
        users.setUserId(userId);
        users.setMessageId(messageId);
        usersRepository.save(users);
    }

    public List<Users> findUsers() {
        return usersRepository.findAll();
    }

    public void deleteUsers(Users users) {
        usersRepository.delete(users);
    }
}
