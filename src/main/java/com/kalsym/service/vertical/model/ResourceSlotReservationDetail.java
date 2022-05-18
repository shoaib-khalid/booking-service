/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.model;

import com.kalsym.service.vertical.enums.ReservationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * This table is used to store the reservation details
 */
@Entity
@Table(name = "resource_slot_reservation_detail")
@Data
@NoArgsConstructor
public class ResourceSlotReservationDetail implements Serializable {

    /**
     * Auto generated unique ID
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 50)
    private String id;

    /**
     * The name of the customer of the reservation
     */
    @Column(name = "customerName", length = 50)
    private String customerName;

    /**
     * The email of the customer
     */
    @Column(name = "customerEmail", length = 50)
    private String customerEmail;

    /**
     * The Phone number of the customer
     */
    @Column(name = "customerPhoneNumber", length = 50)
    private String customerPhoneNumber;

    /**
     * The status of a reservation
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    
    @Column(name = "customerNotes", length = 200)
    private String customerNotes;

    @CreationTimestamp
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;

    @UpdateTimestamp
    private LocalDateTime updated;

    /**
     * The slot of the reservation
     */
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "resourceSlotReservationId", referencedColumnName = "id", nullable = false)
    private ResourceSlotReservation resourceSlotReservation;

    public void update(ResourceSlotReservationDetail reservation) {

        if (reservation.getCustomerName() != null) {
            customerName = reservation.getCustomerName();
        }

        if (reservation.getCustomerEmail() != null) {
            customerEmail = reservation.getCustomerEmail();
        }

        if (reservation.getCustomerNotes() != null) {
            customerNotes = reservation.getCustomerNotes();
        }

        if (reservation.getStatus() != null) {
            status = reservation.getStatus();
        }

        if (reservation.getCustomerPhoneNumber() != null) {
            customerPhoneNumber = reservation.getCustomerPhoneNumber();
        }
    }
}
