/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.controller;

import com.kalsym.service.vertical.ServiceVerticalApplication;
import com.kalsym.service.vertical.model.Resource;
import com.kalsym.service.vertical.model.ResourceAvailability;
import com.kalsym.service.vertical.model.store.Store;
import com.kalsym.service.vertical.repository.ResourceAvailabilityRepository;
import com.kalsym.service.vertical.repository.ResourceRepository;
import com.kalsym.service.vertical.repository.StoreRepository;
import com.kalsym.service.vertical.utility.HttpResponse;
import com.kalsym.service.vertical.utility.Logger;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 */
/**
 * 
 * This controller is used for get,post,put and delete operations of resource availabilities
 */
@RestController
@RequestMapping("/resources/availabilities/")
public class ResourceAvailabilityController {

    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping(path = {""}, name = "resource-availabilities-get", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceAvailabilities(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId) {

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

        List<ResourceAvailability> resourceAvailabilites = resourceAvailabilityRepository.findByStoreId(storeId);

        response.setStatus(HttpStatus.OK);
        response.setData(resourceAvailabilites);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping(path = {"/find/"}, name = "resource-availability-get-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceAvailabilityById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceAvailabiltyId", required = true) String resourceAvailabilityId) {

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

        Optional<ResourceAvailability> optionalResourceAvailability = resourceAvailabilityRepository.findById(resourceAvailabilityId);

        if (!optionalResourceAvailability.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND resourceAvailabilityId: " + resourceAvailabilityId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setStatus(HttpStatus.FOUND);
        response.setData(optionalResourceAvailability.get());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(path = {""}, name = "resource-availability-post", produces = "application/json")
    public ResponseEntity<HttpResponse> postResourceAvailability(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceId", required = true) String resourceId,
            @RequestBody ResourceAvailability bodyResourceAvailability) {

        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "bodyReservationResource: " + bodyResourceAvailability.toString());

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " FOUND storeId: " + storeId);

        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);

        if (!optionalResource.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "NOT_FOUND: {}", resourceId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        bodyResourceAvailability.setResource(optionalResource.get());

        List<ResourceAvailability> resourcesAvailabilties = resourceAvailabilityRepository.findByResourceId(resourceId);

        //If end time is before start time send not acceptable
        if (bodyResourceAvailability.getStartTime().isAfter(bodyResourceAvailability.getEndTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "End Time can not be before start time", "");
            response.setStatus(HttpStatus.NOT_ACCEPTABLE);
            response.setData("End Time can not be before start time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        //Check if the resource being created does not overlap with any other availabilities
        for (ResourceAvailability resourceAvailability : resourcesAvailabilties) {

            if (bodyResourceAvailability.getAvailabilityDay().equals(resourceAvailability.getAvailabilityDay())) {
                if (!((bodyResourceAvailability.getStartTime().isAfter(resourceAvailability.getEndTime()) || bodyResourceAvailability.getStartTime().equals(resourceAvailability.getEndTime()))
                        || (bodyResourceAvailability.getEndTime().isBefore(resourceAvailability.getStartTime()) || bodyResourceAvailability.getEndTime().equals(resourceAvailability.getStartTime())))) {
                    Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "resource duration overlapping", "");
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setData("Resource duration overlapping");
                    return ResponseEntity.status(response.getStatus()).body(response);
                }
            }
        }

        if (bodyResourceAvailability.getMinimumTimeBetweenReservation() == null) {
            bodyResourceAvailability.setMinimumTimeBetweenReservation(0);
        }

        if (bodyResourceAvailability.getDescription() == null) {
            bodyResourceAvailability.setDescription("");
        }

        if (bodyResourceAvailability.getResource().getNumberOfWeeksReservable() == 0) {
            bodyResourceAvailability.getResource().setNumberOfWeeksReservable(1);
        }

        bodyResourceAvailability.setStore(optionalStore.get());
        ResourceAvailability savedReservationResource = resourceAvailabilityRepository.save(bodyResourceAvailability);

        response.setStatus(HttpStatus.CREATED);
        response.setData(savedReservationResource);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(path = {""}, name = "resource-availability-put", produces = "application/json")
    public ResponseEntity<HttpResponse> putResourceAvailabilityById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceAvailabilityId") String resourceAvailabilityId,
            @RequestBody ResourceAvailability bodyResourceAvailability) {

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

        Optional<ResourceAvailability> optionalResourceAvailability = resourceAvailabilityRepository.findById(resourceAvailabilityId);

        if (!optionalResourceAvailability.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "NOT_FOUND: {}", resourceAvailabilityId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        //If end time is before start time send not acceptable
        if (bodyResourceAvailability.getStartTime().isAfter(bodyResourceAvailability.getEndTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "End Time can not be before start time", "");
            response.setStatus(HttpStatus.NOT_ACCEPTABLE);
            response.setData("End Time can not be before start time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        ResourceAvailability resourceAvailability = optionalResourceAvailability.get();

        List<ResourceAvailability> resourceAvailabilties = resourceAvailabilityRepository.findByResourceId(resourceAvailability.getResource().getId());

        //Check if the resource being created does not overlap with any other availabilities
        for (ResourceAvailability currentResourceAvailability : resourceAvailabilties) {

            if (bodyResourceAvailability.getAvailabilityDay() == null) {
                if (!resourceAvailability.getId().equals(currentResourceAvailability.getId())) {
                    if (resourceAvailability.getAvailabilityDay().equals(currentResourceAvailability.getAvailabilityDay())) {
                        if (!((bodyResourceAvailability.getStartTime().isAfter(currentResourceAvailability.getEndTime()) || bodyResourceAvailability.getStartTime().equals(currentResourceAvailability.getEndTime()))
                                || (bodyResourceAvailability.getEndTime().isBefore(currentResourceAvailability.getStartTime()) || bodyResourceAvailability.getEndTime().equals(currentResourceAvailability.getStartTime())))) {
                            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "resource duration overlapping", "");
                            response.setStatus(HttpStatus.CONFLICT);
                            response.setData("Resource duration overlapping");
                            return ResponseEntity.status(response.getStatus()).body(response);
                        }
                    }
                }

            } else {
                if (!resourceAvailability.getId().equals(currentResourceAvailability.getId())) {
                    if (bodyResourceAvailability.getAvailabilityDay().equals(currentResourceAvailability.getAvailabilityDay())) {
                        if (!((bodyResourceAvailability.getStartTime().isAfter(currentResourceAvailability.getEndTime()) || bodyResourceAvailability.getStartTime().equals(currentResourceAvailability.getEndTime()))
                                || (bodyResourceAvailability.getEndTime().isBefore(currentResourceAvailability.getStartTime()) || bodyResourceAvailability.getEndTime().equals(currentResourceAvailability.getStartTime())))) {
                            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "resource duration overlapping", "");
                            response.setStatus(HttpStatus.CONFLICT);
                            response.setData("Resource duration overlapping");
                            return ResponseEntity.status(response.getStatus()).body(response);
                        }
                    }
                }
            }
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Resource found with Id: {}", resourceAvailabilityId);
        ResourceAvailability reservationResource = optionalResourceAvailability.get();

        reservationResource.update(bodyResourceAvailability);

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Resource updated for Id: " + resourceAvailabilityId, "");
        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(resourceAvailabilityRepository.save(reservationResource));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping(path = {""}, name = "resource-availability-delete-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> deleteReservationResourceById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceAvailabilityId", required = true) String resourceAvailabilityId) {
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

        Optional<ResourceAvailability> optionalResourceAvailability = resourceAvailabilityRepository.findById(resourceAvailabilityId);

        if (!optionalResourceAvailability.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND: " + resourceAvailabilityId, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Resource found", "");
        resourceAvailabilityRepository.delete(optionalResourceAvailability.get());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "product deleted, with id: {}", resourceAvailabilityId);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
