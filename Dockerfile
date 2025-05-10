# Use a multi-stage build to keep the final image size small

# Stage 1: Build the application
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar rss-producer.jar

# Expose port 8080 for the application
EXPOSE 6080

# Set environment variables for Redis
ENV KAFKA_BROKER=localhost:39092,localhost:39093,localhost:39094
# Run the application
ENTRYPOINT ["java", "-jar", "rss-producer.jar"]
