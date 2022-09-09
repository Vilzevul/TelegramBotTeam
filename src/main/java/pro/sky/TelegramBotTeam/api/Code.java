package pro.sky.TelegramBotTeam.api;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.lang.NonNull;

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

/**
 * Вспомогательный класс для работы с файлами.
 */
public class Code {
    /**
     * Принимает, обрабатывает и возвращает содержимое файла в виде массива байт.
     *
     * @param telegramBot бот.
     * @param fileID ID файла.
     * @return данные файла в виде массива байт.
     */
    public static byte[] getFileContent(@NonNull TelegramBot telegramBot, @NonNull String fileID) throws IOException {
        GetFile request = new GetFile(fileID);
        GetFileResponse getFileResponse = telegramBot.execute(request);
        File file = getFileResponse.file();
        byte[] fileContent = telegramBot.getFileContent(file);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent);
        BufferedImage imgIn = ImageIO.read(byteArrayInputStream);
        if (imgIn == null) return null;

        //Преобразовать изображение в  формат меньшего размера
        double height = imgIn.getHeight() / (imgIn.getWidth() / 100d);
        BufferedImage imgOut = new BufferedImage(100, (int) height, imgIn.getType());
        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage((Image) imgIn, 0, 0, 100, (int) height, null);
        graphics.dispose();

        ImageIO.write(imgOut, getExtension(file.filePath()), byteArrayOutputStream);

        //Проверить, что файл сохраняется
        java.io.File dir = new java.io.File("c:/temp");
        dir.mkdir();
        java.io.File fileOut = new java.io.File("c:/temp", "test." + getExtension(file.filePath()));
        ImageIO.write(imgOut, getExtension(file.filePath()), fileOut);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Возвращает расширение файла.
     * Можно заменить этим:
     * org.apache.commons.io.FilenameUtils
     * Maven: commons-io:commons-io:2.11.0 (commons-io-2.11.0.jar)
     *
     * @param fileName имя файла.
     * @return расширение файла.
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
     * Преобразует текстовый файл в строку
     *
     * @param file файл.
     * @return содержимое файла в виде строки.
     */
    public static String readFile(String file) {
        Path path = Paths.get(file);
        if (Files.exists(path)) {
            String contents = null;
            try {
                contents = Files.readString(path, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                //Handle exception
            }
            return contents;
        }
        return "null";
    }
}

