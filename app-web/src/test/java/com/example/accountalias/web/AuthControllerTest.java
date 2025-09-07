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
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) { return "http://localhost:" + port + path; }

    @Test
    void register_then_conflict_on_duplicate() {
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("test@example.com", "Password1!");
        ResponseEntity<String> created = rest.postForEntity(url("/api/auth/register"), req, String.class);
        assertThat(created.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.OK, HttpStatus.CONFLICT);

        ResponseEntity<String> conflict = rest.postForEntity(url("/api/auth/register"), req, String.class);
        assertThat(conflict.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflict.getBody()).contains("User already registered");
    }

    @Test
    void login_positive_and_negative() {
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("login2@example.com", "Password1!");
        rest.postForEntity(url("/api/auth/register"), req, String.class);

        // Test login with correct credentials using basic auth instead
        TestRestTemplate auth = rest.withBasicAuth("login2@example.com", "Password1!");
        ResponseEntity<String> me = auth.getForEntity(url("/api/users/me"), String.class);
        assertThat(me.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Test login with wrong credentials
        TestRestTemplate wrongAuth = rest.withBasicAuth("login2@example.com", "wrong");
        ResponseEntity<String> unauthorized = wrongAuth.getForEntity(url("/api/users/me"), String.class);
        assertThat(unauthorized.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}


