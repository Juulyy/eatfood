package com.eat.controllers.mongo.common;

import com.eat.models.mongo.FAQ;
import com.eat.services.mongo.common.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/faqs")
public class FAQController {

    @Autowired
    private FAQService faqService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public FAQ findById(@PathVariable("id") String id) {
        return faqService.findById(id);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public FAQ add(@RequestBody FAQ faq) {
        return faqService.save(faq);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable String id) {
        faqService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<FAQ> findAll() {
        return faqService.findAll();
    }

    @GetMapping(value = "/find-all-by-type", consumes = "application/json", produces = "application/json")
    public List<FAQ> findAllByType(@Param(value = "type") FAQ.FAQType type) {
        return faqService.findAllByType(type);
    }

}