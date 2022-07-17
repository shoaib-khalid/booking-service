/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.ResourceSlotReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hasan
 */
@Repository
public interface ResourceSlotReservationDetailRepository extends PagingAndSortingRepository<ResourceSlotReservationDetail, String>, JpaRepository<ResourceSlotReservationDetail, String> {

}
