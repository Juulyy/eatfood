//package com.eat.models.b2c;
//
//import com.eat.repositories.sql.b2c.TasteDetailRepository;
//import com.eat.repositories.sql.b2c.TasteTypeRepository;
//import lombok.extern.log4j.Log4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.eat.utils.B2CTestHelper.getAllTasteDetails;
//import static com.eat.utils.B2CTestHelper.getAllTasteTypes;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@WebAppConfiguration
//@Log4j
//public class B2CModelsTest {
//
//    @Autowired
//    public TasteTypeRepository tasteTypeRepository;
//
//    @Autowired
//    public TasteDetailRepository tasteDetailRepository;
//
//    @Test
//    @Rollback(false)
//    public void save_only() {
////        tasteTypeRepository.save(getAllTasteTypes());
//        log.info("Saved all tastes! Size: " + getAllTasteTypes().size());
//        tasteDetailRepository.save(getAllTasteDetails());
//    }
//
//}