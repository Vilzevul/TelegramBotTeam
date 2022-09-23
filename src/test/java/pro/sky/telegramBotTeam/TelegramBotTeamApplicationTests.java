package pro.sky.telegramBotTeam;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.telegramBotTeam.listener.TelegramBotUpdatesListener;

@SpringBootTest
class TelegramBotTeamApplicationTests {
	@Autowired
	TelegramBotUpdatesListener telegramBotUpdatesListener;

	@Test
	void contextLoads() {
		Assertions.assertThat(telegramBotUpdatesListener).isNotNull();
	}
}
