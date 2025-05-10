# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the application JAR file
COPY target/rss-producer-0.0.1-SNAPSHOT.jar rss-producer.jar

# Copy the application.yml from resources directory
COPY src/main/resources/application.yml /app/config/application.yml

# Expose Kafka producer port (if applicable)
EXPOSE 8080

# Environment variables (optional, can be set via docker run as well)
ENV SPRING_CONFIG_LOCATION=/app/config/application.yml
ENV RSS_URL=https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml
ENV POLL_INTERVAL=300000
ENV KAFKA_BROKER=localhost:39092,localhost:39093,localhost:39094
ENV KAFKA_TOPIC=nyt.rss.articles

# Add metadata
LABEL maintainer="Natera@natera.com"
LABEL description="RSS Producer Service"

# Copy the entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Run the application
ENTRYPOINT ["/entrypoint.sh"]
