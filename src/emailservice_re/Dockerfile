# 构建阶段
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:21.0.4_7-jre-alpine@sha256:8cc1202a100e72f6e91bf05ab274b373a5def789ab6d9e3e293a61236662ac27
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]