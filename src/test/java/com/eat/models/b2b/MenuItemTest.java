package com.eat.models.b2b;

import com.eat.services.b2b.MenuItemService;
import org.junit.Ignore;
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
public class MenuItemTest {

    @Autowired
    private MenuItemService menuItemService;

    @Test
    @Ignore
    public void test_saveOnlyOne_success() {

    }

}