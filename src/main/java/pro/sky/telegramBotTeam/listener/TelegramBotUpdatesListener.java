package pro.sky.telegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.api.KeyBoardButton;
import pro.sky.telegramBotTeam.model.*;
import pro.sky.telegramBotTeam.service.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static pro.sky.telegramBotTeam.api.Code.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final KeyBoardButton keyBoardButton;
    private final UsersService usersService;
    private final ShelterService shelterService;
    private final MemberService memberService;
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final TelegramBot telegramBot;

    String userContacts = null;
    String userFileId = null;
    String btnStatus = "undefined";
    String btnCommand = "undefined";

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KeyBoardButton keyBoardButton,
                                      UsersService usersService,
                                      ShelterService shelterService,
                                      MemberService memberService,
                                      AdoptionService adoptionService,
                                      ReportService reportService) {
        this.telegramBot = telegramBot;
        this.keyBoardButton = keyBoardButton;
        this.usersService = usersService;
        this.shelterService = shelterService;
        this.memberService = memberService;
        this.adoptionService = adoptionService;
        this.reportService = reportService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Возвращает пользователя, который прислал сообщение/нажал на кнопку.
     *
     * @param update обновления в чате телеграмм-бота.
     * @return пользователь, инициирующий обновление. Может быть null.
     * @throws NullPointerException параметр <code>update</code> равен null.
     */
    @Nullable
    private User getUpdates(Update update) {
        userContacts = null;
        userFileId = null;

        if (update == null) {
            LOGGER.error("Update structure is null");
            throw new NullPointerException("Update structure is null");
        }

        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            btnCommand = (callbackQuery.data() == null) ? "undefined" : callbackQuery.data();
            LOGGER.info("- getUpdates(callbackQuery) - " + btnCommand);
            return callbackQuery.from();
        }

        Message message = update.message();
        if (message != null) {
            if (message.contact() != null) {
                btnCommand = KeyBoardButton.CONTACTS;
                userContacts = message.contact().phoneNumber();
                return message.from();
            }

            if (message.document() != null) {
                btnCommand = btnCommand;
                userFileId = message.document().fileId();
                return message.from();
            }

            if (message.photo() != null) {
                btnCommand = btnCommand;
                userFileId = message.photo()[message.photo().length - 1].fileId();
                return message.from();
            }

            if (message.text() != null) {
                btnCommand = message.text();
                return message.from();
            }
        } else {
            btnCommand = "undefined";
        }

        LOGGER.info("getUpdates: {}", update);
        return null;
    }

    /**
     * Обрабатывает сообщение от пользователя.
     *
     * @param user пользователь.
     * @throws NullPointerException параметр <code>user</code> равен null.
     */
    private void makeProcess(User user) {
        if (user == null) {
            LOGGER.error("User is null");
            throw new NullPointerException("User is null");
        }

        //       btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
        String btnMessage = keyBoardButton.getMessage(btnCommand);
        String message = (btnMessage != null) ? btnMessage : btnCommand;

        LOGGER.info("begin makeProcess - команда: {} статус: {} текст: {}", btnCommand, btnStatus, message);

        Long userId = user.id();
        String userName = user.firstName();

        switch (btnCommand) {
            //Запись в БД
            case KeyBoardButton.DOGMAIN,
                    KeyBoardButton.CATMAIN -> {
                Shelter shelter = shelterService.getShelter(btnCommand.equals(KeyBoardButton.DOGMAIN) ? Shelter.ShelterType.DOGS : Shelter.ShelterType.CATS);
                Users users = new Users(userId, userName, userContacts);
                Member member = new Member(users, shelter);

                usersService.createUser(users);         //Добавить/обновить пользователя
                memberService.createMember(member);     //Добавить/обновить участника

                if (btnStatus.equals(KeyBoardButton.SEND_END) ||
                        btnStatus.equals(KeyBoardButton.DOGSEND_TXT) ||
                        btnStatus.equals(KeyBoardButton.CATSEND_TXT)) {
                    checkReport(member);
                    btnStatus = KeyBoardButton.DOGMAIN;
                    message = keyBoardButton.getMessage(btnCommand);
                }
                btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
            }

            case KeyBoardButton.MESSAGEFORDOGVOLONTER,
                    KeyBoardButton.MESSAGEFORCATVOLONTER -> {
                btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
            }

            //Блок отправки отчета
            //Пользователь отправляет фото
            case KeyBoardButton.DOGSEND_MSG,
                    KeyBoardButton.CATSEND_MSG -> {
                btnStatus = KeyBoardButton.SEND_END;
                try {
                    Shelter shelter = shelterService.getShelter(btnCommand.equals(KeyBoardButton.DOGSEND_MSG) ? Shelter.ShelterType.DOGS : Shelter.ShelterType.CATS);
                    Member member = memberService.getMember(userId, shelter.getId());
                    Adoption adoption = (member == null) ? null : adoptionService.getAdoption(member.getId());
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        if (userFileId != null) {
                            byte[] reportContent = getFileContent(telegramBot, userFileId);
                            //reportContent сохраняем в БД
                            if (reportContent != null) {
                                switch (btnCommand) {
                                    case KeyBoardButton.DOGSEND_MSG -> {
                                        Report report = new Report(adoption, LocalDate.now(), reportContent, null);
                                        report = reportService.createReport(report);
                                        btnCommand = KeyBoardButton.DOGSEND_TXT;
                                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                                        message = "❗️Файл принят\n";
                                        message += keyBoardButton.getMessage(btnCommand);
                                        LOGGER.info("report: {}", report);
                                    }

                                    case KeyBoardButton.CATSEND_MSG -> {
                                        Report report = new Report(adoption, LocalDate.now(), reportContent, null);
                                        report = reportService.createReport(report);
                                        btnCommand = KeyBoardButton.CATSEND_TXT;
                                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                                        message = "❗️Файл принят\n";
                                        message += keyBoardButton.getMessage(btnCommand);
                                        LOGGER.info("report: {}", report);
                                    }
                                }
                            }
                        }
                    } else {
                        btnCommand = KeyBoardButton.CATMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет\n";
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            case KeyBoardButton.DOGSEND_TXT,
                    KeyBoardButton.CATSEND_TXT,
                    KeyBoardButton.DOGTAKE,
                    KeyBoardButton.CATTAKE -> {
                btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
            }

            default -> {
                //Пользователь отправляет текст
                if (btnStatus.equals(KeyBoardButton.DOGSEND_TXT)) {
                    String reportText = message;
                    Shelter shelter = shelterService.getShelter(Shelter.ShelterType.DOGS);
                    Member member = memberService.getMember(userId, shelter.getId());
                    Adoption adoption = (member == null) ? null : adoptionService.getAdoption(member.getId());
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        Report report = new Report(adoption, LocalDate.now(), null, reportText);
                        report = reportService.createReport(report);
                        btnCommand = KeyBoardButton.DOGMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "❗️Отчет принят\n";
                        checkReport(member);
                        LOGGER.info("report: {}", report);
                    } else {
                        btnCommand = KeyBoardButton.DOGMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет";
                    }
                }

                if (btnStatus.equals(KeyBoardButton.CATSEND_TXT)) {
                    String reportText = message;
                    Shelter shelter = shelterService.getShelter(Shelter.ShelterType.CATS);
                    Member member = memberService.getMember(userId, shelter.getId());
                    Adoption adoption = (member == null) ? null : adoptionService.getAdoption(member.getId());
                    if ((adoption != null) && (adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE))) {
                        Report report = new Report(adoption, LocalDate.now(), null, reportText);
                        report = reportService.createReport(report);
                        btnCommand = KeyBoardButton.CATMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "❗️Отчет принят\n";
                        checkReport(member);
                        LOGGER.info("report: {}", report);
                    } else {
                        btnCommand = KeyBoardButton.CATMAIN;
                        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                        message = "Вам не нужно присылать отчет";
                    }
                }
            }
        }

        switch (btnCommand) {
            case "/getdog" -> {
                File file = new File("documents/getdog.doc");
                telegramBot.execute(new SendDocument(userId, file));
            }

            case KeyBoardButton.START -> {
                Users users = new Users(userId, userName, userContacts);
                usersService.createUser(users);
                telegramBot.execute(new SendMessage(userId, userName + ", привет!")
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                        .parseMode(ParseMode.HTML));
            }

            //main keyBoard
            case KeyBoardButton.CONTACTS,
                    KeyBoardButton.ESCAPECONTACTDOG,
                    KeyBoardButton.ESCAPECONTACTCAT -> {
                message = keyBoardButton.getMessage(KeyBoardButton.ESCAPE);
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup()));

                message = keyBoardButton.getMessage(btnCommand);
                if (btnCommand.equals(KeyBoardButton.CONTACTS)) {
                    message = "Продолжить работу с программой";
                }
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                        .parseMode(ParseMode.HTML));
            }

            case KeyBoardButton.INLINECONTACTSDOG -> {
                telegramBot.execute(new SendMessage(userId, KeyBoardButton.CONTACTS)
                        .replyMarkup(keyBoardButton.getContactKeyboardDog())
                );
            }

            case KeyBoardButton.INLINECONTACTSCAT -> {
                telegramBot.execute(new SendMessage(userId, KeyBoardButton.CONTACTS)
                        .replyMarkup(keyBoardButton.getContactKeyboardCat())
                );
            }

            default -> {
                telegramBot.execute(new SendMessage(userId, message)
                        .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                        .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                        .parseMode(ParseMode.HTML)
                );
            }
        }

        LOGGER.info("middle - команда: {}  текст: {}", btnCommand, message);
        LOGGER.info("middle  - статус: {} ", btnStatus);

        switch (btnStatus) {
            case KeyBoardButton.MESSAGEFORDOGVOLONTER -> {
                if (!btnCommand.equals(KeyBoardButton.MESSAGEFORDOGVOLONTER)) {
                    String messageService = (btnMessage != null) ? btnMessage : btnCommand;
                    Shelter shelter = shelterService.getShelter(Shelter.ShelterType.DOGS);
                    message = getMessageForVolunteer(userId, messageService, shelter);
                    btnStatus = btnCommand = KeyBoardButton.DOGMAIN;
                    message = (message == null) ? "Сообщение отправлено" : btnCommand;
                    telegramBot.execute(new SendMessage(userId, message)
                            .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                            .parseMode(ParseMode.HTML));
                }
            }

            case KeyBoardButton.MESSAGEFORCATVOLONTER -> {
                if (!btnCommand.equals(KeyBoardButton.MESSAGEFORCATVOLONTER)) {
                    String messageService = (btnMessage != null) ? btnMessage : btnCommand;
                    Shelter shelter = shelterService.getShelter(Shelter.ShelterType.CATS);
                    message = getMessageForVolunteer(userId, messageService, shelter);
                    btnStatus = btnCommand = KeyBoardButton.CATMAIN;
                    message = (message == null) ? "Сообщение отправлено" : btnCommand;
                    telegramBot.execute(new SendMessage(userId, message)
                            .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                            .parseMode(ParseMode.HTML));
                }
            }
        }

        LOGGER.info("end makeProcess - команда: {}  текст: {}", btnCommand, message);
        LOGGER.info("end makeProcess - статус: {} ", btnStatus);
    }

    /**
     * Находит случайного волонтера указанного приюта и передает ему сообщение пользователя.
     *
     * @param idChartUser id chat пользователя.
     * @param messageService сообщение пользователя.
     * @param shelter приют, к которому обращается пользователь.
     * @return null, если сообщение успешно передано волонтеру, в ином случае - сообщение-предупреждение для пользователя.
     */
    private String getMessageForVolunteer(Long idChartUser, String messageService, Shelter shelter) {
        String message = null;

        List<Member> volunteersList = memberService.getMembersByRole(Member.MemberRole.VOLUNTEER)
                .stream().filter(member -> Objects.equals(member.getShelter().getId(), shelter.getId())).toList();

        if (volunteersList.isEmpty()) {
            message = "Свободных волонтеров нет. Попробуйте связаться позже";
            telegramBot.execute(new SendMessage(idChartUser, message)
                    .parseMode(ParseMode.HTML));
        } else {
            Random random = new Random();
            Long idChartVolunteer = (volunteersList.size() == 1) ?
                    volunteersList.get(0).getUser().getId() :
                    volunteersList.get(random.nextInt(volunteersList.size())).getUser().getId();
            telegramBot.execute(new SendMessage(idChartVolunteer,
                    "Сообщение от пользователя [" + idChartUser + "]: " + messageService)
                    .parseMode(ParseMode.HTML));
            LOGGER.info("Сообщение: {}, волонтер: {}", messageService, idChartVolunteer);
        }

        return message;
    }

    @Override
    public int process(List<Update> updates) throws RuntimeException {
        updates.forEach(update -> {
            LOGGER.info("Processing update: {}", update);
            if (update != null) {
                User user = getUpdates(update);
                if (user != null) {
                    makeProcess(user);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Только для debug: переводит пользователей с ролью Adoption в усыновители.
     */
    private void addAdoptions() {
        List<Member> membersList = memberService.getMembersByRole(Member.MemberRole.ADOPTION);
        if (!membersList.isEmpty()) {
            Member memberAdaptive = membersList.get(0);
            memberAdaptive.setRole(Member.MemberRole.USER);
            memberAdaptive = memberService.createMember(memberAdaptive);

            Adoption adoption = new Adoption();
            LocalDate date = LocalDate.now();
            LocalDate date30 = date.plusDays(30);

            adoption.setParent(memberAdaptive);
            adoption.setVolunteer(memberAdaptive);
            adoption.setStartDate(java.sql.Date.valueOf(date));
            adoption.setEndDate(java.sql.Date.valueOf(date30));
            LOGGER.info("member: {}", memberAdaptive);
            LOGGER.info("adoption: {}", adoption);
            adoptionService.createAdoption(adoption);
        }
    }

    /**
     * Конвертировать Date в LocalDate.
     *
     * @param date дата типа Date.
     * @return дата типа LocalDate. Если date = null, возвращает null.
     */
    private LocalDate convertDateToLocalDate(Date date) {
        return (date == null) ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Проверка отчета на наличие всех полей.
     * Отправляет предупреждение пользователю, если какое-то из полей незаполнено.
     *
     * @param member участник приюта.
     */
    public void checkReport(Member member) {
        final String TEXT = "Вы не прислали текст отчета";
        final String IMAGE = "Вы не прислали фото отчета";
        String message = null;

        Adoption adoption = adoptionService.getAdoption(member.getId());
        if ((adoption == null) || !adoption.getStatus().equals(Adoption.AdoptionStatus.ACTIVE)) {
            return;
        }

        Report report = reportService.getReport(adoption.getId(), LocalDate.now());
        if (report == null) {
            return;
        }

        if (report.getReportImage() == null) {
            message = IMAGE;
        }
        if (report.getReportMessage() == null) {
            message = TEXT;
        }

        if (message != null) {
            telegramBot.execute(new SendMessage(member.getUser().getId(), message)
                    .parseMode(ParseMode.HTML));
        }
    }

    /**
     * Ежедневно в 21:00 проходится по всем усыновителям и рассылает
     * сообщения участникам в соответствии с текущим статусом усыновления.
     */
    @Scheduled(cron = "0 0 21 * * *")
    public void notifyMembersOfAdoption() {
        adoptionService.getAllAdoptions().forEach(adoption -> {
            final Long volunteerChartId = adoption.getVolunteer().getUser().getId();
            final Long parentChartId = adoption.getParent().getUser().getId();
            final String parentName = adoption.getParent().getUser().getName();

            switch (adoption.getStatus()) {
                case SUCCESS -> {
                    telegramBot.execute(new SendMessage(parentChartId,
                            "Поздравляем, " + parentName + "! Вы прошли испытательный период")
                            .parseMode(ParseMode.HTML));
                    adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.NOT_ACTIVE);
                    reportService.deleteReports(adoption.getId());
                }

                case FAILED -> {
                    telegramBot.execute(new SendMessage(parentChartId,
                            "Сожалеем, " + parentName + "! Вы не прошли испытательный период")
                            .parseMode(ParseMode.HTML));
                    adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.NOT_ACTIVE);
                    reportService.deleteReports(adoption.getId());
                }

                case DECIDE -> {
                    telegramBot.execute(new SendMessage(volunteerChartId,
                            "Испытательный период для усыновителя [" + parentChartId + "] закончился. " +
                                    "Требуется Ваше решение о дальнейшей судьбе питомца")
                            .parseMode(ParseMode.HTML));
                }

                case ACTIVE -> {
                    final LocalDate currentDate = LocalDate.now();
                    final LocalDate adoptionStartDate = convertDateToLocalDate(adoption.getStartDate());
                    final LocalDate adoptionEndDate = convertDateToLocalDate(adoption.getEndDate());

                    if (currentDate.isAfter(adoptionEndDate)) {
                        telegramBot.execute(new SendMessage(parentChartId,
                                "Ваш испытательный период закончился, ожидается решение волонтера")
                                .parseMode(ParseMode.HTML));
                        adoptionService.updateAdoptionStatus(adoption.getId(), Adoption.AdoptionStatus.DECIDE);
                    } else {
                        //Если пользователь в текущие сутки уже слал отчет (и его отчет заполнен) - не беспокоим его.
                        //Иначе шлем стандартную напоминалку
                        Report todayReport = reportService.getCompletedReport(adoption.getId(), currentDate);
                        if (todayReport == null) {
                            telegramBot.execute(new SendMessage(parentChartId,
                                    "Пожалуйста, не забудьте заполнить и прислать отчет о самочувствии питомца")
                                    .parseMode(ParseMode.HTML));

                            //Если заполненных отчетов нет уже более 2 дней - даем знать об этой ситуации волонтерам
                            LocalDate lastReportDate = convertDateToLocalDate(reportService.getLastCompletedReportDate(adoption.getId()));
                            final boolean notifyVolunteer = (lastReportDate == null) ?
                                    currentDate.minusDays(2).isAfter(adoptionStartDate) :
                                    (ChronoUnit.DAYS.between(lastReportDate, currentDate) >= 2);

                            if (notifyVolunteer) {
                                telegramBot.execute(new SendMessage(volunteerChartId,
                                        "Усыновитель [" + parentChartId + "] уже более двух дней не присылает отчет о питомце")
                                        .parseMode(ParseMode.HTML));
                            }
                        }
                    }
                }
            }
        });
    }
}