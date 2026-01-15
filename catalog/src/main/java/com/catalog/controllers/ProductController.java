package com.catalog.controllers;

import com.catalog.dto.ProductDTO;
import com.catalog.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        logger.info("GET /products - finding all products");
        Page<ProductDTO> listDto = productService.findAll(pageable);
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        logger.info("GET /products/{} - finding a product by id", id);
        ProductDTO dto = productService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> count() {
        Long total = productService.count();
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
        logger.info("POST /products - inserting the product: {}", dto.getName());
        dto = productService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        logger.info("PUT /products - updating the product: {} with id: {}",dto.getName(), id);
        dto = productService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE /products/{} - deleting a product by id", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
