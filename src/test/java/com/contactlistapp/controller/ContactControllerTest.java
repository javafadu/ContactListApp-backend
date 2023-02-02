package com.contactlistapp.controller;

import com.contactlistapp.dto.response.ContactResponse;
import com.contactlistapp.service.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    private static Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());


    @Test
    void getAllContactsWithPaging() {

        ContactResponse contact1 = new ContactResponse(1L, "Name Surname", "http://imagelink.com/image1.jpg");
        ContactResponse contact2 = new ContactResponse(2L, "Name2 Surname2", "http://imagelink.com/image2.jpg");
        ContactResponse contact3 = new ContactResponse(3L, "Name2 Surname2", "http://imagelink.com/image3.jpg");
        List<ContactResponse> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), contacts.size());
        final Page<ContactResponse> pageContacts = new PageImpl<>(contacts.subList(start, end), pageable, contacts.size());

        when(contactService.getAllContactsWithPaging("a",pageable)).thenReturn(pageContacts);
        ResponseEntity<Page<ContactResponse>>response = contactController.getAllContactsWithPaging("a",0,5,"id",Sort.Direction.ASC);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getTotalElements(),3);

    }

    @Test
    void getCityById() {
        ContactResponse contact1 = new ContactResponse(1L, "Name Surname", "http://imagelink.com/image1.jpg");
        ContactResponse contact2 = new ContactResponse(2L, "Name2 Surname2", "http://imagelink.com/image2.jpg");
        List<ContactResponse> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        Long contactId = 1L;
        when(contactService.findContactById(contactId)).thenReturn(contact1);
        ResponseEntity<ContactResponse> response = contactController.getCityById(contactId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contactId,response.getBody().getId());
        assertEquals(contact1.getImageLink(),response.getBody().getImageLink());

    }
}