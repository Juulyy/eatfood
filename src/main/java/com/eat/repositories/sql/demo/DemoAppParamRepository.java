package com.eat.repositories.sql.demo;

import com.eat.models.demo.DemoAppParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoAppParamRepository extends JpaRepository<DemoAppParam, Long> {

}