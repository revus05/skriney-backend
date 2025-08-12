FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /workspace/target/skriney-backend-0.0.1-SNAPSHOT.jar app.jar
# Если хотите, чтобы файл .env был в контейнере:
COPY .env .env
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]