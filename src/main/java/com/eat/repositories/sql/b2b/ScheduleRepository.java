package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends BaseB2BRepository<Schedule, Long> {

    List<Schedule>findByNameContaining(String name);

    Schedule findFirstByNameContaining(String name);





}
