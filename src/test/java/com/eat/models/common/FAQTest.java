package com.eat.models.common;

import com.eat.models.mongo.FAQ;
import com.eat.repositories.mongo.common.FAQRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FAQTest {

    @Autowired
    private FAQRepository repository;

    @Test
    @Rollback(value = false)
    public void save_onlySuccess() {
        repository.deleteAll();

        repository.save(FAQ.of()
                .locale(Locale.ENGLISH)
                .question("How to become a business user?")
                .answer("You can become a business user via the registration link eat.com/business/new or " +
                        "by phone +8 800 000 000.")
                .type(FAQ.FAQType.B2B)
                .create());

        repository.save(FAQ.of()
                .locale(Locale.ENGLISH)
                .question("How to get started with ios application?")
                .answer("You can download EAT app by link app.store.com/eat/download")
                .type(FAQ.FAQType.B2C)
                .create());

        List<FAQ> faqs = repository.findAll();
        assertEquals(faqs.size(), 2);
        assertEquals(faqs.get(0).getType().getId(), FAQ.FAQType.B2B.getId());

        assertEquals(repository.findAllByType(FAQ.FAQType.B2B).size(), 1);
        assertEquals(repository.findAllByType(FAQ.FAQType.B2C).size(), 1);
    }

    @Test
    public void findByWordTest() {
        repository.findAllBy(TextCriteria.forDefaultLanguage().matching("phone2"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matching("PHONE"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matching("EAT 213"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matching("app lication"));

        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingPhrase("phone2"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingPhrase("PHONE"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingPhrase("EAT 213"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingPhrase("app lication"));

        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny("phone2"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny("PHONE"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny("EAT 213"));
        repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny("app lication"));
    }

}