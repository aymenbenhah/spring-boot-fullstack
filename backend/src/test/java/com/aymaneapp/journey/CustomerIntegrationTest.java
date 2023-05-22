package com.aymaneapp.journey;

import com.aymaneapp.customer.Customer;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.http.MediaType.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Test
    void canRegisterACustomer() {
        Faker faker = new Faker();
        int id = 2;
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" +UUID.randomUUID() + name + "@gmail.com";
        int age = nextInt(1,100);
        Customer customer = new Customer(id,name,email,age);
        webTestClient.post()
                .uri("apiapi/v1/customers")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(customer),Customer.class)
                .exchange()
                .expectStatus();
                //.isBadRequest();
    }
}
