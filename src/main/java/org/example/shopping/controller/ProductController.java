package org.example.shopping.controller;

import org.example.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.shopping.service.ProductService;
import java.util.Collection;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.findProductById(id);
    }

    @GetMapping("/name/{name}")
    public Collection<Product> getProductsByName(@PathVariable String name) {
        return productService.findProductsByName(name);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @GetMapping
    public Collection<Product> getAllProducts() {
        return productService.findAllProducts();
    }
}
