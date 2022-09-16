package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Shelter;
import pro.sky.telegramBotTeam.repository.ShelterRepository;

import java.time.LocalDate;

@Service
public class ShelterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Возвращает запись приюта по указанному названию.
     *
     * @param name название приюта.
     * @return запись о приюте. Может вернуть null, если такая отсутствует.
     */
    public Shelter getShelter(Shelter.ShelterType name) {
        return shelterRepository.findByName(name.toString()).orElse(null);
    }
}
