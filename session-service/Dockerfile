FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/session-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9096
ENTRYPOINT ["java", "-jar", "app.jar"]