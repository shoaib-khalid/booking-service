/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.store.Store;
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
public interface StoreRepository extends PagingAndSortingRepository<Store, String>, JpaRepository<Store, String> {

    List<Store> findByName(@Param("name") String name);
    
    List<Store> findByDomain(@Param("domain") String domain);
    
    List<Store> findByClientId(@Param("clientId") String clientId);
    
}
