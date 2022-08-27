package pro.sky.TelegramBotTeam.service;

import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.UsersRepository;

import javax.transaction.Transactional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
/*
    /**
     * Сохранение сообщений от пользователя в бд, для последующей обработки этих сообщений работниками приюта.
     *
     * @param userId уникальный идентификатор соответствует полю в таблице "users", генерируется автоматически
     * @param message используется для получения сообщения от пользователя и сохранения в БД, для последующей обработки волонтерами приюта
     * @param nameuser сохранение имени пользователя для последующего обращения к нему по имени
     * @param idchat сохранение уникального индентификатора чата из которого пришло сообщение
     * @param phone сохранение контактного номера телефона пользователя от которого пришло сообщение
     * @param idmenu сохранение кода меню - что искал пользователь
     * @param role сохранение кода роли: 1-пользователь; 2-усыновитель; 3-волонтер
     */
  /*  public void addMessage(Long userId,
                           String nameuser,
                           String message,
                           Long idchat,
                           int phone,
                           int idmenu,
                           int role
    ) {
        Users users = new Users();
        users.setUserId(userId);
       // users.setUserMessage(message);
        users.setNameuser(nameuser);
        users.setIdchat(idchat);
        users.setPhone(phone);
        users.setIdmenu(idmenu);
        users.setRole(role);
        usersRepository.save(users);
    }*/

    /**
     * Сохранение пользователей, которые интересуются приютом питомцев.
     */
    @Transactional
    public Users createUsers(Users users) {
        System.out.println("Попали в сохранение");
        return usersRepository.save(users);
    }
 /*

    /**
     * Находит всех пользователей от которых поступило сообщение
     * @return выводит всех пользователей от которых пришли сообщения
     */
/*    public List<Users> findUsersWithMessage() {
        return usersRepository.findUsersByMessage();
    }*/

    /**
     * Удаляет удаляет пользователей по Id
     * @param userId уникальный индентификатор пользователя по которому производится удаление
     */
    public void deleteUsersById(Long userId) {
        usersRepository.deleteById(userId);
    }
}
