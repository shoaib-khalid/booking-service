/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.enums;

/**
 *
 * @author hasan
 */
public enum ReservationStatus {
    CONFIRMED_BY_MERCHANT,
    CONFIRMED_BY_PAYMENT,
    CONFIRMED_BY_PHONE,
    CONFIRMED_BY_EMAIL,
    ONHOLD,
    WAITING,
    WAITING_FOR_PAYMENT,
    CANCELED
}
// ENUM('CONFIRMED_BY_MERCHANT','CONFIRMED_BY_PAYMENT','CONFIRMED_BY_PHONE','CONFIRMED_BY_EMAIL','ONHOLD','WAITING','WAITING_FOR_PAYMENT','CANCELED')