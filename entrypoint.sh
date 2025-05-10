#!/bin/bash

echo "Starting RSS Producer..."
java -jar rss-producer.jar \
  --spring.config.location=${SPRING_CONFIG_LOCATION} \
  --rss.url=${RSS_URL} \
  --rss.polling.interval=${POLL_INTERVAL} \
  --spring.kafka.bootstrap-servers=${KAFKA_BROKER} \
  --spring.kafka.template.default-topic=${KAFKA_TOPIC}
