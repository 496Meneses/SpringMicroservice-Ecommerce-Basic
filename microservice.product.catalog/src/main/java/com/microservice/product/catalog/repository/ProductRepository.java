package com.microservice.product.catalog.repository;

import com.microservice.product.catalog.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
