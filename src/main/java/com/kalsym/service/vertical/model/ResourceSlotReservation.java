/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
 * This table is used to store the slots of a resource
 */
@Entity
@Table(name = "resource_slot_reservation")
@Data
@NoArgsConstructor
public class ResourceSlotReservation implements Serializable {

    /**
     * Auto generated unique Id
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 50)
    private String Id;

    /**
     * True if a slot is reserved, false if unreserved
     */
    @Column(name = "isReserved")
    private Boolean isReserved;

    /**
     * The duration of a slot in minutes
     */
    @Column(name = "durationInMinutes", nullable = false)
    private Integer durationInMinutes;

    /**
     * The date of the slot
     */
    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The Slot starting date and time in milliseconds epoch in UTC format
     */
    @Column(name = "startTimeInMillisecondsUTC", nullable = false)
    private long startTimeInMillisecondsUTC;

    /**
     * The Slot ending date and time in milliseconds epoch in UTC format
     */
    @Column(name = "endTimeInMillisecondsUTC", nullable = false)
    private long endTimeInMillisecondsUTC;

    /**
     * The slot starting time in 24hours format
     */
    @Column(name = "startTimeInHours")
    private LocalTime startTimeInHours;

    /**
     * The slot ending time in 24hours format
     */
    @Column(name = "endTimeInHours")
    private LocalTime endTimeInHours;

    @CreationTimestamp
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;

    @UpdateTimestamp
    private LocalDateTime updated;

    /**
     * The resource of the slot
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceAvailabilityId", referencedColumnName = "id")
    @JsonBackReference
    private ResourceAvailability resourceAvailability;

    @OneToOne(mappedBy = "resourceSlotReservation", fetch = FetchType.LAZY)
    @JsonIgnore
    private ResourceSlotReservationDetail resourceSlotReservationDetail;

    public void update(ResourceSlotReservation reservationSlot) {

        if (reservationSlot.isReserved != null) {
            isReserved = reservationSlot.isReserved;
        }

        if (reservationSlot.getDurationInMinutes() != null) {
            durationInMinutes = reservationSlot.getDurationInMinutes();
        }

        if (reservationSlot.getStartTimeInMillisecondsUTC() != 0) {
            startTimeInMillisecondsUTC = reservationSlot.getStartTimeInMillisecondsUTC();
        }

        if (reservationSlot.getEndTimeInMillisecondsUTC() != 0) {
            endTimeInMillisecondsUTC = reservationSlot.getEndTimeInMillisecondsUTC();
        }

        if (reservationSlot.getStartTimeInHours() != null) {
            startTimeInHours = reservationSlot.getStartTimeInHours();
        }

        if (reservationSlot.getEndTimeInHours() != null) {
            endTimeInHours = reservationSlot.getEndTimeInHours();
        }

        if (reservationSlot.getDate() != null) {
            date = reservationSlot.getDate();
        }
    }

}
