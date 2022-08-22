package pro.sky.TelegramBotTeam.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration

public class TelegramBotConfiguration {
    private Logger logger = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    @Value("${telegram.bot.token}")
    private String token;

    @Bean

       public TelegramBot telegramBot() {
        logger.info("- Processing telegramBot() - 1");
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}
