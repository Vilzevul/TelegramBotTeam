package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.repository.AdoptionRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с усыновителями.
 */
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
     * Возвращает информацию по усыновлению для усыновителя.
     *
     * @param idParent id усыновителя (ссылка на Member).
     * @return информация по усыновлению. Может вернуть null, если такая запись отсутствует.
     */
    public Adoption getAdoption(Long idParent) {
        return adoptionRepository.findFirstByParent_Id(idParent).orElse(null);
    }

    /**
     * Возвращает информацию по усыновлению для усыновителя с указанным статусом.
     *
     * @param idParent id усыновителя (ссылка на Member).
     * @param status статус.
     * @return информация по усыновлению. Может вернуть null, если такая запись отсутствует.
     */
    public Adoption getAdoptionOnStatus(Long idParent, Adoption.AdoptionStatus status) {
        return adoptionRepository.findFirstByParent_IdAndStatus(idParent, status).orElse(null);
    }

    /**
     * Сохранить/обновить данные записи усыновления.
     *
     * @param adoption запись усыновления.
     */
    public Adoption createAdoption(Adoption adoption) {
        LOGGER.info("Добавлена новая запись усыновления");
        return adoptionRepository.save(adoption);
    }

    /**
     * Обновить текущий статус для записи усыновления.
     *
     * @param id ID записи усыновления.
     * @param status новый статус.
     */
    @Transactional
    public void updateAdoptionStatus(Long id, Adoption.AdoptionStatus status) {
        LOGGER.info("Запись усыновления {} изменила статус: {}", id, status);
        adoptionRepository.updateAdoptionStatusById(id, status.toString());
    }

    /**
     * Обновить текущий статус для записи усыновления.
     *
     * @param idUser ID пользователя (ссылка на Users).
     * @param idShelter ID приюта.
     * @param status новый статус.
     */
    @Transactional
    public void updateAdoptionStatus(Long idUser, Long idShelter, Adoption.AdoptionStatus status) {
        LOGGER.info("Запись усыновления для пользователя {} из приюта {} изменила статус: {} ", idUser, idShelter, status);
        adoptionRepository.updateAdoptionStatusByIdUserAndIdShelter(idUser, idShelter, status.toString());
    }
}

