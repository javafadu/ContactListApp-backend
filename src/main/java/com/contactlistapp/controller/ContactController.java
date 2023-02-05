package com.contactlistapp.controller;

import com.contactlistapp.dto.response.ContactResponse;
import com.contactlistapp.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactController {

    private ContactService contactService;

    // 1- GET all contacts or searched contacts with Paging by anyone
    @GetMapping()
    public ResponseEntity<Page<ContactResponse>> getAllContactsWithPaging(
            @RequestParam(required = false, value = "q", defaultValue = "") String q,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<ContactResponse> contactList = contactService.getAllContactsWithPaging(q, pageable);
        return ResponseEntity.ok(contactList);
    }

    // 2- GET a Contact with Id by anyone
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        ContactResponse contactResponse = contactService.findContactById(id);
        return new ResponseEntity<>(contactResponse, HttpStatus.OK);

    }

}
