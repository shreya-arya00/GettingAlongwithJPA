package org.example.shopping;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.shopping.entity.Product;
import org.example.shopping.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testSaveProductWithoutId() {
        Product product = new Product("Product 1", 100.0);
        product = productRepository.save(product);
        Assertions.assertNotNull(product.getId());

        Product entityManagerProduct = new Product("Product 2", 200.0);
        entityManager.persist(entityManagerProduct);
        entityManager.flush();
        Assertions.assertNotNull(entityManagerProduct.getId());

        Product mergeProduct = new Product("Product 3", 300.0);
        mergeProduct = entityManager.merge(mergeProduct);
        entityManager.flush();
        Assertions.assertNotNull(mergeProduct.getId());
    }

    @Test
    @Transactional
    void testSaveProductWithId() {
        // Create a product without setting ID
        Product product1 = new Product("Product 1", 100.0);

        // Save the product
        product1 = productRepository.save(product1);

        // Verify the ID after saving
        Assertions.assertNotNull(product1.getId());

        entityManager.clear(); // Clear the persistence context to simulate retrieving from the database

        // Create another product with the same ID (assuming it's auto-generated)
        Product product2 = new Product("Product 2", 200.0);
        product2.setId(product1.getId()); // Set the ID of the new product to the ID of the previously saved product

        // Merge the product (simulates saving a detached entity with the same ID)
        product2 = entityManager.merge(product2);
        entityManager.flush(); // Flush changes to the database

        // Retrieve the product from the repository
        Product updatedProduct = productRepository.findById(product2.getId()).orElse(null);

        // Assertions
        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals(product1.getId(), updatedProduct.getId()); // Ensure IDs match
    }

    @Test
    void testSaveProductWithDuplicateId() {
        Product product1 = new Product("Product 1", 100.0);
        entityManager.persist(product1);
        entityManager.flush();

        entityManager.clear();

        Product product2 = new Product("Product 2", 200.0);
        product2.setId(product1.getId());

        Assertions.assertThrows(EntityExistsException.class, () -> {
            entityManager.persist(product2);
            entityManager.flush();
        });
    }

    @Test
    void testFetchProductAndChangeWithoutSave() {
        Product product = new Product("Product 1", 100.0);
        product = productRepository.save(product);

        product.setName("Updated Product 1");

        entityManager.flush();
        entityManager.clear();

        Product fetchedProduct = productRepository.findById(product.getId()).get();
        Assertions.assertEquals("Updated Product 1", fetchedProduct.getName());
    }

    @Test
    @Transactional
    void testFetchProductInTransactionAndChangeWithoutSave() {
        Product product = new Product("Product 1", 100.0);
        product = productRepository.save(product);

        product.setName("Updated Product 1");

        entityManager.flush();
        entityManager.clear();

        Product fetchedProduct = productRepository.findById(product.getId()).get();
        Assertions.assertEquals("Updated Product 1", fetchedProduct.getName());
    }
}
