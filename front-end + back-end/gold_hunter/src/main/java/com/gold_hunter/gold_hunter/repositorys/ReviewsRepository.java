package com.gold_hunter.gold_hunter.repositorys;

import com.gold_hunter.gold_hunter.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ReviewsRepository extends CrudRepository<Review, Integer> {

    Page<Review> findAll(Pageable pageable);

    boolean existsByOrderId(int orderId);

    Review findByOrderId(int orderId);
}
