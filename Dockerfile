# 使用预装 Maven 和 JDK21 的镜像
#FROM maven:3.9-amazoncorretto-21
#WORKDIR /app
#
## 只复制必要的源代码和配置文件
#COPY pom.xml .
#COPY src ./src
#
## 使用 Maven 执行打包
#RUN mvn clean package -DskipTests
#
## 暴露应用端口
#EXPOSE 8123
#
## 使用生产环境配置启动应用
#CMD ["java", "-jar", "/app/target/ai-agent-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
#


# 预先构建应用并生成 JAR 文件
# 使用轻量级 JDK21 运行环境
FROM openjdk:21-slim

# 工作目录
WORKDIR /app

# 复制已经打包好的JAR文件（假设已放在当前目录）
COPY target/ai-agent-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8123

# 使用生产环境配置启动应用
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
