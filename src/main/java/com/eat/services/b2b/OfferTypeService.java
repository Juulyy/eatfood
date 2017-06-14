package com.eat.services.b2b;

import com.eat.models.b2b.OfferType;
import com.eat.repositories.sql.b2b.OfferTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class OfferTypeService {

    @Autowired
    private OfferTypeRepository repository;

    public OfferType getById(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Collection<OfferType> getAll() {
        return repository.findAll();
    }

    public OfferType save(OfferType offerType) {
        return repository.save(offerType);
    }
}
