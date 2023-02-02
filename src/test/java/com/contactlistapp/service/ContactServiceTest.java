package com.contactlistapp.service;

import com.contactlistapp.domain.Contact;
import com.contactlistapp.dto.mapper.ContactMapper;
import com.contactlistapp.dto.response.ContactResponse;
import com.contactlistapp.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactMapper contactMapper;

    private static Pageable pageable = PageRequest.of(0, 5, Sort.by("id", "ASC"));

    Page<ContactResponse> contactsByPage = null;

    @Test
    void getAllContactsWithPaging() {

        ContactResponse contact1 = new ContactResponse(1L, "Name Surname", "http://imagelink.com/image1.jpg");
        ContactResponse contact2 = new ContactResponse(2L, "Name2 Surname2", "http://imagelink.com/image2.jpg");
        ContactResponse contact3 = new ContactResponse();
        contact3.setId(3L);
        contact3.setContactName("Name3 Surnam3");
        contact3.setImageLink("http://imagelink.com/image3.jpg");
        List<ContactResponse> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), contacts.size());
        final Page<ContactResponse> pageContacts = new PageImpl<>(contacts.subList(start, end), pageable, contacts.size());

        when(contactRepository.getAllContactsWithPaging(any())).thenReturn(pageContacts);

        contactService.getAllContactsWithPaging("", pageable);
        contactService.getAllContactsWithPaging("a", pageable);

        verify(contactRepository, times(1)).getAllContactsWithPaging(pageable);
        verify(contactRepository, times(1)).getSearchedContactsWithPaging("a", pageable);
        verifyNoMoreInteractions(contactRepository);

    }

    @Test
    void findContactById() {

        Contact contact = new Contact(1L, "Name Surname", "http://imagelink.com/image1.jpg");
        Contact contact2 = new Contact();
        contact2.setId(2L);
        contact2.setContactName("Name2 Surname2");
        contact2.setImageLink("http://imagelink.com/image2.jpg");

        ContactResponse mappedContact = new ContactResponse(1L, "Name Surname", "http://imagelink.com/image1.jpg");

        when(contactRepository.findById(anyLong())).thenReturn(Optional.of(contact));
        when(contactMapper.contactToContactResponse(Mockito.any(Contact.class))).thenReturn(mappedContact);

        ContactResponse foundContact = contactService.findContactById(contact.getId());

        assertThat(foundContact).usingRecursiveComparison().isEqualTo(contact);
        verify(contactRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(contactRepository);

    }

    @Test
    void getContactResponseWithContact () {
        Contact contact = new Contact(1L,"Name Surname","http://image.com/image1.jpg");
        ContactResponse contactResponse = new ContactResponse(contact);
        assertEquals(contactResponse.getId(),contact.getId());
    }
}