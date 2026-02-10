package com.devsuperior.examplemockspy.services;

import com.devsuperior.examplemockspy.dto.ProductDTO;
import com.devsuperior.examplemockspy.entities.Product;
import com.devsuperior.examplemockspy.repositories.ProductRepository;
import com.devsuperior.examplemockspy.services.exceptions.InvalidDataException;
import com.devsuperior.examplemockspy.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private ProductDTO productDTO;
    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception {

        product = new Product(1L, "PlayStation", 5000.0);
        productDTO = new ProductDTO(product);
        existingId = 1L;
        nonExistingId = 10000L;

        Mockito.when(productRepository.save(any())).thenReturn(product);

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    public void insertShouldReturnProductDTOWhenValidDate() {
        ProductService serviceSpy = Mockito.spy(productService);

        Mockito.doNothing().when(serviceSpy).validateData(productDTO);

        ProductDTO result = serviceSpy.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), "PlayStation");
    }

    @Test
    public void insertShouldReturnInvalidDataExceptionWhenProductNameIsBlank() {
        productDTO.setName("");

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.insert(productDTO);
        });
    }

    @Test
    public void insertShouldReturnInvalidDataExceptionWhenProductPriceNegativeOrZero() {
        productDTO.setPrice(-5.0);

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.insert(productDTO);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExistsAndValidData() {
        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doNothing().when(serviceSpy).validateData(productDTO);

        ProductDTO result = serviceSpy.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);

    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductNameIsBlank() {
        productDTO.setName("");

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(existingId, productDTO);
        });
    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductPriceIsNegativeOrZero() {
        productDTO.setPrice(-5.0);

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(existingId, productDTO);
        });
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExistsAndValidData() {
        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doNothing().when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, productDTO);
        });
    }
    @Test
    public void updateShouldInvalidExceptionWhenIdDoesNotExistsAndProductIsBlank() {
        productDTO.setName("");

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, productDTO);
        });
    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdDoesNotExistsAndPriceIsNegativeOrZero() {
        productDTO.setPrice(-5.0);

        ProductService serviceSpy = Mockito.spy(productService);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, productDTO);
        });
    }
}
