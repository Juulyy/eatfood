package com.eat.services.b2b;


import com.eat.models.b2b.DayOpenTime;
import com.eat.repositories.sql.b2b.DayOpenTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class DayOpenTimeService {

    @Autowired
    private DayOpenTimeRepository repository;

    public DayOpenTime getById(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Collection<DayOpenTime> getAll() {
        return repository.findAll();
    }

    public DayOpenTime save(DayOpenTime dayOpenTime) {
        return repository.save(dayOpenTime);
    }

}
