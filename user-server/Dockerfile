# 使用多阶段构建
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# 复制整个项目
COPY . .

# 构建项目
RUN mvn clean package -DskipTests -pl user-server

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/user-server/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 