package com.contactlistapp;

import com.contactlistapp.service.RoleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContactListAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactListAppApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> appReady(RoleService roleService){
        return event -> roleService.addRoles();
    }



}
