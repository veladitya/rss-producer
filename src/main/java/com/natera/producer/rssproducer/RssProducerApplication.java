package com.natera.producer.rssproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RssProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssProducerApplication.class, args);
    }

}
