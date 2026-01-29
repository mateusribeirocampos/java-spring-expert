package com.catalog.services;

import com.catalog.dto.ProductDTO;
import com.catalog.entities.Category;
import com.catalog.entities.Product;
import com.catalog.repositories.CategoryRepository;
import com.catalog.repositories.ProductRepository;
import com.catalog.services.exceptions.DatabaseException;
import com.catalog.services.exceptions.ResourceNotFoundException;
import com.catalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();

        // finAll test
        Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        // save test
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        // findById to product tests
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // update
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        // Update tests - getReferenceById para Category
        Mockito.when(categoryRepository.getReferenceById(2L)).thenReturn(category);


        // Mockito.doNothing().when(productRepository).deleteById(existingId); - Legado
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        // Mockito.doThrow(EmptyResultDataAcessExpetion.class).when(productRepository).deleteById(nonExistingId); - legado
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

    }

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

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

    @Test
    public void findAllPageShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAll(pageable);

        Assertions.assertNotNull(result) ;
        Mockito.verify(productRepository).findAll(pageable);

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
    // Act - call the service and capture the result
    ProductDTO result = productService.findById(existingId);

    // Assert - verify the result is correct
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
    // Verify the repository was called
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.findById(nonExistingId);
        });

        Mockito.verify(productRepository).findById(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);

        Mockito.verify(productRepository).getReferenceById(existingId);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () ->  {
           productService.update(nonExistingId, productDTO);
        });
        Mockito.verify(productRepository, Mockito.never()).save(ArgumentMatchers.any());
    }
}
