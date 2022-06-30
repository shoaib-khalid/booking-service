/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kalsym.service.vertical.enums.AvailabilityDay;
import com.kalsym.service.vertical.enums.ConfirmationMethod;
import com.kalsym.service.vertical.model.product.Product;
import com.kalsym.service.vertical.model.store.Store;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author hasan
 */
/**
 *
 * This table is used to save the availability details of a resource
 */
@Entity
@Table(name = "resource_availability")
@Data
@NoArgsConstructor
public class ResourceAvailability implements Serializable {

    /**
     * Auto Generated Unique id
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 50)
    private String id;
    
    /**
     * The available days of a resource
     */
    @Column(name = "availabilityDay")
    @Enumerated(EnumType.STRING)
    private AvailabilityDay availabilityDay;

    /**
     * The duration of a slot of each resource
     */
    @Column(name = "durationInMinutes", nullable = false)
    private Integer durationInMinutes;

    /**
     * The starting time of a resource availability in UTC
     */
    @Column(name = "startTime", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    /**
     * The last/ending time of a resource availability in UTC
     */
    @Column(name = "endTime", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    /**
     * The offset hours to add or minus to bring the start and end time to UTC
     */
    @Column(name = "offsetHours", length = 10)
    private String offsetHours;

    /**
     * Adds the given time in between two slots
     */
    @Column(name = "minimumTimeBetweenReservation")
    private Integer minimumTimeBetweenReservation;

    /**
     * The way that reservations of a resource will be confirmed
     */
    @Column(name = "confirmationMethod")
    @Enumerated(EnumType.STRING)
    private ConfirmationMethod confirmationMethod;

    @Column(name = "description", length = 200)
    private String description;

    @CreationTimestamp
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "resourceAvailability")
    @JsonIgnore
    private List<ResourceSlotReservation> resourceSlotReservation;

    /**
     * The Resource of availability
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceId", referencedColumnName = "id")
    private Resource resource;

    /**
     * The Store of the resource
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId", referencedColumnName = "id")
    @JsonIgnore
    private Store store;

    public void update(ResourceAvailability reservationResource) {

        if (reservationResource.getAvailabilityDay() != null) {
            availabilityDay = reservationResource.getAvailabilityDay();
        }

        if (reservationResource.getDurationInMinutes() != null) {
            durationInMinutes = reservationResource.getDurationInMinutes();
        }

        if (reservationResource.getStartTime() != null) {
            startTime = reservationResource.getStartTime();
        }

        if (reservationResource.getEndTime() != null) {
            endTime = reservationResource.getEndTime();
        }

        if (reservationResource.getMinimumTimeBetweenReservation() != null) {
            minimumTimeBetweenReservation = reservationResource.getMinimumTimeBetweenReservation();
        }

        if (reservationResource.getDescription() != null) {
            description = reservationResource.getDescription();
        }
        
        if (reservationResource.getOffsetHours() != null){
            offsetHours = reservationResource.getOffsetHours();
        }
        
        if (reservationResource.getConfirmationMethod() != null){
            confirmationMethod = reservationResource.getConfirmationMethod();
        }
    }

}
