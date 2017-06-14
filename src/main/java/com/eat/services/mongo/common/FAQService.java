package com.eat.services.mongo.common;

import com.eat.models.mongo.FAQ;
import com.eat.repositories.mongo.common.FAQRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FAQService {

    @Autowired
    private FAQRepository repository;

    public FAQ findById(String id) {
        return repository.findOne(id);
    }

    public FAQ save(FAQ faq) {
        return repository.save(faq);
    }

    public void update(FAQ faq) {
        repository.save(faq);
    }

    public void delete(FAQ faq) {
        repository.delete(faq);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<FAQ> findAll() {
        return repository.findAll();
    }

    public List<FAQ> findAllByType(FAQ.FAQType type) {
        return repository.findAllByType(type);
    }

    public List<FAQ> findAllByContainingText(String text) {
        return repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny(text));
    }

}