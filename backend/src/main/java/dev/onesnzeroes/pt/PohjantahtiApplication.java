package dev.onesnzeroes.pt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class PohjantahtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PohjantahtiApplication.class, args);
    }
}
