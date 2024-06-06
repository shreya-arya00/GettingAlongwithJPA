package org.example.shopping.service;

import org.example.shopping.entity.Product;
import org.example.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(Math.toIntExact(Long.valueOf(id)));
    }

    public Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (EntityExistsException e) {
            throw new DataIntegrityViolationException("Duplicate entry", e);
        }
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(Math.toIntExact(Long.valueOf(id)));
    }

    public Product createProduct(Product product) {
        return product;
    }

    public Product findProductById(Integer id) {
        return null;
    }

    public Product updateProduct(Product product) {
        return product;
    }

    public void deleteProduct(Integer id) {
    }

    public Collection<Product> findProductsByName(String name) {
        return List.of();
    }

    public Collection<Product> findAllProducts() {
        return List.of();
    }
}
