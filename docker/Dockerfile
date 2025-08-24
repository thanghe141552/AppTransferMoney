# Use official OpenJDK image as base
FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy built JAR file into container
COPY target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar

# Expose port (matches Spring Boot default)
EXPOSE 8088

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]