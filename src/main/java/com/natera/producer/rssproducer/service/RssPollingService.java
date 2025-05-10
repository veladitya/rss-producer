package com.natera.producer.rssproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RssPollingService {
    private static final Logger logger = LoggerFactory.getLogger(RssPollingService.class);


    @Value("${rss.url: https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml}")
    private String RSS_URL = "https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml";
    @Autowired
    private RssFeedService rssFeedService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Scheduled(fixedRateString = "${rss.polling.interval:300000}")
    public void pollRssFeed() {
        List<Map<String, String>> items = rssFeedService.fetchRssFeed(RSS_URL);
        items.forEach(item -> {
            String articleId = item.getOrDefault("id", "unknown");
            try {
                kafkaProducerService.sendMessage(articleId, item);
            } catch (JsonProcessingException e) {
                logger.error("Exception occurred: ", e.getMessage());
                throw new RuntimeException(e);
            }
            logger.info("Published to Kafka with key: " + articleId);
        });
    }
}
