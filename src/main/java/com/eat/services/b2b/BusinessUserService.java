package com.eat.services.b2b;

import com.eat.models.b2b.BusinessUser;
import com.eat.repositories.sql.b2b.BusinessUserRepository;
import com.eat.repositories.sql.b2b.PlaceNetworkRepository;
import com.eat.repositories.sql.b2b.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BusinessUserService {

    @Autowired
    private BusinessUserRepository businessUserRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceNetworkRepository placeNetworkRepository;

    public BusinessUser save(BusinessUser businessUser){
        return businessUserRepository.save(businessUser);
    }

    public BusinessUser findById(Long id){
        return businessUserRepository.findOne(id);
    }

    public void delete(Long id){
        businessUserRepository.delete(id);
    }

    public BusinessUser update(BusinessUser businessUser){
        return businessUserRepository.save(businessUser);
    }

    public List<BusinessUser> findAll(){
        return businessUserRepository.findAll();
    }

    public BusinessUser findByEmail(String email) {
        return businessUserRepository.findByEmail(email);
    }

}