package pro.sky.TelegramBotTeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.TelegramBotTeam.model.Users;

import java.util.Collection;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Modifying
    @Query(value = "UPDATE users SET phone = :phone WHERE id = :id", nativeQuery = true)
    void setUserPhoneById(String phone, Long id);


}