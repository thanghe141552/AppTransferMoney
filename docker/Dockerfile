# Use official OpenJDK image as base
FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy built JAR file into container
COPY target/app.jar app.jar

# Expose port (matches Spring Boot default)
EXPOSE 8088

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]