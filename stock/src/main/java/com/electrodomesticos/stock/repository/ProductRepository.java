package com.electrodomesticos.stock.repository;


import com.electrodomesticos.stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
