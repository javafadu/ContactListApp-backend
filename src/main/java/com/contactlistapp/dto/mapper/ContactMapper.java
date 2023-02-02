package com.contactlistapp.dto.mapper;

import com.contactlistapp.domain.Contact;
import com.contactlistapp.dto.response.ContactResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactResponse contactToContactResponse(Contact contact);
}
