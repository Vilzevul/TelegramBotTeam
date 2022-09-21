package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.exeption.TelegramBotNotFoundException;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.repository.AdoptionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AdoptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdoptionService.class);

    private final AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    /**
     * Возвращает все записи по усыновлениям.
     *
     * @return список записей по усыновлению.
     */
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    /**
     * Возвращает информацию по усыновлению для указанного усыновителя.
     *
     * @param idParent id усыновителя (ссылка на Member).
     * @return информация по усыновлению. Может вернуть null, если такая запись отсутствует.
     */
    public Adoption getAdoption(Long idParent) {
        return adoptionRepository.findFirstByParent_Id(idParent).orElse(null);
    }

    /**
     * Сохранить/обновить данные записи усыновления.
     *
     * @param adoption запись усыновления.
     */
    public Adoption createAdoption(Adoption adoption) {
        if (getAdoption(adoption.getParent().getId()) == null) {
            adoptionRepository.save(adoption);
        }
        return adoption;
    }

    /**
     * Обновить текущий статус для записи усыновления.
     *
     * @param id     ID записи усыновления.
     * @param status новый статус.
     */
    @Transactional
    public String updateAdoptionStatus(Long id, Adoption.AdoptionStatus status) {
        LOGGER.info("Запись усыновления {} изменила статус: {}", id, status);
        adoptionRepository.updateAdoptionStatus(id, status.toString());
        return "Статус изменен на " + status;
    }

    public Optional<Adoption> searchAdoptionStatus(Long id, int idShelter) {
        LOGGER.info("Запись усыновления {} изменила статус: {}", id, idShelter);
        if (id ==null && idShelter ==0 ) {
            throw new TelegramBotNotFoundException("Пустые входящие данные!");
        }
        return adoptionRepository.searchAdoptionStatus(id, idShelter);
    }
}

