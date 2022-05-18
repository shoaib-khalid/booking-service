/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hasan
 */
/**
 * Model used for storing unreserved slots of a resource at run time
 *
 */
@Data
@NoArgsConstructor
public class AvailableSlot {

    private String day;
    private long startTime;
    private long endTime;
}
