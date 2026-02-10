package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long existingId, nonExistingId;
    private long existingProductId, nonExistingProductId;
    private String productName;
    private Product product;
    private PageImpl<Product> page;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingProductId = 1L;
        nonExistingProductId = 2L;

        existingId = 1L;
        nonExistingId = 1000L;

        productName = "PlayStation 5";

        product = ProductFactory.createProduct(productName);
        productDTO = new ProductDTO(product);

        page = new PageImpl<>(List.of(product));

        // findById
        Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        // findAll
        Mockito.when(productRepository.searchByName(any(), any(Pageable.class))).thenReturn(page);

        // insert
        Mockito.when(productRepository.save(any())).thenReturn(product);

        // update
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        // delete
        Mockito.doNothing().when(productRepository.existsById(existingId));
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.findById(existingProductId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistingProductId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Pageable pageable = PageRequest.of(0,12);

        Page<ProductMinDTO> result = productService.findAll(productName, pageable);
        Assertions.assertNotNull(result);
        //Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(1L, result.getTotalElements());
        Assertions.assertEquals(result.iterator().next().getName(), productName);
    }

    @Test
    public void insertShouldReturnProductDTOWhenValidData() {
        ProductDTO result = productService.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getName(), product.getName());
        Assertions.assertEquals(result.getPrice(), product.getPrice());
        Assertions.assertEquals(result.getDescription(), product.getDescription());
        Assertions.assertEquals(result.getImgUrl(), product.getImgUrl());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdIsValid() {
        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getName(), product.getName());
        Assertions.assertEquals(result.getPrice(), product.getPrice());
        Assertions.assertEquals(result.getDescription(), product.getDescription());
        Assertions.assertEquals(result.getImgUrl(), product.getImgUrl());
    }

    @Test
    public void updateShouldReturnResourceNotFoundWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistingId, productDTO);
        });
    }
}
