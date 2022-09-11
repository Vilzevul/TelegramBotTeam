package pro.sky.TelegramBotTeam.api;

import com.pengrad.telegrambot.model.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static pro.sky.TelegramBotTeam.api.Code.readFile;

@Component
public class KeyBoardButton {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyBoardButton.class);

    public final static String CONTACTS = "Оставить контакты";
    public final static String DOCUMENT = "DOCUMENT";
    public final static String ERROR = "ERROR";
    public final static String BACK = "Назад";

    public final static String DOGSEND = "Прислать отчет";
    public final static String DOGSEND_TXT = "Напишите текст отчета";
    public final static String DOGSEND_MSG = "Отправьте фото отчета";

    public final static String HELP = "User->Adoptions";
    public final String SERVICE = "Оставить сообщение";
    public final static String DOGMAIN = "Приют для собак";
    private final String DOGABOUT = "О приюте для собак";
    private final String DOGRULES = "Правила приюта для собак";
    private final String DOGTAKE = "Как взять собаку из приюта";
    private final String DOGZN = "Правила знакомства с собакой";
    private final String CATZN = "Правила знакомства с кошкой";
    private final String DOGDOCUMENTS = "Список документов, чтобы взять собаку";
    private final String CATDOCUMENTS = "Список документов, чтобы взять кошку";
    private final String DOGTRANSPORT = "Список рекомендаций по транспортировке собаки";
    private final String CATTRANSPORT = "Список рекомендаций по транспортировке животного";
    private final String DOGHOMEPUPPY = "Список рекомендаций по обустройству дома для щенка";
    private final String CATHOMEPUPPY = "Список рекомендаций по обустройству дома для котенка";
    private final String DOGHOMEDOG = "Список рекомендаций по обустройству дома для взр. собаки";
    private final String CATHOMEDOG = "Список рекомендаций по обустройству дома для взр. животного";
    private final String DOGHOMEDOGLIMITED = "Список реком. по обустройству дома для собаки с огран. возможностями";
    private final String CATHOMEDOGLIMITED = "Список реком. по обустройству дома для животного с огран. возможностями";
    private final String DOGADVICECYNOLOGIST = "Советы кинолога по первичному общению с собакой";
    private final String DOGRECOMMENDATION = "Рекомендации кинолога для дальнейшего обращения к ним";
    private final String DOGREFUSAL = "Список причин отказа в заборе собаки из приюта";
    private final String CATREFUSAL = "Список причин отказа в заборе животного из приюта";
    private final String VOLONTER = "Связаться с волонтером";
    public static final String CATMAIN = "Приют для кошек";
    private final String CATABOUT = "О приюте для кошек";
    private final String CATRULES = "Правила приюта для кошек";
    private final String CATTAKE = "Как взять кошку из приюта";
    private final String CATSEND = "Прислать отчет для кошек";
    public final static String CATSEND_TXT = "Напишите текст отчета для кошек";
    public final static String CATSEND_MSG = "Отправьте фото отчета для кошек";
    public final static String START = "/start";
    private final String ESCAPE = "Отмена";
    private final String FORVARD = "Дальше";

    public final String STATE_HELP = "HELP";
    public final String STATE_DOG = "DOG";
    public final String STATE_CAT = "CAT";
    public final String STATE_SEND_LETTER = "SEND_LETTER";
    public final String STATE_START = "START";
    public final String STATE_SERVICE = "SERVICE";

    /**
     * Создание основной клавиатуры.
     *
     * @return созданный объект клавиатуры.
     */
    public Keyboard getMainKeyboardMarkup() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[]{DOGMAIN, CATMAIN},
                new String[]{SERVICE, HELP})
                .oneTimeKeyboard(false)     // optional
                .resizeKeyboard(true)       // optional
                .selective(true);           // optional

        keyboard.addRow(new KeyboardButton(CONTACTS).requestContact(true));

        return keyboard;
    }

    /**
     * Создание инлайн клавиатуры.
     *
     * @param command текст команды, от которой зависит содержание клавиатуры.
     * @return созданный объект клавиатуры.
     * @throws NullPointerException - параметр <code>command</code> равен null.
     */
    public InlineKeyboardMarkup getInlineKeyboard(String command) {
        if (command == null) {
            LOGGER.error("Command is null");
            throw new NullPointerException("Command is null");
        }

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        if (command.equals(DOGMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGABOUT).callbackData(DOGABOUT));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRULES).callbackData(DOGRULES));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGTAKE).callbackData(DOGTAKE));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGSEND).callbackData(DOGSEND_MSG));
        }

        if (command.equals(CATMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(CATABOUT).callbackData("CATABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATRULES).callbackData("CATRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATTAKE).callbackData(CATTAKE));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATSEND).callbackData("CATSEND"));
        }

        if (command.equals(DOGTAKE)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGZN).callbackData("DOGZN"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGDOCUMENTS).callbackData("DOGDOCUMENTS"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGTRANSPORT).callbackData("DOGTRANSPORT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEPUPPY).callbackData("DOGHOMEPUPPY"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEDOG).callbackData("DOGHOMEDOG"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEDOGLIMITED).callbackData("DOGHOMEDOGLIMITED"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGADVICECYNOLOGIST).callbackData("DOGADVICECYNOLOGIST"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRECOMMENDATION).callbackData("DOGRECOMMENDATION"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGREFUSAL).callbackData("DOGREFUSAL"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CONTACTS).callbackData("CONTACTS"));
            inlineKeyboard.addRow(new InlineKeyboardButton(VOLONTER).callbackData("VOLONTER"));
        }

        if (command.equals(CATTAKE)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(CATZN).callbackData("CATZN"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATDOCUMENTS).callbackData("CATDOCUMENTS"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATTRANSPORT).callbackData("CATTRANSPORT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATHOMEPUPPY).callbackData("CATHOMEPUPPY"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATHOMEDOG).callbackData("CATHOMEDOG"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATHOMEDOGLIMITED).callbackData("CATHOMEDOGLIMITED"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATREFUSAL).callbackData("CATREFUSAL"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CONTACTS).callbackData("CONTACTS"));
            inlineKeyboard.addRow(new InlineKeyboardButton(VOLONTER).callbackData("VOLONTER"));
        }

        if (command.equals(DOGSEND_MSG)) {
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(ESCAPE).callbackData(DOGMAIN),
                            new InlineKeyboardButton(FORVARD).callbackData(DOGSEND_TXT)
                    });
        }

        if (command.equals(DOGSEND_TXT)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(ESCAPE).callbackData(DOGMAIN));
        }

        if (command.equals(CATSEND_MSG)) {
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(ESCAPE).callbackData(CATMAIN),
                            new InlineKeyboardButton(FORVARD).callbackData(CATSEND_TXT)
                    });
        }

        if (command.equals(CATSEND_TXT)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(ESCAPE).callbackData(CATMAIN));
        }
//Конец - Кнопки отправки отчета

//Кнопка "Назад"
        switch (command) {
            case "DOGZN":
            case "DOGDOCUMENTS":
            case "DOGHOMEPUPPY":
            case "DOGHOMEDOG":
            case "DOGHOMEDOGLIMITED":
            case "DOGADVICECYNOLOGIST":
            case "DOGRECOMMENDATION":
            case "DOGREFUSAL":
            case "DOGCONTACT":
            case "DOGVOLONTER":
                inlineKeyboard.addRow(new InlineKeyboardButton(BACK).callbackData(DOGTAKE));
        }
        switch (command) {
            case DOGRULES:
            case DOGTAKE:
            case DOGABOUT:
                inlineKeyboard.addRow(new InlineKeyboardButton(BACK).callbackData(DOGMAIN));
        }
        return inlineKeyboard;
    }
//конец - Кнопка "Назад"


    /**
     * Функция возвращает имя нажатой кнопки соответствующей команде пользователя.
     *
     * @param command команда пользователя.
     * @param status  результат, который возвращается в случае отсутствия совпадений.
     * @return имя кнопки.
     * @throws NullPointerException - параметр <code>command</code> равен null.
     */
    @Nullable
    public String getState(String command, String status) {
        if (command == null) {
            LOGGER.error("Command is null");
            throw new NullPointerException("Command is null");
        }

        if (status == null) {
            LOGGER.error("Status is null");
            throw new NullPointerException("Status is null");
        }

        if (command.equals(CONTACTS)) {
            return CONTACTS;
        }

        if (command.equals(HELP)) {
            return STATE_HELP;
        }

        if (command.equals(START)) {
            return STATE_START;
        }

        if (command.equals(SERVICE)) {
            return STATE_SERVICE;
        }

        if (command.equals(DOGMAIN)) {
            return DOGMAIN;
        }

        if (command.equals(DOGTAKE)) {
            return DOGTAKE;
        }

        if (command.equals(DOGZN)) {
            return DOGZN;
        }

        if (command.equals(DOGDOCUMENTS)) {
            return DOGDOCUMENTS;
        }
        if (command.equals(DOGTRANSPORT)) {
            return DOGTRANSPORT;
        }

        if (command.equals(DOGHOMEPUPPY)) {
            return DOGHOMEPUPPY;
        }

        if (command.equals(DOGHOMEDOG)) {
            return DOGHOMEDOG;
        }

        if (command.equals(DOGHOMEDOGLIMITED)) {
            return DOGHOMEDOGLIMITED;
        }

        if (command.equals(DOGADVICECYNOLOGIST)) {
            return DOGADVICECYNOLOGIST;
        }

        if (command.equals(DOGRECOMMENDATION)) {
            return DOGRECOMMENDATION;
        }

        if (command.equals(DOGREFUSAL)) {
            return DOGREFUSAL;
        }

/*        if (command.equals(DOGCONTACT)) {
            return CONTACTS;
        }*/

        if (command.equals(DOGSEND)) {
            return DOGSEND;
        }

        if (command.equals(DOGSEND_MSG)) {
            return DOGSEND_MSG;
        }

        if (command.equals(DOGSEND_TXT)) {
            return DOGSEND_TXT;
        }

        if (command.equals(CATSEND)) {
            return CATSEND;
        }

        if (command.equals(CATSEND_MSG)) {
            return CATSEND_MSG;
        }

        if (command.equals(CATSEND_TXT)) {
            return CATSEND_TXT;
        }

        if (command.equals(CATTAKE)) {
            return CATTAKE;
        }
        if (command.equals(CATZN)) {
            return CATZN;
        }

        if (command.equals(CATDOCUMENTS)) {
            return CATDOCUMENTS;
        }
        if (command.equals(CATTRANSPORT)) {
            return CATTRANSPORT;
        }

        if (command.equals(CATHOMEPUPPY)) {
            return CATHOMEPUPPY;
        }

        if (command.equals(CATHOMEDOG)) {
            return CATHOMEDOG;
        }

        if (command.equals(CATHOMEDOGLIMITED)) {
            return CATHOMEDOGLIMITED;
        }
        if (command.equals(CATREFUSAL)) {
            return CATREFUSAL;
        }
        return status;
    }

    /**
     * Функция возвращает текст сообщения для пользователя.
     *
     * @param command команда пользователя.
     * @return текст сообщения для пользователя. Может быть null.
     * @throws NullPointerException - параметр <code>command</code> равен null.
     */
    @Nullable
    public String getMessage(String command) {
        if (command == null) {
            LOGGER.error("Command is null");
            throw new NullPointerException("Command is null");
        }

        if (command.equals(CONTACTS)) {
            return "Спасибо за предоставленные контакты. Будем на связи!";
        }

        if (command.equals(HELP)) {
            return "<b>" + HELP + "</b> \n " +
                    "\n/start - старт бота" + "\n" +
                    "\n/list - список животных" + "\n" +
                    "\n/... - и т.д. ";
        }

        if (command.equals(SERVICE)) {
            return "<b>" + SERVICE + "</b> \n " +
                    "\nОставьте сообщение для работников приюта" + "\n";
        }

        if (command.equals("DOGABOUT")) {
            return "<b>" + DOGABOUT + "</b> \n " + "Приют находится по адресу ..." + "Время работы с 7-00   до  19-00" + "\nЗдесь будет список документов" + "\n" + "<i>" + "скачать - /file1_dog" + "</i>" + "\n" + "<i>" + "скачать - /file2_dog" + "</i>";
        }

        if (command.equals("CATABOUT")) {
            return "<b>" + CATABOUT + "</b> \n " + "Приют находится по адресу ..." + "Время работы с 7-00   до  19-00" + "\nЗдесь будет список документов" + "\n" + "<i>" + "скачать - /file1_cat" + "</i>" + "\n" + "<i>" + "скачать - /file2_cat" + "</i>";
        }

        if (command.equals(DOGRULES)) {
            return "<b>" + DOGRULES + "</b> \n " + readFile("documents/DOGRULES.txt");
        }

        if (command.equals("CATRULES")) {
            return "<b>" + CATRULES + "</b> \n " + readFile("documents/CATRULES.txt");
        }

        if (command.equals(DOGSEND_MSG)) {
            return "<b>" + DOGSEND_MSG + "</b>  ";
        }

        if (command.equals(DOGSEND_TXT)) {
            return "<b>" + DOGSEND_TXT + "</b>  ";
        }

        if (command.equals(CATSEND_MSG)) {
            return "<b>" + CATSEND_MSG + "</b>  ";
        }

        if (command.equals(CATSEND_TXT)) {
            return "<b>" + CATSEND_TXT + "</b>  ";
        }

      /*  if (command.equals("CATTAKE")) {
            return "<b>" + CATTAKE + "</b> \n " +
                    "Съездите посмотреть на кошек в приют. Расспросите сотрудников о животных, попросите подобрать вам питомца.\n" +
                    "Купите всё, что нужно кошке: корм по рекомендации из приюта, лоток, наполнитель, миски, когтеточку, игрушки,\n" +
                    " домик, переноску. \n" +
                    "Выделите животному тихое место, где он сможет адаптироваться к новой обстановке.\n" +
                    "Заберите животное. В первые дни не нужно его тормошить — дайте ему время на то, чтобы привыкнуть к дому.\n" +
                    "Свозите кошку к ветеринару. В приютах обычно есть свои специалисты, но не лишним будет перестраховаться. ";
        }*/

        if (command.equals("DOGZN")) {
            return "<b>" + DOGZN + "</b> \n " + readFile("documents/DOGZN.txt");
        }

        if (command.equals("DOGDOCUMENTS")) {
            return "<b>" + DOGDOCUMENTS + "</b> \n " + readFile("documents/DOGDOCUMENTS.txt");
        }

        if (command.equals("DOGHOMEPUPPY")) {
            return "<b>" + DOGHOMEPUPPY + "</b> \n " + readFile("documents/DOGHOMEPUPPY.txt");
        }

        if (command.equals("DOGHOMEDOG")) {
            return "<b>" + DOGHOMEDOG + "</b> \n " + readFile("documents/DOGHOMEDOG.txt");
        }

        if (command.equals("DOGHOMEDOGLIMITED")) {
            return "<b>" + DOGHOMEDOGLIMITED + "</b> \n " + readFile("documents/DOGHOMEDOGLIMITED.txt");
        }

        if (command.equals("DOGADVICECYNOLOGIST")) {
            return "<b>" + DOGADVICECYNOLOGIST + "</b> \n " + readFile("documents/DOGADVICECYNOLOGIST.txt");
        }

        if (command.equals("DOGRECOMMENDATION")) {
            return "<b>" + DOGRECOMMENDATION + "</b> \n " + readFile("documents/DOGRECOMMENDATION.txt");
        }

        if (command.equals("DOGREFUSAL")) {
            return "<b>" + DOGREFUSAL + "</b> \n " + readFile("documents/DOGREFUSAL.txt");
        }
        if (command.equals("CATREFUSAL")) {
            return "<b>" + CATREFUSAL + "</b> \n " + readFile("documents/DOGREFUSAL.txt");
        }

        if (command.equals("DOGCONTACT")) {
            return "<b>" + CONTACTS + "</b> \n " + "Спасибо за предоставленные контакты. Будем на связи!";
            // "<i>" + "\nВведите ваш номер телефона для связи в формате <u>+7999999999</u>:" + "\n" + "</i>";
        }

        return command;
    }
}