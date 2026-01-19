package com.catalog.services;

import com.catalog.repositories.ProductRepository;
import com.catalog.services.exceptions.DatabaseException;
import com.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        dependentId = 3L;

        // Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        // Mockito.doThrow(EmptyResultDataAcessExpetion.class).when(productRepository).deleteById(nonExistingId);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

    }

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDoNothingWhenIdExists() {;
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistingId);
        });

        // deleteById is NEVER called because existsById returns false first
        Mockito.verify(productRepository, Mockito.never()).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }
}
