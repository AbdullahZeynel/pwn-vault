package com.redteam.vulndb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Red Team Vulnerability Database.
 */
@SpringBootApplication
public class VulnDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(VulnDbApplication.class, args);
    }
}
