FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/user-service.jar app.jar

EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]