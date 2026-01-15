package com.catalog.services;

import com.catalog.dto.CategoryDTO;
import com.catalog.dto.ProductDTO;
import com.catalog.entities.Category;
import com.catalog.entities.Product;
import com.catalog.repositories.CategoryRepository;
import com.catalog.repositories.ProductRepository;
import com.catalog.services.exceptions.DatabaseException;
import com.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        logger.info("Finding all products");
        Page<Product> entityList = productRepository.findAll(pageable);
        return entityList.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        logger.info("Finding a product by id: {}", id);
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional(readOnly = true)
    public Long count() {
        return productRepository.count();
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        logger.info("Inserting a product: {}", dto.getName());
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        logger.info("Updating a product: {} by id: {}", dto.getName(), id);
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        logger.info("Deleting a product by id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }

}
