package com.catalog.tests;

import com.catalog.dto.ProductDTO;
import com.catalog.entities.Category;
import com.catalog.entities.Product;
import jakarta.persistence.Column;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "PC Gamer WW", Instant.parse("2026-01-18T18:03:00Z") , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 1350.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/26-big.jpg");
        product.getCategories().add(new Category(2L, "Electronics", Instant.now(), Instant.now()));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
