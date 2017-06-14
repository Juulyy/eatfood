package com.eat.services.b2c;

import com.eat.models.b2b.BusinessUser;
import com.eat.services.common.RolePermissionsService;
import com.eat.services.common.RoleService;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
@Log4j
public class RolePermissionsServiceTest {

    @Autowired
    private RolePermissionsService permissionsService;

    @Autowired
    private RoleService roleService;

    @Test
    public void simple_test() {
        System.out.println(BusinessUser.of()
                .firstName("1")
                .lastName("2")
                .login("3")
                .password("4")
                .email("5")
//                .establishments(null)
                .create()
                .toString());
    }

}