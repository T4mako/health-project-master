# 使用 Maven 镜像作为构建阶段
FROM maven:3.8-openjdk-17 AS build

# 设置工作目录
WORKDIR /app

# 将整个项目复制到容器中
COPY . /app

# 构建项目并跳过测试（可以根据需要移除 -DskipTests）
RUN mvn clean package -DskipTests

# 使用较小的 OpenJDK 运行时镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将构建好的 JAR 文件从构建阶段复制到运行时镜像
COPY --from=build /app/target/health-project-master-0.0.1-SNAPSHOT.jar /app/health-project-master.jar

# 公开容器的 8081 端口
EXPOSE 8081

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "/app/health-project-master.jar"]
