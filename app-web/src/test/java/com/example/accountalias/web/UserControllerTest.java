package com.example.accountalias.web;

import com.example.accountalias.web.dto.AuthDtos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) { return "http://localhost:" + port + path; }

    @Test
    void me_requires_auth_and_returns_profile() {
        ResponseEntity<String> unauth = rest.getForEntity(url("/api/users/me"), String.class);
        assertThat(unauth.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // register and call with basic auth
        rest.postForEntity(url("/api/auth/register"), new AuthDtos.RegisterRequest("me@example.com", "Password1!"), String.class);

        TestRestTemplate auth = rest.withBasicAuth("me@example.com", "Password1!");
        ResponseEntity<String> me = auth.getForEntity(url("/api/users/me"), String.class);
        assertThat(me.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(me.getBody()).contains("me@example.com");
    }
}


