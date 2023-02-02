package com.contactlistapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactListController {



}
