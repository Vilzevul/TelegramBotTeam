package pro.sky.TelegramBotTeam.api;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.TelegramBotTeam.model.Users;

@Component
public class KeyBoardButton {

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


    /*    private InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton[]{ new InlineKeyboardButton("url").url("www.google.com")},
            new InlineKeyboardButton[] { new InlineKeyboardButton("callback_data").callbackData("456")},
            new InlineKeyboardButton[]{ new InlineKeyboardButton("Switch!").callbackData("/start")
            });

*/

    /**
     * Создание основной клавиатуры
     *
     * @return
     */
    public Keyboard getMainKeyboardMarkup() {
        Keyboard mainKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{DOGMAIN, CATMAIN},
                new String[]{SERVICE, HELP})
//            new String[]{"BUTTON_CB"}, new String[]{"BUTTON_EXCHANGE"})
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional

        return mainKeyboardMarkup;
    }

    /**
     * создание инлайн клавиатуры, от команды зависит содержание клавиатуры
     *
     * @param flag параметр передает команду
     * @return
     */
    public InlineKeyboardMarkup getInlineKeyboard(String flag) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        if (flag.equals(DOGMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGABOUT).callbackData("DOGABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRULES).callbackData("DOGRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGTAKE).callbackData(DOGTAKE));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGSEND).callbackData("DOGSEND"));
        }
        if (flag.equals(CATMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(CATABOUT).callbackData("CATABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATRULES).callbackData("CATRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATSEND).callbackData("CATSEND"));
        }
//Если нажали кнопку "Как взять собаку из приюта"
        if (flag.equals(DOGTAKE)) {
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGZN).callbackData("DOGZN")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGDOCUMENTS).callbackData("DOGDOCUMENTS")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGHOMEPUPPY).callbackData("DOGHOMEPUPPY")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGHOMEDOG).callbackData("DOGHOMEDOG")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGHOMEDOGLIMITED).callbackData("DOGHOMEDOGLIMITED")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGADVICECYNOLOGIST).callbackData("DOGADVICECYNOLOGIST")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGRECOMMENDATION).callbackData("DOGRECOMMENDATION")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGREFUSAL).callbackData("DOGREFUSAL")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGCONTACT).callbackData("DOGCONTACT")});
            inlineKeyboard.addRow(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(DOGVOLONTER).callbackData("DOGVOLONTER")});
        }
        return inlineKeyboard;
    }

    /**
     * Возвращает строку - нажатой кнопки , последние команды
     *
     * @param flag
     * @return
     */
    public String getState(String flag, String btnStatus) {
        if (flag.equals(HELP)) {
            return STATE_HELP;
        }

        if (flag.equals(START)) {
            return STATE_START;
        }

        if (flag.equals(SERVICE)) {
            return STATE_SERVICE;
        }

        if (flag.equals(DOGMAIN)) {
            return DOGMAIN;
        }
        if (flag.equals(DOGTAKE)) {
            return DOGTAKE;
        }
        if (flag.equals(DOGZN)) {
            return DOGZN;
        }
        if (flag.equals(DOGDOCUMENTS)) {
            return DOGDOCUMENTS;
        }
        if (flag.equals(DOGHOMEPUPPY)) {
            return DOGHOMEPUPPY;
        }
        if (flag.equals(DOGHOMEDOG)) {
            return DOGHOMEDOG;
        }
        if (flag.equals(DOGHOMEDOGLIMITED)) {
            return DOGHOMEDOGLIMITED;
        }
        if (flag.equals(DOGADVICECYNOLOGIST)) {
            return DOGADVICECYNOLOGIST;
        }
        if (flag.equals(DOGRECOMMENDATION)) {
            return DOGRECOMMENDATION;
        }
        if (flag.equals(DOGREFUSAL)) {
            return DOGREFUSAL;
        }
        if (flag.equals(DOGCONTACT)) {
            return DOGCONTACT;
        }

        return btnStatus;
    }

    /**
     * возвращает текст, который будет передан в сообщение
     *
     * @param flag      команды с клавиатуры, зависит выбор текста
     * @param btnStatus
     * @return
     */
    public String getText(String flag, String btnStatus) {

        if (flag.equals(HELP)) {
            return "<b>" + HELP + "</b> \n " +
                    "\n/start - старт бота" + "\n" +
                    "\n/list - список животных" + "\n" +
                    "\n/... - и т.д. ";

        }
        if (flag.equals(SERVICE)) {
            return "<b>" + SERVICE + "</b> \n " +
                    "\nОставьте сообщение для работников приюта" + "\n";

        }
        if (flag.equals("DOGABOUT")) {
            return "<b>" + DOGABOUT + "</b> \n " +
                    "Приют находится по адресу ..." +
                    "Время работы с 7-00   до  19-00" +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_dog" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_dog" + "</i>";
        }
        if (flag.equals("CATABOUT")) {
            return "<b>" + CATABOUT + "</b> \n " +
                    "Приют находится по адресу ..." +
                    "Время работы с 7-00   до  19-00" +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
        }


        if (flag.equals("DOGRULES")) {
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

        if (flag.equals("CATRULES")) {
            return "<b>" + CATRULES + "</b> \n " +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
        }

        if (flag.equals("DOGSEND")) {
            return "<b>" + DOGSEND + "</b> \n " +
                    "<i>" + "\nОтчет должен содержать:" + "\n" +
                    "\n- *Фото животного" +
                    "\n- *Рацион животного" +
                    "\n- *Общее самочувствие и привыкание к новому месту" +
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых" + "</i>";
        }
        if (flag.equals("CATSEND")) {
            return "<b>" + CATSEND + "</b> \n " +
                    "<i>" + "\nОтчет должен содержать:" + "\n" +
                    "\n- *Фото животного" +
                    "\n- *Рацион животного" +
                    "\n- *Общее самочувствие и привыкание к новому месту" +
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых" + "</i>";
        }

        if (flag.equals("DOGZN")) {
            return "<b>" + DOGZN + "</b> \n " +
                    "<i>" + "\nПравила знакомства с собакой:" + "\n" + "</i>";
        }

        if (flag.equals("DOGDOCUMENTS")) {
            return "<b>" + DOGDOCUMENTS + "</b> \n " +
                    "<i>" + "\nСписок документов, чтобы взять собаку:" + "\n" + "</i>";
        }

        if (flag.equals("DOGHOMEPUPPY")) {
            return "<b>" + DOGHOMEPUPPY + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для щенка:" + "\n" + "</i>";
        }
        if (flag.equals("DOGHOMEDOG")) {
            return "<b>" + DOGHOMEDOG + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для взрослой собаки:" + "\n" + "</i>";
        }
        if (flag.equals("DOGHOMEDOGLIMITED")) {
            return "<b>" + DOGHOMEDOGLIMITED + "</b> \n " +
                    "<i>" + "\nСписок рекомендаций по обустройству дома для собаки с ограниченными возможностями:" + "\n" + "</i>";
        }
        if (flag.equals("DOGADVICECYNOLOGIST")) {
            return "<b>" + DOGADVICECYNOLOGIST + "</b> \n " +
                    "<i>" + "\nСоветы кинолога по первичному общению с собакой:" + "\n" + "</i>";
        }
        if (flag.equals("DOGRECOMMENDATION")) {
            return "<b>" + DOGRECOMMENDATION + "</b> \n " +
                    "<i>" + "\nРекомендации по проверенным кинологам для дальнейшего обращения к ним:" + "\n" + "</i>";
        }
        if (flag.equals("DOGREFUSAL")) {
            return "<b>" + DOGREFUSAL + "</b> \n " +
                    "<i>" + "\nСписок причин отказа в заборе собаки из приюта:" + "\n" + "</i>";
        }
        if (flag.equals("DOGCONTACT")) {
            return "<b>" + DOGCONTACT + "</b> \n " +
                    "<i>" + "\nВведите ваш номер телефона для связи в формате <u>+7999999999</u>:" + "\n" + "</i>";
        }

        return null;
    }
}