package dev.prithwish.ledgerlux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LedgerLuxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LedgerLuxApplication.class, args);
    }

}
