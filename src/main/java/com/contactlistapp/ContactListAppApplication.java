package com.contactlistapp;

import com.contactlistapp.service.RoleService;
import com.contactlistapp.service.UserService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.CommandLineRunner;
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
    public CommandLineRunner demo(UserService userService, RoleService roleService) {
        return (args) -> {
            roleService.addRoles();
            userService.addAdminWithStart();
        };
    }




}
