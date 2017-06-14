package com.eat.services.b2c;

import com.eat.models.b2c.NotInteresting;
import com.eat.repositories.sql.b2c.NotInterestingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class NotInterestingService {

    @Autowired
    private NotInterestingRepository repository;

    public NotInteresting findById(Long id) {
        return repository.findOne(id);
    }

    public NotInteresting save(NotInteresting notInteresting) {
        return repository.save(notInteresting);
    }

    public void update(NotInteresting notInteresting) {
        repository.save(notInteresting);
    }

    public void delete(NotInteresting notInteresting) {
        repository.delete(notInteresting);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<NotInteresting> findAll() {
        return repository.findAll();
    }

}