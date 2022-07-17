/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.controller;

import com.kalsym.service.vertical.ServiceVerticalApplication;
import com.kalsym.service.vertical.enums.ReservationStatus;
import com.kalsym.service.vertical.model.ResourceSlotReservation;
import com.kalsym.service.vertical.model.ResourceSlotReservationDetail;
import com.kalsym.service.vertical.model.store.Store;
import com.kalsym.service.vertical.repository.ResourceSlotReservationDetailRepository;
import com.kalsym.service.vertical.repository.ResourceSlotReservationRepository;
import com.kalsym.service.vertical.repository.StoreRepository;
import com.kalsym.service.vertical.utility.HttpResponse;
import com.kalsym.service.vertical.utility.Logger;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hasan
 *
 * This Controller is used for get,post,put and delete operations of resource
 * slot reservation detail
 */
@RestController
@RequestMapping("/resources/slots/reservations")
@CrossOrigin("*")
public class ResourceSlotReservationDetailController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ResourceSlotReservationRepository resourceSlotReservationRepository;

    @Autowired
    private ResourceSlotReservationDetailRepository resourceSlotReservationDetailRepository;

    @GetMapping(path = {""}, name = "resource-slot-reservation-detail-get", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceSlotReservationDetails(HttpServletRequest request,
            @RequestParam(name = "storeId") String storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setStatus(HttpStatus.OK);
        response.setData(resourceSlotReservationDetailRepository.findAll());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = {"/find/"}, name = "resource-slot-reservation-detail-get-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceSlotReservationDetailById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceSlotReservationId", required = true) String resourceSlotReservationId) {

        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<ResourceSlotReservationDetail> optionalResourceSlotReservationDetail = resourceSlotReservationDetailRepository.findById(resourceSlotReservationId);

        if (!optionalResourceSlotReservationDetail.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND id: " + resourceSlotReservationId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setStatus(HttpStatus.OK);
        response.setData(optionalResourceSlotReservationDetail.get());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(path = {""}, name = "resource-slot-reservation-detail-post", produces = "application/json")
    public ResponseEntity<HttpResponse> postResourceSlotReservationDetail(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceSlotReservationId", required = true) String resourceSlotReservationId,
            @RequestBody ResourceSlotReservationDetail bodyReservation) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "bodyReservation: " + bodyReservation.toString());

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<ResourceSlotReservation> optionalResourceSlotReservation = resourceSlotReservationRepository.findById(resourceSlotReservationId);

        if (!optionalResourceSlotReservation.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND slotId: " + resourceSlotReservationId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        List<ResourceSlotReservationDetail> resourceSlotReservationDetails = resourceSlotReservationDetailRepository.findAll();

        bodyReservation.setResourceSlotReservation(optionalResourceSlotReservation.get());

        for (ResourceSlotReservationDetail resouceSlotReservationDetail : resourceSlotReservationDetails) {
            if (resouceSlotReservationDetail.getResourceSlotReservation().getId().equals(bodyReservation.getResourceSlotReservation().getId())) {
                Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Slot already reserved", "");
                response.setStatus(HttpStatus.CONFLICT);
                response.setData("Slot Already Reserved");
                return ResponseEntity.status(response.getStatus()).body(response);
            }
        }
        if (bodyReservation.getCustomerNotes() == null) {
            bodyReservation.setCustomerNotes("");
        }

        if (bodyReservation.getStatus() == null) {
            bodyReservation.setStatus(ReservationStatus.WAITING);
        }

        if (bodyReservation.getResourceSlotReservation().getIsReserved() == false) {
            bodyReservation.getResourceSlotReservation().setIsReserved(Boolean.TRUE);
        }

        bodyReservation.setResourceSlotReservation(optionalResourceSlotReservation.get());
        ResourceSlotReservationDetail savedReservation = resourceSlotReservationDetailRepository.save(bodyReservation);

        response.setStatus(HttpStatus.CREATED);
        response.setData(savedReservation);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PutMapping(path = {""}, name = "resource-slot-reservation-detail-put", produces = "application/json")
    public ResponseEntity<HttpResponse> putResourceSlotReservationDetail(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceSlotReservationDetailId") String resourceSlotReservationDetailId,
            @RequestBody ResourceSlotReservationDetail bodyReservation) {

        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<ResourceSlotReservationDetail> optionalResourceSlotReservationDetail = resourceSlotReservationDetailRepository.findById(resourceSlotReservationDetailId);

        if (!optionalResourceSlotReservationDetail.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "NOT_FOUND: {}", resourceSlotReservationDetailId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Slot found with Id: {}", resourceSlotReservationDetailId);
        ResourceSlotReservationDetail reservation = optionalResourceSlotReservationDetail.get();

        reservation.update(bodyReservation);

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation slot updated for Id: " + resourceSlotReservationDetailId, "");
        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(resourceSlotReservationDetailRepository.save(reservation));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping(path = {""}, name = "resource-slot-reservation-detail-delete", produces = "application/json")
    public ResponseEntity<HttpResponse> deleteResourceSlotReservationDetail(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceSlotReservationDetailId", required = true) String resourceSlotReservationDetailId) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<ResourceSlotReservationDetail> optionalResourceSlotReservationDetail = resourceSlotReservationDetailRepository.findById(resourceSlotReservationDetailId);

        if (!optionalResourceSlotReservationDetail.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND: " + resourceSlotReservationDetailId, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Resource Slot Reservation Detail found", "");
        resourceSlotReservationDetailRepository.delete(optionalResourceSlotReservationDetail.get());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Resource Slot Reservation Detail deleted, with id: {}", resourceSlotReservationDetailId);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

}
