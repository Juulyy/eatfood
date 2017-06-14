package com.eat.services.demo;

import com.eat.dto.demo.DemoAppParamDto;
import com.eat.models.common.enums.RoleType;
import com.eat.models.demo.DemoAppParam;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.repositories.sql.demo.DemoAppParamRepository;
import com.eat.services.common.RoleService;
import com.eat.utils.converters.dto.DemoAppUserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;

@Slf4j
@Component
public class DemoAppParamService {

    @Autowired
    private DemoAppParamRepository repository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DemoAppUserConverter converter;

    @Resource
    private DemoAppParamService paramService;

    public DemoAppParam updateParams(Long id, LocalTime time, Double temp, WeatherIcon icon, Double longtitude,
                                     Double latitude) {
        DemoAppParam demoAppParam = findById(id);
        demoAppParam.setTime(time);
        demoAppParam.setTemp(temp);
        demoAppParam.setIcon(icon);
        demoAppParam.setLongtitude(longtitude);
        demoAppParam.setLatitude(latitude);
        return repository.save(demoAppParam);
    }

    public DemoAppParam addDemoUser(DemoAppParamDto dto) {
        DemoAppParam demoAppParam = converter.toDemoAppParam(dto);
        demoAppParam.getAppUser().setRole(roleService.getRoleByRoleType(RoleType.ROLE_APP_USER));
        return paramService.save(demoAppParam);
    }

    public DemoAppParam save(DemoAppParam demoAppParam) {
        return repository.save(demoAppParam);
    }

    public DemoAppParam update(DemoAppParam demoAppParam) {
        return repository.save(demoAppParam);
    }

    public void remove(Long demoUserId) {
        repository.delete(demoUserId);
    }

    public DemoAppParam findById(Long id) {
        return repository.findOne(id);
    }

}