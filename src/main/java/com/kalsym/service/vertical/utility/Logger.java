/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.utility;

import org.slf4j.LoggerFactory;

/**
 *
 * @author Sarosh
 */
public class Logger {

    public static final org.slf4j.Logger application = LoggerFactory.getLogger("application");
    
    
     public static final org.slf4j.Logger cdr = LoggerFactory.getLogger("cdr");

    public static String pattern = "[v{}][{}] {}";
}
