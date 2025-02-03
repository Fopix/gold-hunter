package com.gold_hunter.gold_hunter.repositorys;

import com.gold_hunter.gold_hunter.models.PaymentMethod;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {

    List<PaymentMethod> findByPaymentSystem(String paymentSystem);
}
