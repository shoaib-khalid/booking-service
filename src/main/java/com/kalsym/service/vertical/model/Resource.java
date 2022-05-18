/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kalsym.service.vertical.enums.ResourceStatus;
import com.kalsym.service.vertical.model.product.Product;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author hasan
 */
/**
 * Resource table used for storing product details to be used for reservation
 * purposes
 *
 */
@Entity
@Table(name = "resource")
@Data
@NoArgsConstructor
public class Resource {

    /**
     * Auto Generated Unique id for a resource
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 50)
    private String id;

    /**
     * The current status of a resource, Default value is "DEFAULT"
     */
    @Column(name = "status", columnDefinition = "default 'DEFAULT'")
    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    /**
     * The total number of weeks for which a resource can be reserved
     */
    @Column(name = "numberOfWeeksReservable", columnDefinition = "default 1")
    private Integer numberOfWeeksReservable;

    /**
     * The price of a resource
     */
    @Column(name = "price")
    private Double price;

    /**
     * The product of a resource
     */
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
    private Product resourceProduct;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ResourceAvailability> resourceAvailability;

    public void update(Resource resource) {

        if (null != resource.getNumberOfWeeksReservable()) {
            numberOfWeeksReservable = resource.getNumberOfWeeksReservable();
        }

        if (null != resource.getPrice()) {
            price = resource.getPrice();
        }

        if (null != resource.getStatus()) {
            status = resource.getStatus();
        }
        if (null != resource.getResourceAvailability()) {
            resourceAvailability = resource.getResourceAvailability();
        }
    }
}
