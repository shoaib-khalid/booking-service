/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hasan
 */
public interface ResourceRepository extends JpaRepository<Resource, String> {
    //List<ResourceAvailability> findByProductId(String productId);
}
