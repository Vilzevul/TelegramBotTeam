package pro.sky.TelegramBotTeam;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;

@Component
public class KeyBoardButton {

    private final String HALP = "Помощь";
    private final String SERVICE = "Оставить сообщение";

    private final String DOGMAIN = "Приют для собак";
    private final String DOGABOUT = "О приюте для собак";
    private final String DOGRULES = "Правила приюта для собак";
    private final String DOGSEND = "Прислать отчет";

    private final String CATMAIN = "Приют для кошек";
    private final String CATABOUT = "О приюте для кошек";
    private final String CATRULES = "Правила приюта для кошек";
    private final String CATSEND = "Прислать отчет";


    /*    private InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton[]{ new InlineKeyboardButton("url").url("www.google.com")},
            new InlineKeyboardButton[] { new InlineKeyboardButton("callback_data").callbackData("456")},
            new InlineKeyboardButton[]{ new InlineKeyboardButton("Switch!").callbackData("/start")
            });

*/

    /**
     * Создание основной клавиатуры с кнопками
     * @return кнопки с вариантами выбора
     */
    public Keyboard getMainKeyboardMarkup() {
        Keyboard mainKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{DOGMAIN, CATMAIN},
                new String[]{SERVICE, HALP})
//            new String[]{"BUTTON_CB"}, new String[]{"BUTTON_EXCHANGE"})
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional

        return mainKeyboardMarkup;
    }

    /**
     * Создание инлайн клавиатуры
     * @param flag передача команды и определение содержания клавиатуры
     * @return меню инлайн клавиатуры
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

    public String getText(String flag) {

        if (flag.equals(HALP)) {
            return "<b>" + HALP + "</b> \n " +
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

        return flag;
    }
}
