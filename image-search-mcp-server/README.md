# Image Search MCP Server

基于 Spring Boot 和 Spring AI MCP Server 框架构建的图片搜索 MCP 服务器，提供图片搜索工具供 AI Agent 调用。

## 项目介绍

这是一个 Model Context Protocol (MCP) 服务器实现，专门提供图片搜索功能。通过 MCP 协议，主应用（AI Agent）可以调用此服务器提供的图片搜索工具，实现 AI 驱动的图片搜索能力。

### 核心功能

- ✅ **图片搜索工具**：提供 `searchImage` 工具，支持关键词搜索图片
- ✅ **Pexels API 集成**：使用 Pexels API 进行图片搜索
- ✅ **MCP 协议支持**：支持 SSE（Server-Sent Events）和 Stdio 两种连接方式
- ✅ **自动端点注册**：Spring AI MCP Server 自动注册工具和端点

### 技术栈

- **框架**：Spring Boot 3.3.8
- **MCP 框架**：Spring AI MCP Server 1.0.0-M6
- **工具库**：Hutool 5.8.40
- **Java 版本**：21
- **构建工具**：Maven

## 快速开始

### 环境要求

- JDK 21 或更高版本
- Maven 3.6+
- Pexels API Key（需要从 [Pexels](https://www.pexels.com/api/) 申请）

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd image-search-mcp-server
```

2. **配置 Pexels API Key**

编辑 `src/main/java/com/xinwei/imagesearchmcpserver/tools/ImageSearchTool.java`：

```java
private static final String API_KEY = "your-pexels-api-key-here";
```

或通过环境变量配置（推荐）：

```java
@Value("${pexels.api-key}")
private String apiKey;
```

3. **构建项目**

```bash
mvn clean package -DskipTests
```

### 启动方式

#### SSE 方式（推荐，用于 HTTP 连接）

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=sse
```

或使用 JAR：

```bash
java -jar target/image-search-mcp-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=sse
```

**访问地址**：http://localhost:8127

#### Stdio 方式（用于进程间通信）

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=stdio
```

或使用 JAR：

```bash
java -jar target/image-search-mcp-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=stdio
```

**特点**：无 Web 服务器，通过标准输入输出通信

## 项目结构

```
image-search-mcp-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/xinwei/imagesearchmcpserver/
│   │   │       ├── ImageSearchMcpServerApplication.java  # 主启动类
│   │   │       └── tools/
│   │   │           └── ImageSearchTool.java            # 图片搜索工具
│   │   └── resources/
│   │       ├── application.yml                         # 主配置文件
│   │       ├── application-sse.yml                    # SSE 配置
│   │       └── application-stdio.yml                 # Stdio 配置
│   └── test/                                           # 测试代码
├── pom.xml                                             # Maven 配置
└── target/                                             # 构建输出
```

### 核心文件说明

#### `ImageSearchMcpServerApplication.java`

主启动类，负责：
- 启动 Spring Boot 应用
- 注册 `ToolCallbackProvider` Bean
- 配置 MCP 服务器

#### `ImageSearchTool.java`

图片搜索工具实现：
- `searchImage()`：MCP 工具方法，供 AI 调用
- `searchMediumImages()`：内部方法，执行实际的 API 调用

## 架构说明

### MCP 服务器架构

```
┌─────────────────────────────────────┐
│     主应用 (AI Agent)                │
│  ┌───────────────────────────────┐  │
│  │   MCP Client                  │  │
│  │   - SSE 连接                  │  │
│  │   - Stdio 连接                │  │
│  └───────────┬───────────────────┘  │
└──────────────┼──────────────────────┘
               │ MCP Protocol
┌──────────────▼──────────────────────┐
│   Image Search MCP Server            │
│  ┌───────────────────────────────┐  │
│  │   Spring AI MCP Server        │  │
│  │   - 自动注册工具               │  │
│  │   - 提供 SSE/Stdio 端点        │  │
│  └───────────┬───────────────────┘  │
│              │                       │
│  ┌───────────▼───────────────────┐  │
│  │   ImageSearchTool             │  │
│  │   - searchImage()             │  │
│  └───────────┬───────────────────┘  │
└──────────────┼───────────────────────┘
               │ HTTP API
┌──────────────▼───────────────────────┐
│      Pexels API                      │
│   https://api.pexels.com/v1/search  │
└──────────────────────────────────────┘
```

### 工具注册流程

```
1. Spring Boot 启动
    ↓
2. ImageSearchTool 被扫描为 @Service
    ↓
3. ImageSearchMcpServerApplication 创建 ToolCallbackProvider
    ↓
4. Spring AI MCP Server 自动注册工具
    ↓
5. 工具通过 MCP 协议暴露给客户端
```

## 功能特性

### 图片搜索工具

#### 工具名称

`searchImage`

#### 工具描述

"search image from web"

#### 参数

- `query`（String，必需）：搜索关键词

#### 返回值

返回图片 URL 列表（逗号分隔的字符串）

#### 使用示例

AI Agent 调用示例：

```json
{
  "tool": "searchImage",
  "arguments": {
    "query": "sunset beach"
  }
}
```

返回示例：

```
https://images.pexels.com/photos/123456/pexels-photo-123456.jpeg,
https://images.pexels.com/photos/789012/pexels-photo-789012.jpeg,
...
```

### Pexels API 集成

- **API 端点**：`https://api.pexels.com/v1/search`
- **认证方式**：API Key（通过 Authorization 头）
- **返回格式**：JSON
- **图片尺寸**：中等尺寸（medium）

## 配置说明

### 主配置文件

**application.yml**：

```yaml
spring:
  application:
    name: image-search-mcp-server
  profiles:
    active: sse  # 默认使用 SSE 方式
server:
  port: 8127     # 服务器端口
```

### SSE 配置

**application-sse.yml**：

```yaml
spring:
  ai:
    mcp:
      server:
        name: image-search-mcp-server
        version: 0.0.1
        type: SYNC
        stdio: false  # 禁用 Stdio
```

**特点**：
- ✅ 支持 HTTP/SSE 连接
- ✅ 可通过浏览器访问
- ✅ 适合远程调用

### Stdio 配置

**application-stdio.yml**：

```yaml
spring:
  ai:
    mcp:
      server:
        name: image-search-mcp-server
        version: 0.0.1
        type: SYNC
        stdio: true  # 启用 Stdio
  main:
    web-application-type: none  # 禁用 Web 服务器
    banner-mode: off
```

**特点**：
- ✅ 通过标准输入输出通信
- ✅ 无 Web 服务器，资源占用更少
- ✅ 适合本地进程间通信

### Pexels API Key 配置

#### 方式一：硬编码（不推荐）

在 `ImageSearchTool.java` 中直接设置：

```java
private static final String API_KEY = "your-api-key";
```

#### 方式二：配置文件（推荐）

1. 在 `application.yml` 中添加：

```yaml
pexels:
  api-key: your-api-key-here
```

2. 在 `ImageSearchTool.java` 中使用：

```java
@Value("${pexels.api-key}")
private String apiKey;
```

#### 方式三：环境变量（生产环境推荐）

```bash
export PEXELS_API_KEY=your-api-key
```

在代码中读取：

```java
@Value("${PEXELS_API_KEY:default-key}")
private String apiKey;
```

## 集成到主应用

### SSE 方式集成

在主应用的 `application.yml` 中配置：

```yaml
spring:
  ai:
    mcp:
      client:
        sse:
          connections:
            image-search-server:
              url: http://localhost:8127
```

### Stdio 方式集成

在主应用的 `application.yml` 中配置：

```yaml
spring:
  ai:
    mcp:
      client:
        stdio:
          servers-configuration: classpath:mcp-servers.json
```

**mcp-servers.json**：

```json
{
  "servers": {
    "image-search": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/image-search-mcp-server-0.0.1-SNAPSHOT.jar",
        "--spring.profiles.active=stdio"
      ]
    }
  }
}
```

### 在主应用中使用

配置完成后，主应用的 AI Agent 可以自动调用图片搜索工具：

```java
// LoveApp.java 或 Manus.java
public String doChatWithMcp(String message, String chatId) {
    // AI 会自动识别需要使用图片搜索工具
    // 例如：用户说"帮我找一些日落的图片"
    // AI 会自动调用 searchImage("sunset")
    return chatClient.prompt()
            .user(message)
            .tools(toolCallbackProvider)  // 包含 MCP 工具
            .call()
            .chatResponse()
            .getResult()
            .getOutput()
            .getText();
}
```

## API 端点

### MCP 信息端点（SSE 方式）

**端点**：`GET /mcp/info`

**响应**：MCP 服务器信息，包括可用工具列表

### MCP SSE 传输端点

**端点**：`POST /mcp/sse`

**请求体**：MCP 协议消息

**响应**：Server-Sent Events 流式响应

### 健康检查端点

**端点**：`GET /actuator/health`（如果启用了 Actuator）

## 开发指南

### 添加新工具

1. **创建工具类**：

```java
@Service
public class YourNewTool {
    @Tool(description = "工具描述")
    public String yourToolMethod(@ToolParam(description = "参数描述") String param) {
        // 工具实现
        return "结果";
    }
}
```

2. **注册工具**：

在 `ImageSearchMcpServerApplication.java` 中：

```java
@Bean
public ToolCallbackProvider allTools(
    ImageSearchTool imageSearchTool,
    YourNewTool yourNewTool  // 添加新工具
) {
    return MethodToolCallbackProvider.builder()
            .toolObjects(imageSearchTool, yourNewTool)  // 注册所有工具
            .build();
}
```

### 自定义配置

#### 修改端口

在 `application.yml` 中：

```yaml
server:
  port: 9000  # 修改为其他端口
```

#### 修改 MCP 服务器名称

在 `application-sse.yml` 或 `application-stdio.yml` 中：

```yaml
spring:
  ai:
    mcp:
      server:
        name: your-custom-name
```

### 调试技巧

1. **查看工具注册**：启动后访问 `/mcp/info` 端点查看注册的工具
2. **查看日志**：Spring Boot 默认会输出工具注册信息
3. **测试工具**：可以直接调用 `ImageSearchTool.searchImage()` 方法测试

## 部署说明

### Docker 部署

#### 1. 创建 Dockerfile

```dockerfile
FROM openjdk:21-slim
WORKDIR /app
COPY target/image-search-mcp-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8127
ENV SPRING_PROFILES_ACTIVE=sse
ENV PEXELS_API_KEY=your-api-key
CMD ["java", "-jar", "app.jar"]
```

#### 2. 构建和运行

```bash
docker build -t image-search-mcp-server:latest .
docker run -d -p 8127:8127 \
  -e SPRING_PROFILES_ACTIVE=sse \
  -e PEXELS_API_KEY=your-api-key \
  image-search-mcp-server:latest
```

### 生产环境部署

1. **构建 JAR**：

```bash
mvn clean package -DskipTests
```

2. **运行服务**：

```bash
java -jar target/image-search-mcp-server-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=sse \
  --pexels.api-key=your-api-key
```

3. **使用进程管理器**：

使用 systemd、supervisor 或 PM2 管理进程。

## 常见问题

### 1. 工具未注册

**问题**：主应用无法调用图片搜索工具

**解决方案**：
- 检查 MCP 服务器是否启动
- 检查主应用的 MCP 客户端配置
- 查看服务器日志确认工具注册成功

### 2. Pexels API 调用失败

**问题**：返回错误信息

**解决方案**：
- 检查 API Key 是否正确
- 检查网络连接
- 查看 Pexels API 文档确认请求格式

### 3. SSE 连接失败

**问题**：主应用无法连接到 MCP 服务器

**解决方案**：
- 检查服务器是否运行在正确端口
- 检查防火墙设置
- 检查 URL 配置是否正确

### 4. Stdio 方式不工作

**问题**：Stdio 模式下无法通信

**解决方案**：
- 确认使用 `stdio` profile
- 检查 `mcp-servers.json` 配置
- 确认命令路径正确

## 注意事项

1. **API Key 安全**：不要将 API Key 提交到代码仓库，使用环境变量或配置文件（不提交）
2. **端口冲突**：确保端口 8127 未被其他服务占用
3. **网络访问**：SSE 方式需要确保主应用可以访问 MCP 服务器
4. **API 限制**：注意 Pexels API 的调用频率限制
5. **错误处理**：工具方法应包含异常处理，返回友好的错误信息

## 许可证

本项目仅用于学习与演示。

## 贡献

欢迎提交 Issue 和 Pull Request。

## 相关资源

- [Spring AI MCP Server 文档](https://docs.spring.io/spring-ai/reference/)
- [Pexels API 文档](https://www.pexels.com/api/)
- [Model Context Protocol 规范](https://modelcontextprotocol.io/)
