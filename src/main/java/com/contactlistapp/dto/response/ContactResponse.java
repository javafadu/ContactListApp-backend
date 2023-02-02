package com.contactlistapp.dto.response;

import com.contactlistapp.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    private Long id;
    private String contactName;
    private String imageLink;

    public ContactResponse(Contact contact) {
        this.id = contact.getId();
        this.contactName = contact.getContactName();
        this.imageLink = contact.getImageLink();
    }


}
