package com.eat.controllers.mongo.common;

import com.eat.models.mongo.ProblemReport;
import com.eat.services.mongo.common.ProblemReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reports")
public class ProblemReportController {

    @Autowired
    private ProblemReportService reportService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ProblemReport findById(@PathVariable("id") String id) {
        return reportService.findById(id);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ProblemReport add(@RequestBody ProblemReport report) {
        return reportService.save(report);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable String id) {
        reportService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<ProblemReport> findAll() {
        return reportService.findAll();
    }

}
