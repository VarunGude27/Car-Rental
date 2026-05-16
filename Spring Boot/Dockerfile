# Use OpenJDK base image
FROM openjdk:23-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar
COPY target/Car-Rental-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8000

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
