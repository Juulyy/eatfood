package com.eat.services.b2b;

import com.eat.models.b2b.ContactType;
import com.eat.repositories.sql.b2b.ContactTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactTypeService {

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @PostConstruct
    private void init() {
        try {


            EnumSet<ContactType.ContactAspect> domainAspects = EnumSet.allOf(ContactType.ContactAspect.class);
            Set<ContactType.ContactAspect> persistedAspects = contactTypeRepository.findAll().stream()
                    .map(ContactType::getContactAspect)
                    .collect(Collectors.toSet());
            domainAspects.forEach(domainAspect -> {
                if (!persistedAspects.contains(domainAspect)) {
                    save(ContactType.of()
                            .contactAspect(domainAspect)
                            .name(domainAspect.getName())
                            .create());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public ContactType findById(Long id) {
        return contactTypeRepository.findOne(id);
    }

    public ContactType findByName(String name) {
        return contactTypeRepository.findByName(name);
    }

    public List<ContactType> findAll() {
        return contactTypeRepository.findAll();
    }

    public ContactType save(ContactType contactType) {
        return contactTypeRepository.save(contactType);
    }

    public List<ContactType> saveAll(List<ContactType> contactTypes) {
        return contactTypeRepository.save(contactTypes);
    }

    public void delete(Long id) {
        contactTypeRepository.delete(id);
    }

    public void delete(ContactType contactType) {
        contactTypeRepository.delete(contactType);
    }

    public ContactType update(ContactType contactType) {
        return contactTypeRepository.save(contactType);
    }

    public List<ContactType> findContactTypeByIds(List<Long> ids) {
        return contactTypeRepository.findByIdIn(ids);
    }

    public List<ContactType> findContactTypeByName(String name) {
        return contactTypeRepository.findByNameIgnoreCase(name);
    }

    public List<ContactType> findContactTypeByNameContaining(String name) {
        return contactTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<ContactType> findContactTypeByContactAspect(String name) {
        return contactTypeRepository.findByContactAspect(ContactType.ContactAspect.valueOf(name));
    }

}