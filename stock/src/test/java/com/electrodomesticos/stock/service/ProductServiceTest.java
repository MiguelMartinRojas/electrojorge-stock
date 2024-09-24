package com.electrodomesticos.stock.service;

import com.electrodomesticos.stock.entity.Product;
import com.electrodomesticos.stock.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product("Phone", 500.0);
        when(productRepository.save(any(Product.class))).thenReturn(product); // Mock save method

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct); // Assert that the created product is not null
        assertEquals("Phone", createdProduct.getName());
        assertEquals(500.0, createdProduct.getPrice());
        verify(productRepository, times(1)).save(product); // Verify that save was called once
    }

    @Test
    public void testGetProductById() {
        Product product = new Product("Phone", 500.0);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product)); // Mock findById method

        Optional<Product> foundProduct = productService.getProductById(1L);

        assertTrue(foundProduct.isPresent()); // Assert that the product is present
        assertEquals("Phone", foundProduct.get().getName());
        assertEquals(500.0, foundProduct.get().getPrice());
        verify(productRepository, times(1)).findById(1L); // Verify that findById was called once
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product("Phone", 500.0);
        Product product2 = new Product("Laptop", 1200.0);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2)); // Mock findAll method

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size()); // Assert that the size of the product list is correct
        assertEquals("Phone", products.get(0).getName());
        assertEquals("Laptop", products.get(1).getName());
        verify(productRepository, times(1)).findAll(); // Verify that findAll was called once
    }

    @Test
    public void testUpdateProduct() {
        Product existingProduct = new Product("Phone", 500.0);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(existingProduct)); // Mock findById
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct); // Mock save

        Product updatedProduct = new Product("Smartphone", 600.0);
        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Smartphone", result.getName()); // Assert that the name has been updated
        assertEquals(600.0, result.getPrice()); // Assert that the price has been updated
        verify(productRepository, times(1)).findById(1L); // Verify that findById was called once
        verify(productRepository, times(1)).save(existingProduct); // Verify that save was called once
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product("Phone", 500.0);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product)); // Mock findById

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L); // Verify that findById was called once
        verify(productRepository, times(1)).deleteById(1L); // Verify that deleteById was called once
    }
}
