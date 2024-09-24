package com.electrodomesticos.stock.controller;

import com.electrodomesticos.stock.entity.Product;
import com.electrodomesticos.stock.repository.ProductRepository;
import com.electrodomesticos.stock.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional  // Ensure that database changes are rolled back after each test
@Rollback
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();  // Clean up the database before each test
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product("Phone", 500.0);
        String productJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.price").value(500.0));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        productService.createProduct(new Product("Phone", 500.0));
        productService.createProduct(new Product("Laptop", 1200.0));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Phone"))
                .andExpect(jsonPath("$[1].name").value("Laptop"));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = productService.createProduct(new Product("Phone", 500.0));

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.price").value(500.0));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = productService.createProduct(new Product("Phone", 500.0));
        Product updatedProduct = new Product("Smartphone", 600.0);
        String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(put("/api/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(600.0));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = productService.createProduct(new Product("Phone", 500.0));

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isNotFound());
    }
}
