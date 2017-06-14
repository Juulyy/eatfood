package com.eat.services.b2b;

import com.eat.models.b2b.Contact;
import com.eat.repositories.sql.b2b.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> findAll() {
        return new ArrayList<>(contactRepository.findAll());
    }

    public Contact getById(String id) {
        return contactRepository.findOne(Long.valueOf(id));
    }

    public Contact add(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteById(Long id) {
        contactRepository.delete(id);
    }

    public Contact update(Contact contact) {
        return contactRepository.save(contact);
    }

    public void update(Set<Contact> contacts) {
        contactRepository.save(contacts);
    }

    public List<Contact> findContactByIds(List<Long> ids) {
        return contactRepository.findByIdIn(ids);
    }

}