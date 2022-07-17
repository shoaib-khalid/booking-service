/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.ResourceAvailability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hasan
 */
@Repository
public interface ResourceAvailabilityRepository extends PagingAndSortingRepository<ResourceAvailability, String>, JpaRepository<ResourceAvailability, String> {

    List<ResourceAvailability> findByResourceId(String resourceId);

    List<ResourceAvailability> findByStoreId(String storeId);
}
