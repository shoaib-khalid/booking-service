/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hasan
 */
@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String>, JpaRepository<Product, String> {

    List<Product> findByName(@Param("name") String name);

    List<Product> findByStoreId(@Param("storeId") String storeId);

    List<Product> findByStoreIdAndName(@Param("storeId") String storeId, @Param("name") String name);

    List<Product> findByCategoryId(@Param("categoryId") String categoryId);

}
