package com.gold_hunter.gold_hunter_bot.repositorys;

import com.gold_hunter.gold_hunter_bot.models.TelegramPassword;
import org.springframework.data.repository.CrudRepository;

public interface TelegramPasswordRepository extends CrudRepository<TelegramPassword, Integer> {

    TelegramPassword findByPassword(String password);

    boolean existsByPassword(String password);
}
