# 1단계: 빌드 스테이지 (무조건 성공하는 조합)
FROM --platform=linux/amd64 gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY . .

RUN sudo chmod +x ./gradlew
RUN sudo gradle clean bootJar --no-daemon

# 2단계: 런타임 스테이지
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
