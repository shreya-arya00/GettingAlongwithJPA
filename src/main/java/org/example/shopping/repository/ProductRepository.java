package org.example.shopping.repository;

import org.example.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Collection<Product> findByName(String name);
}
