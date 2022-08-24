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
    public void addLastOperationOfUser(Long userId,
                                       String nameuser,
                                       Long idchat,
                                       int phone,
                                       int idmenu) {
        Users users = new Users();
        users.setUserId(userId);
        users.setNameuser(nameuser);
        users.setIdchat(idchat);
        users.setPhone(phone);
        users.setIdmenu(idmenu);
        usersRepository.save(users);
    }

    public List<Users> findUsers() {
        return usersRepository.findAll();
    }

    public void deleteUsers(Users users) {
        usersRepository.delete(users);
    }
}
