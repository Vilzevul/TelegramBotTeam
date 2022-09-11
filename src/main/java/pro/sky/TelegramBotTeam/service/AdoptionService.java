package pro.sky.TelegramBotTeam.service;

import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.TelegramBotTeam.model.Adoption;
import pro.sky.TelegramBotTeam.model.Users;
import pro.sky.TelegramBotTeam.repository.AdoptionRepository;

import java.util.ArrayList;
import java.util.Collection;
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
     * Возвращает информацию по усыновлению для указанного усыновителя.
     *
     * @param idParent id усыновителя.
     * @return информация по усыновлению. Может вернуть null, если такая запись отсутствует.
     */
    public Adoption getAdoption(Long idParent) {
        return adoptionRepository.findByIdParent(idParent).orElse(null);
    }

    public Adoption createAdoption(Adoption adoption) {
        if(getAdoption(adoption.getParent().getId()) == null) adoptionRepository.save(adoption);
        return adoption;

    }


}
