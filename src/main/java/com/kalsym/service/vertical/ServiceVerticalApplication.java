package com.kalsym.service.vertical;

import com.kalsym.service.vertical.controller.ResourceSlotReservationController;
import com.kalsym.service.vertical.model.ResourceSlotReservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceVerticalApplication {

    
    public static String VERSION;

    static {
        System.setProperty("spring.jpa.hibernate.naming.physical-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceVerticalApplication.class, args);
        ResourceSlotReservationController controller = new ResourceSlotReservationController();
        
    }
    
}
