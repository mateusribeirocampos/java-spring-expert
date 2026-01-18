package com.catalog.repositories;

import com.catalog.entities.Product;
import com.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDoNothingWhenIdDoesNotExist() {

        Assertions.assertDoesNotThrow(() -> {
           productRepository.deleteById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnNotEmptyWhenIdExists() {
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<Product> result = productRepository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());

    }
}
