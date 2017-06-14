package com.eat.services.mongo.common;

import com.eat.models.mongo.ContactReport;
import com.eat.models.mongo.enums.ContactReportType;
import com.eat.repositories.mongo.common.ContactReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ContactReportService {

    @Autowired
    private ContactReportRepository repository;

    public ContactReport findById(String id) {
        return repository.findOne(id);
    }

    public ContactReport save(ContactReport report) {
        return repository.save(report);
    }

    public ContactReport update(ContactReport report) {
        return repository.save(report);
    }

    public void delete(ContactReport report) {
        repository.delete(report);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<ContactReport> findAll() {
        return repository.findAll();
    }

    public List<ContactReport> findAllByType(ContactReportType type) {
        return repository.findAllByType(type);
    }

    public List<ContactReport> findAllByUserId(String userId){
        return repository.findAllByUserId(userId);
    }

    public List<ContactReport> findAllByPlaceId(String placeId){
        return repository.findAllByPlaceId(placeId);
    }

    public List<ContactReport> findAllByContainingText(String text) {
        return repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny(text));
    }
}
