package com.catalog.dto;

import com.catalog.entities.Category;
import com.catalog.entities.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;
    private String name;
    private Instant date;
    private String description;
    private Double price;
    private String imgUrl;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Instant date, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }
    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        date = entity.getDate();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
