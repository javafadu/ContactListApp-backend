package com.contactlistapp.repository;

import com.contactlistapp.domain.ContactList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactListRepository extends JpaRepository<ContactList, Long> {

}
