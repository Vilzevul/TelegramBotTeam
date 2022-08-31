package pro.sky.TelegramBotTeam.api;

import com.pengrad.telegrambot.model.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class KeyBoardButton {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyBoardButton.class);

    public final static String CONTACTS = "Оставить контакты";
    private final String HELP = "Помощь";
    private final String SERVICE = "Оставить сообщение";
    private final String DOGMAIN = "Приют для собак";
    private final String DOGABOUT = "О приюте для собак";
    private final String DOGRULES = "Правила приюта для собак";
    private final String DOGTAKE = "Как взять собаку из приюта";
    private final String DOGSEND = "Прислать отчет";
    private final String DOGZN = "Правила знакомства с собакой";
    private final String DOGDOCUMENTS = "Список документов, чтобы взять собаку";
    private final String DOGHOMEPUPPY = "Список рекомендаций по обустройству дома для щенка";
    private final String DOGHOMEDOG = "Список рекомендаций по обустройству дома для взр. собаки";
    private final String DOGHOMEDOGLIMITED = "Список реком. по обустройству дома для собаки с огран. возможностями";
    private final String DOGADVICECYNOLOGIST = "Советы кинолога по первичному общению с собакой";
    private final String DOGRECOMMENDATION = "Рекомендации кинолога для дальнейшего обращения к ним";
    private final String DOGREFUSAL = "Список причин отказа в заборе собаки из приюта";
    private final String DOGCONTACT = "Принять и записать контактные данные для связи";
    private final String DOGVOLONTER = "Связаться с волонтером";
    private final String CATMAIN = "Приют для кошек";
    private final String CATABOUT = "О приюте для кошек";
    private final String CATRULES = "Правила приюта для кошек";
    private final String CATSEND = "Прислать отчет";
    private final String START = "/start";

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
                new String[]{ DOGMAIN, CATMAIN },
                new String[]{ SERVICE, HELP })
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
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGABOUT).callbackData("DOGABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRULES).callbackData("DOGRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGTAKE).callbackData(DOGTAKE));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGSEND).callbackData("DOGSEND"));
        }

        if (command.equals(CATMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(CATABOUT).callbackData("CATABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATRULES).callbackData("CATRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATSEND).callbackData("CATSEND"));
        }

        if (command.equals(DOGTAKE)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGZN).callbackData("DOGZN"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGDOCUMENTS).callbackData("DOGDOCUMENTS"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEPUPPY).callbackData("DOGHOMEPUPPY"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEDOG).callbackData("DOGHOMEDOG"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGHOMEDOGLIMITED).callbackData("DOGHOMEDOGLIMITED"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGADVICECYNOLOGIST).callbackData("DOGADVICECYNOLOGIST"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRECOMMENDATION).callbackData("DOGRECOMMENDATION"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGREFUSAL).callbackData("DOGREFUSAL"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGCONTACT).callbackData("DOGCONTACT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGVOLONTER).callbackData("DOGVOLONTER"));
        }

        return inlineKeyboard;
    }

    /**
     * Функция возвращает имя нажатой кнопки соответствующей команде пользователя.
     *
     * @param command команда пользователя.
     * @return имя кнопки. Может быть null.
     * @throws NullPointerException - параметр <code>command</code> равен null.
     */
    @Nullable
    public String getState(String command) {
        if (command == null) {
            LOGGER.error("Command is null");
            throw new NullPointerException("Command is null");
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

        if (command.equals(DOGCONTACT)) {
            return DOGCONTACT;
        }

        return null;
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
            return "<b>" + DOGABOUT + "</b> \n " +
                    "Приют находится по адресу ..." +
                    "Время работы с 7-00   до  19-00" +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_dog" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_dog" + "</i>";
        }

        if (command.equals("CATABOUT")) {
            return "<b>" + CATABOUT + "</b> \n " +
                    "Приют находится по адресу ..." +
                    "Время работы с 7-00   до  19-00" +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
        }

        if (command.equals("DOGRULES")) {
            return "<b>" + DOGRULES + "</b> \n " +
                    "\nЗАПРЕЩАЕТСЯ:" + "\n" +
                    " <i>" + "- Самостоятельно открывать выгулы и вольеры без разрешения работника приюта." + "</i>\n" +
                    " <i>" + "- Кормить животных. Этим Вы можете спровоцировать драку. Угощения разрешены только постоянным опекунам и волонтерам, во время прогулок с животными на поводке" + "</i>\n" +
                    " <i>" + "- Оставлять после себя мусор на территории приюта и прилегающей территории." + "</i>\n" +
                    " <i>" + "- Подходить близко к вольерам и гладить собак через сетку на выгулах. Животные могут быть агрессивны!" + "</i>\n" +
                    " <i>" + "- Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных." + "</i>\n" +
                    " <i>" + "- Посещение приюта для детей дошкольного и младшего школьного возраста без сопровождения взрослых." + "</i>\n" +
                    " <i>" + "- Нахождение на территории приюта детей среднего и старшего школьного возраста без  сопровождения взрослых или письменной справки-разрешения от родителей или законных представителей." + "</i>\n" +
                    " <i>" + "- Посещение приюта в состоянии алкогольного, наркотического опьянения." + "</i>";
        }

        if (command.equals("CATRULES")) {
            return "<b>" + CATRULES + "</b> \n " +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
        }

        if (command.equals("DOGSEND")) {
            return "<b>" + DOGSEND + "</b> \n " +
                    "<i>" + "\nОтчет должен содержать:" + "\n" +
                    "\n- *Фото животного" +
                    "\n- *Рацион животного" +
                    "\n- *Общее самочувствие и привыкание к новому месту" +
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых" + "</i>";
        }

        if (command.equals("CATSEND")) {
            return "<b>" + CATSEND + "</b> \n " +
                    "<i>" + "\nОтчет должен содержать:" + "\n" +
                    "\n- *Фото животного" +
                    "\n- *Рацион животного" +
                    "\n- *Общее самочувствие и привыкание к новому месту" +
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых" + "</i>";
        }

        if (command.equals("DOGZN")) {
            return "<b>" + DOGZN + "</b> \n " +
                    "<i>" + "\nПравила знакомства с собакой:" + "\n" + "</i>";
        }

        if (command.equals("DOGDOCUMENTS")) {
            return "<b>" + DOGDOCUMENTS + "</b> \n " +
                    "<i>" + "\nСписок документов, чтобы взять собаку:" + "\n" + "</i>";
        }

        if (command.equals("DOGHOMEPUPPY")) {
            return "<b>" + DOGHOMEPUPPY + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для щенка:" + "\n" + "</i>";
        }

        if (command.equals("DOGHOMEDOG")) {
            return "<b>" + DOGHOMEDOG + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для взрослой собаки:" + "\n" + "</i>";
        }

        if (command.equals("DOGHOMEDOGLIMITED")) {
            return "<b>" + DOGHOMEDOGLIMITED + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для собаки с ограниченными возможностями:" + "\n" + "</i>";
        }

        if (command.equals("DOGADVICECYNOLOGIST")) {
            return "<b>" + DOGADVICECYNOLOGIST + "</b> \n " +
                    "<i>" + "\nСоветы кинолога по первичному общению с собакой:" + "\n" + "</i>";
        }

        if (command.equals("DOGRECOMMENDATION")) {
            return "<b>" + DOGRECOMMENDATION + "</b> \n " +
                    "<i>" + "\nРекомендации по проверенным кинологам для дальнейшего обращения к ним:" + "\n" + "</i>";
        }

        if (command.equals("DOGREFUSAL")) {
            return "<b>" + DOGREFUSAL + "</b> \n " +
                    "<i>" + "\nСписок причин отказа в заборе собаки из приюта:" + "\n" + "</i>";
        }

        if (command.equals("DOGCONTACT")) {
            return "<b>" + DOGCONTACT + "</b> \n " +
                    "<i>" + "\nВведите ваш номер телефона для связи в формате <u>+7999999999</u>:" + "\n" + "</i>";
        }

        return null;
    }
}