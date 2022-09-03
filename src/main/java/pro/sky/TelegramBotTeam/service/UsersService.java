package pro.sky.TelegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.model.UsersMenu;
import pro.sky.TelegramBotTeam.repository.ReportRepository;
import pro.sky.TelegramBotTeam.repository.UsersMenuRepository;
import pro.sky.TelegramBotTeam.repository.UsersRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);
    private final UsersRepository usersRepository;
    private final UsersMenuRepository usersMenuRepository;
    private final ReportRepository reportRepository;

    public UsersService(UsersRepository usersRepository,
                        UsersMenuRepository usersMenuRepository, ReportRepository reportRepository) {
        this.usersRepository = usersRepository;
        this.usersMenuRepository = usersMenuRepository;
        this.reportRepository = reportRepository;
    }

    /**
     * Сохранение пользователей, которые интересуются приютом питомцев.
     */
    @Transactional
    public UsersMenu createUsers(UsersMenu usersMenu) {
        UsersMenu baseUsers = usersMenuRepository.findById(usersMenu.getId())
                .orElse(new UsersMenu(usersMenu.getId(), usersMenu.getNameuser(), usersMenu.getIdmenu(), usersMenu.getRole()));
        baseUsers.setIdmenu(usersMenu.getIdmenu());
        LOGGER.info("Попали в сохранение");
        return usersMenuRepository.save(usersMenu);
    }

    @Transactional
    public Users createUsersAll(Users users) {
        LOGGER.info("Попали в сохранение");
        return usersRepository.save(users);
    }

    /**
     * Сохраняем всех пользователей с отчетами
     * @param reports
     * @return
     */
    @Transactional
    public Report createUsersWithReportAll(Report reports) {
        LOGGER.info("Попали в сохранение");
        return reportRepository.save(reports);
    }
    /**
     * Находит всех пользователей от которых поступил отчет
     *
     * @return выводит всех пользователей от которых пришли отчеты
     */
    public List<Report> getUsersWitReport() {
        return reportRepository.findAll();
    }

    /**
     * Удаляет пользователей по Id
     *
     * @param userId уникальный индентификатор пользователя по которому производится удаление
     */
    public void deleteUsersById(Long userId) {
        usersRepository.deleteById(userId);
    }
}
