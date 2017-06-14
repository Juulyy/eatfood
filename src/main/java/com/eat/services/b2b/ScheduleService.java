package com.eat.services.b2b;

import com.eat.models.b2b.Schedule;
import com.eat.repositories.sql.b2b.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository repository;

    public Schedule getById(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Collection<Schedule> getAll() {
        return repository.findAll();
    }

    public Schedule save(Schedule schedule) {
        return repository.save(schedule);
    }

    public List<Schedule> saveAll(List<Schedule> schedules) {
        return repository.save(schedules);
    }

    public List<Schedule> findAllByName(String name){
        return repository.findByNameContaining(name);
    }

    public Schedule findOneByName(String name){
        return repository.findFirstByNameContaining(name);
    }
}
