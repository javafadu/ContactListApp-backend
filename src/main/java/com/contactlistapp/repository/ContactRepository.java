package com.contactlistapp.repository;

import com.contactlistapp.domain.Contact;
import com.contactlistapp.dto.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT new com.contactlistapp.dto.response.ContactResponse(c)  FROM Contact c where (lower(c.contactName) like %?1% )")
    Page<ContactResponse> getSearchedContactsWithPaging(String q, Pageable pageable);

    @Query("SELECT new com.contactlistapp.dto.response.ContactResponse(c)  FROM Contact c")
    Page<ContactResponse> getAllContactsWithPaging(Pageable pageable);

}
