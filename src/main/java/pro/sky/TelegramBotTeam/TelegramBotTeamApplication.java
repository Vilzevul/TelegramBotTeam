package pro.sky.TelegramBotTeam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotTeamApplication.class, args);
	}

}
