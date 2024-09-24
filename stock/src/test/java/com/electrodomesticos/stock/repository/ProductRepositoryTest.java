package com.electrodomesticos.stock.repository;

import com.electrodomesticos.stock.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // Ensure the database changes are rolled back after each test
@Rollback
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();  // Clean up the database before each test
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product("Phone", 500.0);
        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());  // The ID should be auto-generated
        assertEquals("Phone", savedProduct.getName());
        assertEquals(500.0, savedProduct.getPrice());
    }

    @Test
    public void testFindById() {
        Product product = new Product("Phone", 500.0);
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals("Phone", foundProduct.get().getName());
        assertEquals(500.0, foundProduct.get().getPrice());
    }

    @Test
    public void testFindAll() {
        productRepository.save(new Product("Phone", 500.0));
        productRepository.save(new Product("Laptop", 1200.0));

        List<Product> products = productRepository.findAll();

        assertEquals(2, products.size());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product("Phone", 500.0);
        Product savedProduct = productRepository.save(product);

        savedProduct.setName("Smartphone");
        savedProduct.setPrice(600.0);
        Product updatedProduct = productRepository.save(savedProduct);

        assertEquals("Smartphone", updatedProduct.getName());
        assertEquals(600.0, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteById() {
        Product product = new Product("Phone", 500.0);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testDeleteAll() {
        productRepository.save(new Product("Phone", 500.0));
        productRepository.save(new Product("Laptop", 1200.0));

        productRepository.deleteAll();

        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }
}
