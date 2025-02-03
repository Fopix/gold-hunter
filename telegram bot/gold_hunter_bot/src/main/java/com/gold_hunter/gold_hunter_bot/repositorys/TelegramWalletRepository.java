package com.gold_hunter.gold_hunter_bot.repositorys;

import com.gold_hunter.gold_hunter_bot.models.TelegramWallet;
import org.springframework.data.repository.CrudRepository;

public interface TelegramWalletRepository extends CrudRepository<TelegramWallet, Integer> {

    TelegramWallet findByUserId(String userId);
}
