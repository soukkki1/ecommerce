# Use a minimal Java image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file to the container
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
