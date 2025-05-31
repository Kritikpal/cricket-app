# Use a minimal JDK runtime image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/live_score-0.0.1.jar app.jar

# Expose the port your app runs on (adjust if different)
EXPOSE 8080

# Set environment profile if needed (optional)
# ENV SPRING_PROFILES_ACTIVE=prod

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
