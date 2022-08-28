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
    private final String DOGSEND = "Прислать отчет";

    private final String CATMAIN = "Приют для кошек";
    private final String CATABOUT = "О приюте для кошек";
    private final String CATRULES = "Правила приюта для кошек";
    private final String CATSEND = "Прислать отчет";
    private final String START = "/start";


    public final  String  STATE_HELP = "HELP";
    public final   String STATE_DOG = "DOG";
    public final   String STATE_CAT = "CAT";
    public final  String STATE_SEND_LETTER = "SEND_LETTER";
    public final  String STATE_START = "START";
    public final  String STATE_SERVICE = "SERVICE";


    /*    private InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton[]{ new InlineKeyboardButton("url").url("www.google.com")},
            new InlineKeyboardButton[] { new InlineKeyboardButton("callback_data").callbackData("456")},
            new InlineKeyboardButton[]{ new InlineKeyboardButton("Switch!").callbackData("/start")
            });

*/

    /**
     * Создание основной клавиатуры
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
     * @param flag параметр передает команду
     * @return
     */
    public InlineKeyboardMarkup getInlineKeyboard(String flag) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        if (flag.equals(DOGMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGABOUT).callbackData("DOGABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGRULES).callbackData("DOGRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(DOGSEND).callbackData("DOGSEND"));
        }
        if (flag.equals(CATMAIN)) {
            inlineKeyboard.addRow(new InlineKeyboardButton(CATABOUT).callbackData("CATABOUT"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATRULES).callbackData("CATRULES"));
            inlineKeyboard.addRow(new InlineKeyboardButton(CATSEND).callbackData("CATSEND"));
        }

        return inlineKeyboard;
    }

    /**
     * Возвращает строку - нажатой кнопки , последние команды
     * @param flag
     * @return
     */
    public String getState(String flag,String btnStatus){
        if(flag.equals(HELP)) {
            return  STATE_HELP;
        }

        if (flag.equals(START)) {
            return STATE_START;
        }

        if (flag.equals(SERVICE)) {
            return STATE_SERVICE;
        }

        if (flag.equals("Приют для собак")) {
            return DOGMAIN;
        }
        if (flag.equals("О приюте для собак")) {
            return DOGABOUT;
        }
        if (flag.equals("Правила приюта для собак")) {
            return DOGRULES;
        }
        if (flag.equals("Прислать отчет")) {
            return DOGSEND;
        }

        return btnStatus;
    }

    /**
     * возвращает текст, который будет передан в сообщение
     * @param flag команды с клавиатуры, зависит выбор текста
     * @param btnStatus
     * @return
     */
    public String getText(String flag,String btnStatus) {

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
                    "Время работы с 7-00   до  19-00"+
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_dog" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_dog" + "</i>";
        }
        if (flag.equals("CATABOUT")) {
            return "<b>" + CATABOUT + "</b> \n " +
                    "Приют находится по адресу ..." +
                    "Время работы с 7-00   до  19-00"+
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
        }


        if (flag.equals("DOGRULES")) {
            return "<b>" + DOGRULES + "</b> \n " +
                    "\nЗдесь будет список документов" + "\n" +
                    "<i>" + "скачать - /file1_cat" + "</i>" + "\n" +
                    "<i>" + "скачать - /file2_cat" + "</i>";
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
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых"+ "</i>";
        }
        if (flag.equals("CATSEND")) {
            return "<b>" + CATSEND + "</b> \n " +
                    "<i>" + "\nОтчет должен содержать:" + "\n" +
                    "\n- *Фото животного" +
                    "\n- *Рацион животного" +
                    "\n- *Общее самочувствие и привыкание к новому месту" +
                    "\n- *Изменение в поведении: отказ от старых привычек, приобретение новых"+ "</i>";
        }

        return null;
    }
}