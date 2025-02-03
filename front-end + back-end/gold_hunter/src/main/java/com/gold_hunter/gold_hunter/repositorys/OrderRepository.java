package com.gold_hunter.gold_hunter.repositorys;

import com.gold_hunter.gold_hunter.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    boolean existsByUrl(String url);

    Order findByUrl(String url);

    Iterable<Order> findAllByLifeTimeLessThan(long lifeTime);

    Iterable<Order> findAllByLifeTimeLessThanAndState(long lifeTime, int state);

    Iterable<Order> findAllByStateGreaterThanEqual(int state);

    Iterable<Order> findAllByStateAndPaymentSystemNotAndBillIdNotNull(int state, String paymentSystem);

    Iterable<Order> findAllByStateLessThanAndStateTelegramAndLifeTimeLessThan(int state, int stateTelegram, long lifeTime);
}
