package com.gold_hunter.gold_hunter.repositorys;

import com.gold_hunter.gold_hunter.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {

}
