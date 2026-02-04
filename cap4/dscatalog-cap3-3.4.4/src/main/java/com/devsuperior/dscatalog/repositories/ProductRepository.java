package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Query(value = " SELECT obj FROM Product obj JOIN FETCH obj.categories",
//        countQuery = "SELECT COUNT(obj) FROM Product obj"
//    )
//    Page<Product> findAllProductsWithCategories(Pageable pageable);
//
//    @Query(value = "SELECT obj " +
//            "FROM Product obj " +
//            "JOIN FETCH obj.categories " +
//            "WHERE obj.id = :id")
//    Optional<Product> findByIdProductWithCategories(Long id);

    @Query(nativeQuery = true, value = """
        SELECT * FROM (
        SELECT DISTINCT tb_product.id, tb_product.name
        FROM tb_product
        INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
        WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN (:categoryIds))
        AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))
        ) AS tb_result
    """, countQuery = """
         SELECT COUNT(*) FROM (
         SELECT DISTINCT tb_product.id, tb_product.name
         FROM tb_product
         INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
         WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN (:categoryIds))
         AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))
         ) AS tb_result
    """)
    Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories "
           + "WHERE obj.id IN :productIds")
    List<Product> searchProductsWithCategories(List<Long> productIds);

    @Query("SELECT obj FROM  Product obj JOIN FETCH obj.categories WHERE obj.id =:id")
    Optional<Product> findByIdWithCategories(Long id);


}
