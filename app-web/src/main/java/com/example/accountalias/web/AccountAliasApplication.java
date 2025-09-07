package com.example.accountalias.web;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.accountalias")
@EnableJpaRepositories(basePackages = "com.example.accountalias.repository")
@EntityScan(basePackages = "com.example.accountalias.domain")
public class AccountAliasApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountAliasApplication.class, args);
    }
}

