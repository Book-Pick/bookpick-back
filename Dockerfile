# 1단계: 빌드 스테이지 (Java 17)
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

# 2단계: 런타임 스테이지 (Java 17 또는 21 둘 다 가능)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
