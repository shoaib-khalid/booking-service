/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.controller;

import com.kalsym.service.vertical.ServiceVerticalApplication;
import com.kalsym.service.vertical.model.AvailableSlot;
import com.kalsym.service.vertical.model.ResourceAvailability;
import com.kalsym.service.vertical.model.ResourceSlotReservation;
import com.kalsym.service.vertical.model.store.Store;
import com.kalsym.service.vertical.repository.ResourceAvailabilityRepository;
import com.kalsym.service.vertical.repository.ResourceSlotReservationRepository;
import com.kalsym.service.vertical.repository.StoreRepository;
import com.kalsym.service.vertical.utility.HttpResponse;
import com.kalsym.service.vertical.utility.Logger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.ArrayList;
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
 *
 * This Controller is used for get,post,put and delete operations of resource
 * slot reservations
 */
@RestController
@RequestMapping("/resources/slots")
public class ResourceSlotReservationController {

    @Autowired
    private ResourceSlotReservationRepository resourceSlotReservationRepository;

    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping(path = {""}, name = "resource-slot-reservation-get-all", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceSlotReservations(HttpServletRequest request,
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

        response.setStatus(HttpStatus.OK);
        response.setData(resourceSlotReservationRepository.findAll());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = {"/find/"}, name = "resource-slot-reservation-get-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourceSlotReservationById(HttpServletRequest request,
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

        Optional<ResourceSlotReservation> optionalResourceSlotReservation = resourceSlotReservationRepository.findById(resourceSlotReservationId);

        if (!optionalResourceSlotReservation.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND id: " + resourceSlotReservationId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response.setStatus(HttpStatus.OK);
        response.setData(optionalResourceSlotReservation.get());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = {"/reserved"}, name = "resource-slot-reservation-reserved-get", produces = "application/json")
    public ResponseEntity<HttpResponse> getReservedSlots(HttpServletRequest request,
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

        List<ResourceSlotReservation> resourceSlotReservations = resourceSlotReservationRepository.findByIsReserved(Boolean.TRUE);

        if (resourceSlotReservations.isEmpty()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "Not Found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setStatus(HttpStatus.OK);
        response.setData(resourceSlotReservations);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping(path = {"/unreserved/"}, name = "resource-slot-reservation-unreserved-slots-get", produces = "application/json")
    public ResponseEntity<HttpResponse> getUnreservedSlots(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceId", required = true) String resourceId) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Create Slots", "");

        // 1. duration: Time of the resource's availability
        // 2. slot: Valid Time duration falling within the resource's availalibilty duration
        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        List<AvailableSlot> availableSlots = new ArrayList<>();
        List<ResourceAvailability> resourceAvailabilites = resourceAvailabilityRepository.findByResourceId(resourceId);

        if (resourceAvailabilites.isEmpty()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND: " + resourceId, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        for (ResourceAvailability resourceAvailability : resourceAvailabilites) {
            LocalTime startTime = resourceAvailability.getStartTime();
            LocalTime endTime = resourceAvailability.getEndTime();
            // ToDo: Need to conform the date in a stardard UTC 
            LocalDate date = LocalDate.now();

            int numberOfWeeks = resourceAvailability.getResource().getNumberOfWeeksReservable();

            // The endDate of the duration for which the slots need to be created
            LocalDate durationEndDate = LocalDate.now().plusWeeks(numberOfWeeks);

            // The duration of allowed slot of the resource
            long slotDuration = resourceAvailability.getDurationInMinutes();
            long durationInMs = slotDuration * 60000;

            while (!date.equals(durationEndDate)) {
                LocalTime availabilityStartTime = startTime;
                LocalTime availabilityEndTime = endTime;

                long availabilityEndTimeMS = convertToMillisecondsEpoch(date, availabilityEndTime);

                String dayAtDate = date.getDayOfWeek().toString();
                String availableDay = resourceAvailability.getAvailabilityDay().toString();

                // The end time of the slot to be created
                LocalTime slotEndTime = availabilityStartTime.plusMinutes(slotDuration);

                long slotStartTimeMS = convertToMillisecondsEpoch(date, availabilityStartTime);
                long slotEndTimeMS = convertToMillisecondsEpoch(date, slotEndTime);

                while (slotEndTimeMS <= availabilityEndTimeMS) {
                    // <editor-fold defaultstate="collapsed" desc="Slots Generation for a day">
                    if (!isReserved(resourceAvailability.getId(), slotStartTimeMS, slotEndTimeMS)
                            && !isDayOff(resourceAvailability.getId(), date)) {
                        // Check if the resource's available day matches the current day, so that the slots could be created from today.
                        if (dayAtDate.equals(availableDay)) {
                            AvailableSlot availableSlot = new AvailableSlot();
                            availableSlot.setStartTime(slotStartTimeMS);
                            availableSlot.setEndTime(slotEndTimeMS);
                            availableSlot.setDay(date.getDayOfWeek().toString().toUpperCase());
                            availableSlots.add(availableSlot);//TODO change the string comparison to enum comparison
                        } else if ("MONDAY_FRIDAY".equals(availableDay)) {

                            if (!"SATURDAY".equals(dayAtDate) && !"SUNDAY".equals(dayAtDate)) {
                                AvailableSlot availableSlot = new AvailableSlot();
                                availableSlot.setStartTime(slotStartTimeMS);
                                availableSlot.setEndTime(slotEndTimeMS);
                                availableSlot.setDay(date.getDayOfWeek().toString().toUpperCase());
                                availableSlots.add(availableSlot);
                            }
                        } else if ("FRIDAY_SUNDAY".equals(availableDay)) {

                            if ("FRIDAY".equals(dayAtDate) || "SATURDAY".equals(dayAtDate) || "SUNDAY".equals(dayAtDate)) {
                                AvailableSlot availableSlot = new AvailableSlot();
                                availableSlot.setStartTime(slotStartTimeMS);
                                availableSlot.setEndTime(slotEndTimeMS);
                                availableSlot.setDay(date.getDayOfWeek().toString().toUpperCase());
                                availableSlots.add(availableSlot);
                            }

                        } else if ("SATURDAY_SUNDAY".equals(availableDay)) {

                            if ("SATURDAY".equals(dayAtDate) || "SUNDAY".equals(dayAtDate)) {
                                AvailableSlot availableSlot = new AvailableSlot();
                                availableSlot.setStartTime(slotStartTimeMS);
                                availableSlot.setEndTime(slotEndTimeMS);
                                availableSlot.setDay(date.getDayOfWeek().toString().toUpperCase());
                                availableSlots.add(availableSlot);
                            }
                        } else if ("EVERYDAY".equals(availableDay)) {
                            AvailableSlot availableSlot = new AvailableSlot();
                            availableSlot.setStartTime(slotStartTimeMS);
                            availableSlot.setEndTime(slotEndTimeMS);
                            availableSlot.setDay(date.getDayOfWeek().toString().toUpperCase());
                            availableSlots.add(availableSlot);
                        }
                    }
                    // </editor-fold>

                    slotStartTimeMS = slotEndTimeMS;
                    slotEndTimeMS = slotStartTimeMS + durationInMs;
                }
                date = date.plusDays(1);
            }

        }

        response.setStatus(HttpStatus.OK);
        response.setData(availableSlots);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping(path = {""}, name = "resource-slot-reservation-post", produces = "application/json")
    public ResponseEntity<HttpResponse> postResourceSlotReservation(HttpServletRequest request,
            @RequestParam(name = "storeId") String storeId,
            @RequestParam(name = "resourceAvailabilityId") String resourceAvailabilityId,
            @RequestBody ResourceSlotReservation bodyReservationSlot) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "bodyReservationSlot: " + bodyReservationSlot.toString());

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

        bodyReservationSlot.setDurationInMinutes(optionalResourceAvailability.get().getDurationInMinutes());

        if (MINUTES.between(bodyReservationSlot.getStartTimeInHours(), bodyReservationSlot.getEndTimeInHours()) != bodyReservationSlot.getDurationInMinutes()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid starting and ending time according to duration in minutes", "");
            response.setStatus(HttpStatus.CONFLICT);
            response.setData("Invalid starting and ending time according to duration in minutes");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        if (bodyReservationSlot.getStartTimeInHours().isBefore(optionalResourceAvailability.get().getStartTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid slot starting time according to resource available starting time", "");
            response.setStatus(HttpStatus.CONFLICT);
            response.setData("Invalid slot starting time according to resource available starting time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        if (bodyReservationSlot.getEndTimeInHours().isAfter(optionalResourceAvailability.get().getEndTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid slot ending time according to resource available ending time", "");
            response.setStatus(HttpStatus.CONFLICT);
            response.setData("Invalid slot ending time according to resource available ending time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        if (bodyReservationSlot.getIsReserved() == null) {
            bodyReservationSlot.setIsReserved(Boolean.TRUE);
        }

        bodyReservationSlot.setStartTimeInMillisecondsUTC(convertToMillisecondsEpoch(bodyReservationSlot.getDate(), bodyReservationSlot.getStartTimeInHours()));

        bodyReservationSlot.setEndTimeInMillisecondsUTC(convertToMillisecondsEpoch(bodyReservationSlot.getDate(), bodyReservationSlot.getEndTimeInHours()));

        List<ResourceSlotReservation> reservationSlots = resourceSlotReservationRepository.findAll();

        for (ResourceSlotReservation existingReservationSlot : reservationSlots) {
            if (existingReservationSlot.getDate().equals(bodyReservationSlot.getDate())
                    && existingReservationSlot.getStartTimeInMillisecondsUTC() == bodyReservationSlot.getStartTimeInMillisecondsUTC()
                    && existingReservationSlot.getEndTimeInMillisecondsUTC() == bodyReservationSlot.getEndTimeInMillisecondsUTC()
                    && existingReservationSlot.getIsReserved() == true) {
                Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Slot already exists", "");
                response.setStatus(HttpStatus.CONFLICT);
                response.setData("Slot Already Exists");
                return ResponseEntity.status(response.getStatus()).body(response);
            }
        }

        bodyReservationSlot.setResourceAvailability(optionalResourceAvailability.get());
        ResourceSlotReservation savedReservationSlot = resourceSlotReservationRepository.save(bodyReservationSlot);

        response.setStatus(HttpStatus.CREATED);
        response.setData(savedReservationSlot);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(path = {"/offday"}, name = "resource-slot-reservation-post", produces = "application/json")
    public ResponseEntity<HttpResponse> postDayOff(HttpServletRequest request,
            @RequestParam(name = "storeId") String storeId,
            @RequestParam(name = "resourceAvailabilityId") String resourceAvailabilityId,
            @RequestParam(name = "date") String dateString) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        LocalDate date = LocalDate.parse(dateString);
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
        List<ResourceSlotReservation> resourceSlotReservations = resourceSlotReservationRepository.findAll();

        for (ResourceSlotReservation existingResourceSlotReservation : resourceSlotReservations) {
            if (existingResourceSlotReservation.getResourceAvailability().getId().equals(optionalResourceAvailability.get().getId())
                    && existingResourceSlotReservation.getDate().equals(date)
                    && existingResourceSlotReservation.getStartTimeInHours().equals(LocalTime.parse("00:00"))
                    && existingResourceSlotReservation.getEndTimeInHours().equals(LocalTime.parse("23:59"))) {
                Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Day is already Off");
                response.setMessage("The Day is already Off");
                response.setStatus(HttpStatus.CONFLICT);
                return ResponseEntity.status(response.getStatus()).body(response);
            }
        }
        ResourceSlotReservation resourceSlotReservation = new ResourceSlotReservation();
        resourceSlotReservation.setStartTimeInMillisecondsUTC(convertToMillisecondsEpoch(date, LocalTime.parse("00:00")));
        resourceSlotReservation.setEndTimeInMillisecondsUTC(convertToMillisecondsEpoch(date, LocalTime.parse("23:59")));
        resourceSlotReservation.setDate(date);
        resourceSlotReservation.setStartTimeInHours(LocalTime.parse("00:00"));
        resourceSlotReservation.setEndTimeInHours(LocalTime.parse("23:59"));
        resourceSlotReservation.setIsReserved(Boolean.TRUE);
        resourceSlotReservation.setDurationInMinutes(1440);
        resourceSlotReservation.setResourceAvailability(optionalResourceAvailability.get());

        response.setStatus(HttpStatus.CREATED);
        response.setData(resourceSlotReservationRepository.save(resourceSlotReservation));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(path = {""}, name = "resource-slot-reservation-put", produces = "application/json")
    public ResponseEntity<HttpResponse> putResourceSlotReservation(HttpServletRequest request,
            @RequestParam(name = "storeId") String storeId,
            @RequestParam(name = "resourceSlotReservationId") String resourceSlotReservationId,
            @RequestBody ResourceSlotReservation bodyReservationSlot) {

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

        Optional<ResourceSlotReservation> optionalResourceSlotReservation = resourceSlotReservationRepository.findById(resourceSlotReservationId);

        if (!optionalResourceSlotReservation.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "NOT_FOUND: {}", resourceSlotReservationId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Slot found with Id: {}", resourceSlotReservationId);
        ResourceSlotReservation reservationSlot = optionalResourceSlotReservation.get();

        Optional<ResourceAvailability> optionalResourceAvailability = resourceAvailabilityRepository.findById(reservationSlot.getResourceAvailability().getId());

        if (bodyReservationSlot.getStartTimeInHours().isBefore(optionalResourceAvailability.get().getStartTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid slot starting time according to resource available starting time", "");
            response.setStatus(HttpStatus.CONFLICT);
            response.setData("Invalid slot starting time according to resource available starting time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        if (bodyReservationSlot.getEndTimeInHours().isAfter(optionalResourceAvailability.get().getEndTime())) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid slot ending time according to resource available ending time", "");
            response.setStatus(HttpStatus.CONFLICT);
            response.setData("Invalid slot ending time according to resource available ending time");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        if (bodyReservationSlot.getStartTimeInHours() != null) {

            if (MINUTES.between(bodyReservationSlot.getStartTimeInHours(), bodyReservationSlot.getEndTimeInHours()) != reservationSlot.getDurationInMinutes()) {
                Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid starting and ending time according to duration in minutes", "");
                response.setStatus(HttpStatus.CONFLICT);
                response.setData("Invalid starting and ending time according to duration in minutes");
                return ResponseEntity.status(response.getStatus()).body(response);
            }
            if (bodyReservationSlot.getDate() == null) {

                bodyReservationSlot.setStartTimeInMillisecondsUTC(convertToMillisecondsEpoch(reservationSlot.getDate(), bodyReservationSlot.getStartTimeInHours()));

            } else {
                bodyReservationSlot.setStartTimeInMillisecondsUTC(convertToMillisecondsEpoch(bodyReservationSlot.getDate(), bodyReservationSlot.getStartTimeInHours()));

            }
        }

        if (bodyReservationSlot.getEndTimeInHours() != null) {

            if (MINUTES.between(bodyReservationSlot.getStartTimeInHours(), bodyReservationSlot.getEndTimeInHours()) != reservationSlot.getDurationInMinutes()) {
                Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Invalid starting and ending time according to duration in minutes", "");
                response.setStatus(HttpStatus.CONFLICT);
                response.setData("Invalid starting and ending time according to duration in minutes");
                return ResponseEntity.status(response.getStatus()).body(response);
            }

            if (bodyReservationSlot.getDate() == null) {

                bodyReservationSlot.setEndTimeInMillisecondsUTC(convertToMillisecondsEpoch(reservationSlot.getDate(), bodyReservationSlot.getEndTimeInHours()));

            } else {
                bodyReservationSlot.setEndTimeInMillisecondsUTC(convertToMillisecondsEpoch(bodyReservationSlot.getDate(), bodyReservationSlot.getEndTimeInHours()));

            }

        }

        reservationSlot.update(bodyReservationSlot);

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation slot updated for Id: " + resourceSlotReservationId, "");
        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(resourceSlotReservationRepository.save(reservationSlot));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @DeleteMapping(path = {""}, name = "resource-slot-reservation-delete-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> deleteResourceSlotById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceSlotReservationId") String resourceSlotReservationId) {
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

        Optional<ResourceSlotReservation> optionalResourceSlotReservation = resourceSlotReservationRepository.findById(resourceSlotReservationId);

        if (!optionalResourceSlotReservation.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND: " + resourceSlotReservationId, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Slot found", "");
        resourceSlotReservationRepository.delete(optionalResourceSlotReservation.get());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Slot deleted, with id: {}", resourceSlotReservationId);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private long convertToMillisecondsEpoch(LocalDate date, LocalTime time) {
        LocalDateTime dt = LocalDateTime.parse(date + "T" + time);
        return dt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * Checks if a slot is within the <param> startTime </param> and <param>
     * endTime </param>
     *
     * @param resourceAvailabilityId
     * @param startTime
     * @param endTime
     * @return
     */
    private Boolean isReserved(String resourceAvailabilityId, long startTime, long endTime) {
        List<ResourceSlotReservation> resourceSlotReservations = resourceSlotReservationRepository.findByResourceAvailabilityId(resourceAvailabilityId);
        for (ResourceSlotReservation resourceSlotReservation : resourceSlotReservations) {
            if (resourceSlotReservation.getStartTimeInMillisecondsUTC() == startTime && resourceSlotReservation.getEndTimeInMillisecondsUTC() == endTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given day is off for an available resource
     *
     * @param resourceAvailabilityId
     * @param date
     * @return true if found/ false if not found
     */
    public Boolean isDayOff(String resourceAvailabilityId, LocalDate date) {
        List<ResourceSlotReservation> resourceSlotReservations = resourceSlotReservationRepository.findByResourceAvailabilityId(resourceAvailabilityId);
        if (!resourceSlotReservations.isEmpty()) {
            for (ResourceSlotReservation existingReservationSlot : resourceSlotReservations) {
                if (existingReservationSlot.getDate().equals(date) && existingReservationSlot.getStartTimeInHours().equals(LocalTime.parse("00:00:00")) && existingReservationSlot.getEndTimeInHours().equals(LocalTime.parse("23:59:00"))) {

                    return true;
                }
            }
        }
        return false;
    }

//    private String convertToDatetime(long timeInMilliseconds) {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
//        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMilliseconds), ZoneId.of("UTC")));
//    }
}
