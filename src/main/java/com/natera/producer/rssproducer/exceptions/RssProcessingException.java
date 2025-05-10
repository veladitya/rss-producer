package com.natera.producer.rssproducer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RssProcessingException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(RssProcessingException.class);

    public RssProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
