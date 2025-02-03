package com.gold_hunter.gold_hunter_bot.repositorys;

import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Integer> {

    boolean existsByUserId(String userId);

    TelegramUser findByUserId(String userId);

    Iterable<TelegramUser> findAllByStateSearch(boolean stateSearch);

    Iterable<TelegramUser> findAllByFailedOrders(int failedOrders);
}
