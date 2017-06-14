package com.eat.services.mongo.common;

import com.eat.models.mongo.ProblemReport;
import com.eat.repositories.mongo.common.ProblemReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProblemReportService {

    @Autowired
    private ProblemReportRepository repository;

    public ProblemReport findById(String id) {
        return repository.findOne(id);
    }

    public ProblemReport save(ProblemReport report) {
        return repository.save(report);
    }

    public void update(ProblemReport report) {
        repository.save(report);
    }

    public void delete(ProblemReport report) {
        repository.delete(report);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<ProblemReport> findAll() {
        return repository.findAll();
    }

}
