# =========================
# Stage 1: Build
# =========================
FROM maven:3.9.4-eclipse-temurin-20 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости (кэшируем слои)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходники
COPY src ./src

RUN mvn spotless:apply

# Сборка приложения
RUN mvn clean package -DskipTests

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:20-jre-alpine

# Рабочая директория
WORKDIR /app

# Копируем JAR из стадии сборки
COPY --from=build /app/target/*.jar app.jar

# Прокидываем переменные окружения (можно через .env при запуске docker-compose)
ENV JAVA_OPTS=""

# Экспонируем порт приложения
EXPOSE 8080
EXPOSE 9090

# Команда запуска
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
