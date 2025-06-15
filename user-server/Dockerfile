# 使用多阶段构建
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY common/pom.xml common/
COPY user-server/pom.xml user-server/
# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY common/src common/src
COPY user-server/src user-server/src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/user-server/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 