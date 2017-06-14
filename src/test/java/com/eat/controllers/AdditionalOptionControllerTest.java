/*
package com.eat.controllers;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdditionalOptionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void createAdditionalOption() {
        AdditionalOption createdAdditionalOption = AdditionalOption.of()
                .name("music")
                .create();

        ResponseEntity<AdditionalOption> responseEntity =
                restTemplate.postForEntity("/api/additional-option/add", createdAdditionalOption, AdditionalOption.class);

        AdditionalOption additionalOption = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("music", additionalOption.getName());
    }

    @Test
    public void findByIdAdditionalOption() {

        ResponseEntity<AdditionalOption> responseEntity =
                restTemplate.getForEntity("/api/additional-option/{id}", AdditionalOption.class, 1L);

        AdditionalOption additionalOption = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("music", additionalOption.getName());
    }

    @Test
    public void removeByIdAdditionalOption() {

        ResponseEntity<HttpStatus> response =
                restTemplate.exchange("/api/additional-option/{id}",
                        HttpMethod.DELETE,
                        new HttpEntity<String>(""),
                        HttpStatus.class,
                        1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
*/
