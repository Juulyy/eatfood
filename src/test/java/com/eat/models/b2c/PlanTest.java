package com.eat.models.b2c;

import com.eat.models.b2b.enums.BusinessPlanCategory;
import com.eat.models.b2b.enums.BusinessPlanStatus;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.enums.ClientPlanCategory;
import com.eat.models.b2c.enums.ClientPlanStatus;
import com.eat.models.common.enums.RoleType;
import com.eat.services.b2c.PlanService;
import com.eat.services.common.RoleService;
import lombok.extern.log4j.Log4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
@Log4j
public class PlanTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private RoleService roleService;

    @Test
    @Ignore
    public void save_planStatusAndPrivacyType_correctly() {
        planService.save(Plan.of()
                .description("test")
                .privacyType(Plan.PrivacyType.PRIVATE)
                .clientPlanCategory(ClientPlanCategory.CREATED)
                .clientPlanStatus(ClientPlanStatus.PENDING)
                .businessPlanCategory(BusinessPlanCategory.PENDING)
                .businessPlanStatus(BusinessPlanStatus.SUCCESSFUL)
                .planDate(LocalDateTime.now())
                .organizer(AppUser.of()
                        .firstName("test")
                        .lastName("test")
                        .login("test")
                        .email("maystrovyy@gmail.com")
                        .password("testasdasd123AZ")
                        .gender(AppUser.Gender.FEMALE)
                        .role(roleService.getRoleByRoleType(RoleType.ROLE_APP_USER))
                        .create())
                .place(Place.of()
                        .name("test")
                        .create())
                .create());
        Plan plan = planService.findById(1L);
        assertEquals(plan.getPrivacyType(), Plan.PrivacyType.PRIVATE);
        assertEquals(plan.getPrivacyType().getId(), Plan.PrivacyType.PRIVATE.getId());
        assertEquals(plan.getBusinessPlanCategory(), BusinessPlanCategory.PENDING);
    }

}