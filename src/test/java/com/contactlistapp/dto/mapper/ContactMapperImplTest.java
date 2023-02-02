package com.contactlistapp.dto.mapper;

import com.contactlistapp.domain.Contact;
import com.contactlistapp.domain.User;
import com.contactlistapp.dto.response.ContactResponse;
import com.contactlistapp.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContactMapperImplTest {

    @InjectMocks
    private ContactMapperImpl contactMapper;

    @Test
    void contactToContactResponse() {
        Contact contact = new Contact(1L,"Name Surname","http://image.com/image1.jpg");
        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setId(contact.getId());
        contactResponse.setContactName(contact.getContactName());
        contactResponse.setImageLink(contact.getImageLink());

        ContactResponse mappedContact = contactMapper.contactToContactResponse(contact);
        assertEquals(mappedContact.getContactName(),contact.getContactName());
    }

    @Test
    void contactToContactResponseWithNullContact() {

        ContactResponse contactResponse = new ContactResponse();
        Contact contact = null;

        ContactResponse respContact = contactMapper.contactToContactResponse(contact);
        assertNull(respContact);

    }
}