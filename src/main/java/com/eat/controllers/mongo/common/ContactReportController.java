package com.eat.controllers.mongo.common;

import com.eat.models.mongo.ContactReport;
import com.eat.models.mongo.enums.ContactReportType;
import com.eat.services.mongo.common.ContactReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contact-report")
public class ContactReportController {

    @Autowired
    private ContactReportService reportService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ContactReport findById(@PathVariable("id") String id) {
        return reportService.findById(id);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ContactReport add(@RequestBody ContactReport report) {
        return reportService.save(report);
    }

    @PutMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ContactReport update(@RequestBody ContactReport report) {
        return reportService.update(report);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable String id) {
        reportService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<ContactReport> findAll() {
        return reportService.findAll();
    }

    @GetMapping(value = "/find-all-by-type", consumes = "application/json", produces = "application/json")
    public List<ContactReport> findAllByType(@Param(value = "type") ContactReportType type) {
        return reportService.findAllByType(type);
    }

    @GetMapping(value = "/find-all-by-user", consumes = "application/json", produces = "application/json")
    public List<ContactReport> findAllByUser(@Param(value = "user") String userId) {
        return reportService.findAllByUserId(userId);
    }

    @GetMapping(value = "/find-all-by-place", consumes = "application/json", produces = "application/json")
    public List<ContactReport> findAllByPlace(@Param(value = "place") String placeId) {
        return reportService.findAllByPlaceId(placeId);
    }

    @GetMapping(value = "/find-all-by-text", consumes = "application/json", produces = "application/json")
    public List<ContactReport> findAllByText(@Param(value = "text") String text) {
        return reportService.findAllByContainingText(text);
    }
}
