package com.eat.services.b2b;

import com.eat.repositories.sql.b2b.PlaceNetworkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlaceNetworkService {

    @Autowired
    private PlaceNetworkRepository networkRepository;


}
