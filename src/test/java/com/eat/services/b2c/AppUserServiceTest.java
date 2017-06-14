package com.eat.services.b2c;

import com.eat.models.b2c.AppUser;
import lombok.extern.log4j.Log4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
@Log4j
public class AppUserServiceTest {

    @Autowired
    private AppUserService service;

    @Test
    @Ignore
    public void simpleTest() {
        System.out.println("pause for db filling");
        List<AppUser> mutualFollowers = service.getMutualFollowers(1l);
        System.out.println(mutualFollowers);
    }

}