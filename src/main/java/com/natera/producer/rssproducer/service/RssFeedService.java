package com.natera.producer.rssproducer.service;

import com.natera.producer.rssproducer.exceptions.RssProcessingException;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RssFeedService {
    private static final Logger logger = LoggerFactory.getLogger(RssFeedService.class);

    // Define a DateTimeFormatter for ISO 8601 with timezone
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            .withZone(ZoneId.of("UTC"));


    public List<Map<String, String>> fetchRssFeed(String rssUrl) {
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(rssUrl)));
            return feed.getEntries().stream()
                    .map(this::mapSyndEntryToMap)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Exception occurred: ", e.getMessage());
            throw new RssProcessingException("Failed to fetch RSS feed: " + e.getMessage(), e);
        }
    }

    private Map<String, String> mapSyndEntryToMap(SyndEntry entry) {
        String link = Optional.ofNullable(entry.getLink()).orElse("No Link");

        // Format publishedDate to ISO 8601 format
        String formattedDate = Optional.ofNullable(entry.getPublishedDate())
                .map(date -> DATE_FORMATTER.format(date.toInstant()))
                .orElse(null);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", link);
        resultMap.put("title", Optional.ofNullable(entry.getTitle()).orElse("No Title"));
        resultMap.put("link", link);
        resultMap.put("publishedDate", formattedDate);
        extractMediaFromForeignMarkup(resultMap, entry);
        resultMap.put("description", Optional.ofNullable(entry.getDescription())
                .map(SyndContent::getValue).orElse("No Description"));

        return resultMap;
    }

    public void extractMediaFromForeignMarkup(Map<String, String> resultMap, SyndEntry entry) {
        List<Element> foreignMarkup = entry.getForeignMarkup();

        for (Element element : foreignMarkup) {
            String tagName = element.getName();
            // Example: Extracting media content
            if ("content".equals(tagName) || "thumbnail".equals(tagName) || "image".equals(tagName)) {
                String mediaUrl = element.getAttributeValue("url");
                resultMap.put("mediaUrl", mediaUrl);
            }
        }
    }
}
