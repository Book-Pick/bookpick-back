
# 1단계: 빌드 스테이지
FROM --platform=linux/amd64 gradle:8.5-jdk21-alpine AS builder

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew

# Gradle 캐시 최적화 & JAR 만들기
RUN ./gradlew clean bootJar --no-daemon

# 2단계: 런타임 스테이지
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine

WORKDIR /app

# builder에서 jar만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
