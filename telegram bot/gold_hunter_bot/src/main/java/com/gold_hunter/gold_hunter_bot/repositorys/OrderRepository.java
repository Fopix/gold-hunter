package com.gold_hunter.gold_hunter_bot.repositorys;

import com.gold_hunter.gold_hunter_bot.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    Order findById(int id);

    Iterable<Order> findAllByExecutor(String executor);

    Iterable<Order> findAllByStateAndStateTelegram(int state, int stateTelegram);

    Iterable<Order> findAllByStateGreaterThanAndStateLessThan(int min, int max);

    Iterable<Order> findAllByStateAndStateTelegramLessThan(int state, int stateTelegram);
}
