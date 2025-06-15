# 使用多阶段构建
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# 复制整个项目
COPY . .

# 先构建 common 模块
RUN mvn clean install -DskipTests -pl common

# 构建 todo-server
RUN mvn clean package -DskipTests -pl todo-server

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/todo-server/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 