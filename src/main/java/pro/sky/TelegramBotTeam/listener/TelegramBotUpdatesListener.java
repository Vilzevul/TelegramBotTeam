package pro.sky.TelegramBotTeam.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.api.KeyBoardButton;
import pro.sky.TelegramBotTeam.model.Report;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.service.UsersService;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final KeyBoardButton keyBoardButton;
    private final UsersService usersService;
    private final TelegramBot telegramBot;


    String btnCommand = "undefined";
    String userContacts=null;
    Document document=null;
    String btnStatus = "undefined";

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KeyBoardButton keyBoardButton,
                                      UsersService usersService) {
        this.telegramBot = telegramBot;
        this.keyBoardButton = keyBoardButton;
        this.usersService = usersService;

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Функция возвращает пользователя, который прислал сообщение/нажал на кнопку.
     *
     * @param update обновления в чате телеграмм-бота.
     * @return пользователь, инициирующий обновление. Может быть null.
     * @throws NullPointerException параметр <code>update</code> равен null.
     */
    @Nullable
    private User getUpdates(Update update) {

         userContacts=null;
         document=null;

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
                document = message.document();
                return message.from();
            }
            if (message.text() != null) {
                btnCommand = message.text();
                return message.from();
            }
        }
        else {
            btnCommand =  "undefined" ;

        }
        LOGGER.info("getUpdates: {}", update);
        return null;
    }

    /**
     * Функция обрабатывает сообщение от пользователя.
     *
     * @param user пользователь.
     * @throws NullPointerException параметр <code>user</code> равен null.
     */
    private void makeProcess(User user) {
        if (user == null) {
            LOGGER.error("User is null");
            throw new NullPointerException("User is null");
        }

        btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
        String btnMessage = keyBoardButton.getMessage(btnCommand);
        String message = (btnMessage != null) ? btnMessage : btnCommand;

        Long userId = user.id();
        String userName = user.firstName();
        System.out.println("btnCommand" + btnCommand);


        if (btnCommand.equals(KeyBoardButton.CONTACTS)) {
            LOGGER.info("Пользователь прислал контакты: {}", userContacts);
        }
        LOGGER.info("begin makeProcess - Команда: {}  Статус {} текст {}", btnCommand, btnStatus, message);

        // Запись в БД
        Users users = new Users(userId, userName, btnStatus, 1);
        usersService.createUsersAll(users);

        if (btnCommand.equals("DOGSEND")) {
            LOGGER.info("Пользователь прислал отчет");
            //          Report report = new Report();
            //         usersService.createUsersWithReportAll(report);
        }

// Блок отправки отчета
        //пользователь отправляет фото
        if (btnCommand.equals(keyBoardButton.DOGSEND_MSG)) {
            try {
                if (document != null) { //если файл отправлен
                    byte[] reportContent = getFile(document);

                    // content сохраняем в БД
                    btnCommand = KeyBoardButton.DOGSEND_TXT; // перейти к отправке текста

                    if (reportContent != null)   //если отправлена картинка
                        message = "❗️Файл принят\n";
                    else message = "❌  Это не фото отчета \n";

                    message = message + keyBoardButton.getMessage(btnCommand);
                    btnStatus = keyBoardButton.getState(btnCommand, btnStatus);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//пользователь отправляет текст
        if (((btnStatus.equals(keyBoardButton.DOGSEND_TXT)))
                && (!btnCommand.equals(keyBoardButton.DOGSEND_TXT))) {
            String reportText = message;
            // reportText сохраняем в БД
            btnCommand = KeyBoardButton.DOGMAIN;
            message = "❗️Отчет принят\n";
        }
//конец блока отправки отчета

        if (message.equals("/start")) {
            telegramBot.execute(new SendMessage(userId, userName + ", привет!")
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .parseMode(ParseMode.HTML));
        } else {
            telegramBot.execute(new SendMessage(userId, message)
                    .replyMarkup(keyBoardButton.getMainKeyboardMarkup())
                    .replyMarkup(keyBoardButton.getInlineKeyboard(btnCommand))
                    .parseMode(ParseMode.HTML)
            );
        }
        LOGGER.info("end makeProcess - Команда: {}  Статус {} текст {}", btnCommand, btnStatus, message);
        LOGGER.info("end makeProcess - document: {} ", document);

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
     * Принимает от пользователя фото отчета, обрабатывает и возвращает byte[] для
     * последующей записи в БД в Report
     *
     * @param document
     * @return
     * @throws IOException
     */

    private byte[] getFile(Document document) throws IOException {
/**
 * com.pengrad.telegrambot
 * оюработка входящего сообщения и получение фото в виде байт массива
 */
        GetFile request = new GetFile(document.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(request);
        File file = getFileResponse.file(); // com.pengrad.telegrambot.model.File
        file.fileId();
        byte[] fileContent = telegramBot.getFileContent(file);
        /**
         *поток вывода - byteArrayOutputStream -, использующий массив байтов в качестве места вывода
         * и
         * входной поток,- byteArrayInputStream - использующий в качестве источника данных массив байтов
         * (присланный файл)
         */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent);
/**
 *BufferedImage - класс который представляет изображение, которое хранится в памяти
 */
        BufferedImage imgIn = ImageIO.read(byteArrayInputStream);
        if (imgIn == null) return null;
        /**
         * преобразуем изображение в  формат меньшего размера
         */
        double height = imgIn.getHeight() / (imgIn.getWidth() / 100d);
        BufferedImage imgOut = new BufferedImage(100, (int) height, imgIn.getType());
        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage((Image) imgIn, 0, 0, 100, (int) height, null);
        graphics.dispose();
/**
 * После  обработки изображение, его сохраняем обратно в поток вывода (byteArrayOutputStream)
 */
        ImageIO.write(imgOut, getExtension(document.fileName()), byteArrayOutputStream);
        /**
         * И проверить что файл сохраняется

        java.io.File dir = new java.io.File("c:/temp");
        dir.mkdir();
        java.io.File fileOut = new java.io.File("c:/temp", "test." + getExtension(document.fileName()));
        ImageIO.write(imgOut, getExtension(document.fileName()), fileOut);
         */

        /**
         * и преобразуем обратно в массив байтов
         */
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * получаем расширение файла - fileName
     * можно заменить этим
     * org.apache.commons.io.FilenameUtils
     * Maven: commons-io:commons-io:2.11.0 (commons-io-2.11.0.jar)
     *
     * @param fileName
     * @return
     */
    private String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}