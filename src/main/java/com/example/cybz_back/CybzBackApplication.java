package com.example.cybz_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement  // 开启基于注解事务功能
public class CybzBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CybzBackApplication.class, args);
    }

}
