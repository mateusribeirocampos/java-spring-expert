package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		Category category = CategoryFactory.createCategory();
		Product product = new Product(1L, "Console PlayStation 5", "consectetur adipiscing elit, sed", 3999.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
		product.getCategories().add(category);
		return product;
	}
	
	public static Product createProduct(String name) {
		Product product = createProduct();
		product.setName(name);
		return product;
	}

	public static ProductDTO createProductDTO() {
		ProductDTO productDTO = new ProductDTO(1L, "Mackbook air", "The MacBook Air is a line of lightweight laptops developed by Apple", 7999.0, "https://i0.wp.com/9to5mac.com/wp-content/uploads/sites/6/2025/05/M4-MacBook-Air-take-two.jpg?w=1500&quality=82&strip=all&ssl=1");
		productDTO.getCategories().add(new CategoryDTO());
		return productDTO;
	}

}
