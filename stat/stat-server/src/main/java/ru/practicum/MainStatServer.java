package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MainStatServer {
    public static void main(String[] args) {
        System.setProperty("server.port", "9090");
        SpringApplication.run(MainStatServer.class, args);
    }
}
