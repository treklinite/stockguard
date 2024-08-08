#FROM openjdk:17
FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port Spring Boot application runs on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]