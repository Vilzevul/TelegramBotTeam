package pro.sky.TelegramBotTeam.api;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Code {


    /**
     * Принимает от пользователя фото отчета, обрабатывает и возвращает byte[] для
     * последующей записи в БД в Report
     *
     * @param document
     * @return
     * @throws IOException
     */

    public static byte[] getFile(TelegramBot telegramBot,Document document) throws IOException {
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
         */
         java.io.File dir = new java.io.File("c:/temp");
         dir.mkdir();
         java.io.File fileOut = new java.io.File("c:/temp", "test." + getExtension(document.fileName()));
         ImageIO.write(imgOut, getExtension(document.fileName()), fileOut);


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
    private static String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    /**
     * считывает текстовый файл в строку
     * @param file
     * @return
     */
    public static String readFile(String file) {

        Path path = Paths.get(file);
        if (Files.exists(path)) {
            String contents = null;
            try {
                contents = Files.readString(path, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                // Handle exception
            }
            return contents;
        }
        return "null";
    }
}

