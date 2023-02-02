package com.contactlistapp.service;

import com.contactlistapp.domain.Contact;
import com.contactlistapp.dto.mapper.ContactMapper;
import com.contactlistapp.dto.response.ContactResponse;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ContactService {

    private ContactRepository contactRepository;
    private ContactMapper contactMapper;


    public Page<ContactResponse> getAllContactsWithPaging(String q, Pageable pageable) {
        Page<ContactResponse> allContactsWithPaging = null;
        if (!q.isEmpty()) {
            allContactsWithPaging = contactRepository.getSearchedContactsWithPaging(q.toLowerCase(), pageable);
        } else {
            allContactsWithPaging = contactRepository.getAllContactsWithPaging(pageable);
        }
        return allContactsWithPaging;
    }

    public ContactResponse findContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return contactMapper.contactToContactResponse(contact);
    }
}
