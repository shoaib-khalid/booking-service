/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kalsym.service.vertical.repository;

import com.kalsym.service.vertical.model.ResourceSlotReservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hasan
 */
@Repository
public interface ResourceSlotReservationRepository extends JpaRepository<ResourceSlotReservation, String> {
    List<ResourceSlotReservation> findByIsReserved(Boolean isReserved);
    List<ResourceSlotReservation> findByResourceAvailabilityId(String resourceAvailabilityId);
    //List<ReservationSlot> findByStoreId(String storeId);
}
