package com.codeit.blob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class BlobApplication implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(BlobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .forEach(s -> log.info(s));
    }
}
