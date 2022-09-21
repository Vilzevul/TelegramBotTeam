package pro.sky.telegramBotTeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegramBotTeam.model.Adoption;
import pro.sky.telegramBotTeam.repository.AdoptionRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

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
    public Adoption getAdoptionOnStatus(Long idParent,Adoption.AdoptionStatus status) {
        return adoptionRepository.findFirstByParent_IdAndStatus(idParent,status.toString()).orElse(null);
    }

    /**
     * Сохранить/обновить данные записи усыновления.
     *
     * @param adoption запись усыновления.
     */
    public Adoption createAdoption(Adoption adoption) {

            adoptionRepository.save(adoption);

        return adoption;
    }

    //delete
    public Adoption findAdoptionOrCreate(Long id) throws IOException {
        return adoptionRepository.findFirstByParent_Id(id).orElse(new Adoption());
    }

    /**
     * Обновить текущий статус для записи усыновления.
     *
     * @param id ID записи усыновления.
     * @param status новый статус.
     */
    @Transactional
    public void updateAdoptionStatus(Long id, Adoption.AdoptionStatus status) {
        adoptionRepository.updateAdoptionStatus(id, status.toString());
        LOGGER.info("Запись усыновления {} изменила статус: {}", id, status);
    }
}
