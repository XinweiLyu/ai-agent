# AI Agent 后端应用

基于 Spring Boot 和 Spring AI 框架构建的智能体应用，提供 AI 聊天、RAG 检索增强、工具调用等能力。

## 项目介绍

本项目是一个功能丰富的 AI Agent 应用，主要包含两个核心应用：

- **健康顾问（LoveApp）**：专业的健康咨询助手（预防 / 治疗 / 康复场景），支持多轮对话记忆、RAG 知识库增强、工具调用等功能；后端类名与 HTTP 路径仍为 `LoveApp`、`/ai/love_app/...`
- **超级智能体（Manus）**：基于 ReAct 模式的自主规划智能体，能够自主选择和使用工具完成复杂任务

### 技术栈

- **框架**：Spring Boot 3.5.9
- **AI 框架**：Spring AI 1.0.0-M6
- **AI 模型**：
  - DashScope（阿里云百炼）- 默认使用
  - Ollama（本地模型）- 可选
- **Java 版本**：21
- **构建工具**：Maven
- **向量存储**：
  - SimpleVectorStore（本地内存）
  - PgVector（PostgreSQL 向量扩展）
- **文档处理**：支持 Markdown、PDF、HTML 等多种格式

## 快速开始

### 环境要求

- JDK 21 或更高版本
- Maven 3.6+
- （可选）PostgreSQL 数据库（如使用 PgVector）

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd ai-agent
```

2. **配置应用**

复制配置文件并修改：

```bash
# 复制本地配置文件
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
```

编辑 `application-local.yml`，配置必要的参数：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-dashscope-api-key
      chat:
        options:
          model: qwen-plus
```

3. **构建项目**

```bash
mvn clean package -DskipTests
```

### 启动方式

#### 方式一：IDE 启动（推荐，适合开发测试）

**IntelliJ IDEA：**

1. 打开项目，确保项目 SDK 为 Java 21
   - `File` → `Project Structure` → `Project` → `SDK` 选择 Java 21
2. 找到主启动类：`src/main/java/com/xinwei/aiagent/AiAgentApplication.java`
3. 右键点击主类 → `Run 'AiAgentApplication'`
4. 或直接点击类名旁边的绿色运行按钮

**Eclipse：**

1. 右键项目 → `Run As` → `Java Application`
2. 选择 `AiAgentApplication` 主类

**VS Code：**

1. 安装 Java Extension Pack
2. 打开 `AiAgentApplication.java`
3. 点击 `Run` 按钮或按 `F5`

**注意事项：**
- 确保 IDE 中配置的 Java 版本为 21 或更高
- 首次运行可能需要等待依赖下载和编译

#### 方式二：Maven 启动（开发环境）

```bash
mvn spring-boot:run
```

**或使用 Maven Wrapper（无需安装 Maven）：**

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

#### 方式三：JAR 包启动（生产环境）

```bash
java -jar target/ai-agent-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### 方式四：指定配置文件启动

```bash
java -jar target/ai-agent-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### 访问地址

- **应用地址**：http://localhost:8123/api
- **API 文档**：http://localhost:8123/api/swagger-ui.html
- **健康检查**：http://localhost:8123/api/health

## 项目结构

```
src/main/java/com/xinwei/aiagent/
├── AiAgentApplication.java          # 主启动类
├── controller/                      # 控制器层
│   ├── AiController.java           # AI 相关接口
│   └── HealthController.java       # 健康检查接口
├── app/                             # 应用层
│   └── LoveApp.java                # 健康顾问聊天应用
├── agent/                           # 智能体层
│   └── model/
│       ├── BaseAgent.java          # 基础智能体
│       ├── ReActAgent.java         # ReAct 模式抽象类
│       ├── ToolCallAgent.java      # 工具调用智能体
│       └── Manus.java              # 超级智能体实现
├── tools/                           # 工具层
│   ├── ToolRegistration.java       # 工具注册配置
│   ├── FileOperationTool.java      # 文件操作工具
│   ├── WebSearchTool.java          # 网络搜索工具
│   ├── WebScrapingTool.java        # 网页抓取工具
│   ├── ResourceDownloadTool.java   # 资源下载工具
│   ├── TerminalOperationTool.java  # 终端操作工具
│   ├── PDFGenerationTool.java      # PDF 生成工具
│   └── TerminateTool.java          # 终止工具
├── rag/                             # RAG 检索增强层
│   ├── LoveAppVectorStoreConfig.java        # 本地向量存储配置
│   ├── PgVectorVectorStoreConfig.java      # PgVector 配置
│   ├── LoveAppRagCloudAdvisorConfig.java   # 云知识库 RAG 配置
│   ├── LoveAppRagCustomAdvisorFactory.java # 自定义 RAG 工厂
│   ├── LoveAppDocumentLoader.java          # 文档加载器
│   ├── MyTokenTextSplitter.java            # 文本切分器
│   └── MyKeywordEnricher.java              # 关键词增强器
├── chatmemory/                      # 对话记忆层
│   └── FileBasedChatMemory.java    # 文件持久化记忆
├── advisor/                         # Advisor 增强层
│   ├── MyLoggerAdvisor.java        # 日志 Advisor
│   └── ReReadingAdvisor.java       # 重读 Advisor
└── config/                          # 配置层
    └── CorsConfig.java              # CORS 配置
```

## 架构说明

### 整体架构

```
┌─────────────┐
│  前端应用    │
└──────┬──────┘
       │ HTTP/SSE
┌──────▼─────────────────────────────────────┐
│          Spring Boot 应用层                 │
│  ┌──────────────────────────────────────┐  │
│  │        Controller 层                │  │
│  │  - AiController                     │  │
│  │  - HealthController                 │  │
│  └───────────┬────────────────────────┘  │
│              │                             │
│  ┌───────────▼────────────────────────┐  │
│  │        Application 层              │  │
│  │  - LoveApp (健康顾问)              │  │
│  │  - Manus (超级智能体)              │  │
│  └───────────┬────────────────────────┘  │
│              │                             │
│  ┌───────────▼────────────────────────┐  │
│  │     Spring AI ChatClient          │  │
│  │  - ChatModel (DashScope/Ollama)    │  │
│  │  - Advisors (记忆、日志、RAG)      │  │
│  │  - Tools (工具调用)                │  │
│  └────────────────────────────────────┘  │
│                                           │
│  ┌────────────────────────────────────┐  │
│  │     支持组件                       │  │
│  │  - VectorStore (向量存储)         │  │
│  │  - ChatMemory (对话记忆)          │  │
│  │  - MCP Client (MCP 服务)          │  │
│  └────────────────────────────────────┘  │
└───────────────────────────────────────────┘
```

### 核心组件

1. **ChatClient**：Spring AI 提供的统一聊天客户端，封装了与 AI 模型的交互
2. **Advisor**：增强器，在对话前后进行处理（记忆管理、RAG 检索、日志记录等）
3. **VectorStore**：向量存储，用于 RAG 检索增强
4. **ChatMemory**：对话记忆，管理多轮对话上下文
5. **ToolCallback**：工具回调，允许 AI 调用外部工具

## 功能特性

### 1. 健康顾问（LoveApp）

面向一般健康咨询场景的助手（非替代诊疗），支持多种对话模式。

#### 核心功能

- ✅ **多轮对话记忆**：基于文件的持久化记忆，支持会话上下文管理
- ✅ **SSE 流式传输**：实时流式返回 AI 响应，提升用户体验
- ✅ **RAG 检索增强**：支持多种 RAG 实现方式，从知识库检索相关信息
- ✅ **工具调用**：AI 可以调用外部工具完成复杂任务
- ✅ **MCP 服务集成**：支持通过 MCP 协议调用外部服务

#### 支持的对话模式

1. **同步对话**：`doChat()` - 返回完整响应
2. **SSE 流式对话**：`doChatByStream()` - 实时流式返回
3. **RAG 增强对话**：`doChatWithRag()` - 基于知识库检索增强
4. **工具调用对话**：`doChatWithTools()` - AI 可调用工具
5. **MCP 服务对话**：`doChatWithMcp()` - 通过 MCP 调用服务

### 2. 超级智能体（Manus）

基于 ReAct（Reasoning and Acting）模式的自主规划智能体。

#### 核心功能

- ✅ **自主规划**：AI 自主分析任务并制定执行计划
- ✅ **工具选择**：智能选择最合适的工具组合
- ✅ **多步执行**：支持最多 20 步的复杂任务执行
- ✅ **上下文管理**：自动维护执行上下文和工具调用结果

#### 可用工具

1. **FileOperationTool**：文件读写操作
2. **WebSearchTool**：网络搜索
3. **WebScrapingTool**：网页内容抓取
4. **ResourceDownloadTool**：资源下载
5. **TerminalOperationTool**：终端命令执行
6. **PDFGenerationTool**：PDF 文档生成
7. **TerminateTool**：任务终止

## 核心流程和逻辑

### 1. 健康顾问 LoveApp 流程和逻辑

#### 初始化流程

```java
1. 创建 ChatMemory（文件持久化或内存）
   └─ FileBasedChatMemory: 使用 Kryo 序列化，存储到 tmp/chat-memories/
   
2. 构建 ChatClient
   └─ 设置系统提示词（健康顾问角色）
   └─ 添加默认 Advisors：
      ├─ MessageChatMemoryAdvisor（记忆管理）
      └─ MyLoggerAdvisor（日志记录）
   
3. 注入依赖
   └─ VectorStore（RAG 使用）
   └─ ToolCallback[]（工具调用使用）
   └─ ToolCallbackProvider（MCP 服务使用）
```

#### 对话流程

```
用户消息
    ↓
ChatClient.prompt()
    ↓
Advisor 增强处理
    ├─ MessageChatMemoryAdvisor: 加载历史对话（最后10条）
    ├─ MyLoggerAdvisor: 记录日志
    └─ QuestionAnswerAdvisor: RAG 检索（如启用）
    ↓
AI 模型处理（DashScope/Ollama）
    ↓
生成响应
    ↓
保存到 ChatMemory
    ↓
返回响应（同步/流式）
```

#### 记忆管理机制

- **存储方式**：基于文件的持久化存储（`FileBasedChatMemory`）
- **序列化**：使用 Kryo 进行高效序列化
- **存储路径**：`{user.dir}/tmp/chat-memories/{chatId}.kryo`
- **检索策略**：每次对话检索最后 N 条消息（默认 10 条）
- **切换方式**：可在 `LoveApp` 构造函数中切换为 `InMemoryChatMemory`

#### Advisor 机制

- **MessageChatMemoryAdvisor**：自动管理对话记忆，加载和保存历史消息
- **MyLoggerAdvisor**：记录对话日志，便于调试和监控
- **ReReadingAdvisor**：增强推理能力（可选，需手动启用）
- **QuestionAnswerAdvisor**：RAG 检索增强，从向量存储检索相关信息

### 2. 超级智能体 Manus 流程和逻辑

#### 架构层次

```
Manus
  ↓ extends
ToolCallAgent
  ↓ extends
ReActAgent
  ↓ extends
BaseAgent
```

#### ReAct 执行流程

```
用户请求
    ↓
┌─────────────────┐
│  Think 阶段      │
│  1. 分析任务     │
│  2. 选择工具     │
│  3. 准备参数     │
└────────┬────────┘
         │
   需要工具？
    ├─ 是 → Act 阶段
    └─ 否 → 返回结果
         │
┌────────▼────────┐
│  Act 阶段       │
│  1. 执行工具调用 │
│  2. 获取结果     │
│  3. 更新上下文   │
└────────┬────────┘
         │
    任务完成？
    ├─ 是 → 返回最终结果
    └─ 否 → 继续 Think 阶段（最多20步）
```

#### Think 阶段详细流程

```java
1. 添加 NextStepPrompt 到消息上下文
   └─ 提示 AI 分析任务并选择工具
   
2. 调用 AI 模型
   └─ 传入系统提示词和可用工具列表
   └─ AI 返回思考结果和工具调用请求
   
3. 解析工具调用
   └─ 提取要调用的工具名称和参数
   └─ 记录到 toolCallChatResponse
   
4. 判断是否需要执行
   └─ 有工具调用 → 返回 true（进入 Act）
   └─ 无工具调用 → 返回 false（任务完成）
```

#### Act 阶段详细流程

```java
1. 工具调用执行
   └─ ToolCallingManager.executeToolCalls()
   └─ 根据工具名称和参数执行对应工具
   
2. 更新消息上下文
   └─ 添加 AssistantMessage（AI 的思考）
   └─ 添加 ToolResponseMessage（工具执行结果）
   
3. 检查终止条件
   └─ 调用 terminate 工具 → 设置状态为 FINISHED
   └─ 达到最大步数（20步）→ 自动终止
   
4. 返回执行结果
   └─ 继续下一轮 Think-Act 循环
```

#### 工具调用机制

- **工具注册**：通过 `ToolRegistration` 统一注册所有工具
- **工具选择**：AI 根据任务需求自主选择工具
- **参数解析**：自动解析 AI 返回的工具调用参数
- **结果反馈**：工具执行结果自动添加到上下文，供下一轮思考使用

## 配置说明

### 1. AI 模型配置和切换

#### 配置文件位置

- `src/main/resources/application.yml` - 主配置文件
- `src/main/resources/application-local.yml` - 本地环境配置
- `src/main/resources/application-prod.yml` - 生产环境配置

#### DashScope 配置

```yaml
spring:
  ai:
    dashscope:
      api-key: your-api-key-here
      chat:
        options:
          model: qwen-plus  # 可选: qwen-plus, qwen-max, qwen-turbo 等
```

#### Ollama 配置（可选）

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: gemma3:1b  # 或其他本地模型
```

#### 切换方式

1. **切换模型**：修改配置文件中的 `model` 字段
2. **切换提供商**：注释/取消注释对应配置块
3. **切换环境**：修改 `spring.profiles.active` 或使用启动参数

### 2. Embedding 方法配置

#### 默认配置

- **默认使用**：DashScope Embedding Model（自动注入）
- **向量维度**：1536（DashScope 默认维度）
- **配置位置**：通过 Spring 自动注入 `EmbeddingModel dashscopeEmbeddingModel`

#### 切换方式

如需切换 Embedding 模型，修改以下配置类：

- `LoveAppVectorStoreConfig.java` - 本地向量存储
- `PgVectorVectorStoreConfig.java` - PgVector 向量存储

修改构造函数参数中的 `EmbeddingModel` 类型。

### 3. 向量存储（VectorStore）配置和切换

#### 本地内存存储（默认）

**配置类**：`LoveAppVectorStoreConfig.java`

```java
@Bean
VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
    SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
            .build();
    // ... 加载文档
    return simpleVectorStore;
}
```

**特点**：
- ✅ 无需数据库，启动即用
- ✅ 适合开发和测试
- ❌ 数据不持久化，重启后丢失

#### PgVector 云存储

**配置类**：`PgVectorVectorStoreConfig.java`

**启用步骤**：

1. **配置数据源**（`application.yml`）：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://host:5432/database
    username: your-username
    password: your-password
```

2. **启用 PgVector**：

```yaml
app:
  pgvector:
    enabled: true  # 设置为 true
```

3. **配置向量存储参数**（可选）：

```yaml
spring:
  ai:
    vectorstore:
      pgvector:
        index-type: HNSW              # 索引类型：HNSW 或 IVFFLAT
        dimensions: 1536              # 向量维度
        distance-type: COSINE_DISTANCE # 距离类型：COSINE_DISTANCE 或 L2_DISTANCE
        max-document-batch-size: 10000 # 批量处理大小
```

**切换方式**：
- 启用：设置 `app.pgvector.enabled=true` 并配置数据源
- 禁用：设置 `app.pgvector.enabled=false`（默认）

### 4. RAG 方法配置和切换

#### 配置位置

`LoveApp.java` 的 `doChatWithRag()` 方法（第 146-175 行）

#### 四种 RAG 实现方式

##### 方式 1：本地向量存储 RAG（默认）

```java
.advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
```

**特点**：
- ✅ 使用本地 SimpleVectorStore
- ✅ 无需额外配置
- ✅ 适合小规模知识库

##### 方式 2：云知识库 RAG

```java
.advisors(loveAppQuestionAnswerAdvisor)
```

**配置步骤**：

1. 确保 `LoveAppRagCloudAdvisorConfig` 已配置
2. 在 `LoveAppRagCloudAdvisorConfig.java` 中设置知识库索引名称：

```java
final String KNOWLEDGE_INDEX = "健康顾问";  // 与阿里云知识库索引名一致，可按实际修改
```

3. 在 `doChatWithRag()` 中取消注释对应行

**特点**：
- ✅ 使用阿里云知识库服务
- ✅ 支持大规模知识库
- ✅ 需要配置 DashScope API Key

##### 方式 3：PgVector RAG

```java
.advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
```

**前提条件**：
- 启用 PgVector（`app.pgvector.enabled=true`）
- 配置 PostgreSQL 数据源

**特点**：
- ✅ 数据持久化
- ✅ 支持大规模数据
- ✅ 需要 PostgreSQL 数据库

##### 方式 4：自定义 RAG（支持状态过滤）

```java
.advisors(
    LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
        loveAppVectorStore, "康复"  // 状态过滤：预防、治疗、康复（与文档元数据字段 status 一致）
    )
)
```

**配置位置**：`LoveAppRagCustomAdvisorFactory.java`

**可配置参数**：
- `similarityThreshold`：相似度阈值（默认 0.5）
- `topK`：返回文档数量（默认 3）
- `status`：用户状态过滤（预防、治疗、康复）

**特点**：
- ✅ 支持元数据过滤
- ✅ 可自定义检索参数
- ✅ 支持上下文查询增强

#### 切换方式

在 `doChatWithRag()` 方法中，注释/取消注释对应的 `.advisors()` 调用：

```java
// 1.应用 RAG 问答（基于本地知识库）- 默认启用
.advisors(new QuestionAnswerAdvisor(loveAppVectorStore))

// 2.应用 RAG 检索增强服务（基于云知识库服务）- 取消注释启用
//.advisors(loveAppQuestionAnswerAdvisor)

// 3.应用RAG 检索增强服务（基于PgVector云向量存储）- 取消注释启用
//.advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))

// 4.应用自定义 RAG 检索增强服务 - 取消注释启用
//.advisors(
//    LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
//        loveAppVectorStore, "康复"
//    )
//)
```

### 5. 对话记忆（ChatMemory）配置和切换

#### 文件持久化记忆（默认）

**实现类**：`FileBasedChatMemory`

**配置位置**：`LoveApp.java` 构造函数（第 48-50 行）

```java
String fileDir = System.getProperty("user.dir")+"/tmp/chat-memories";
ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
```

**特点**：
- ✅ 数据持久化，重启后保留
- ✅ 使用 Kryo 序列化，性能高效
- ✅ 每个会话单独文件存储

**存储路径**：`{项目根目录}/tmp/chat-memories/{chatId}.kryo`

#### 内存记忆

**实现类**：`InMemoryChatMemory`

**切换方式**：在 `LoveApp` 构造函数中替换：

```java
// 注释文件记忆
// ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

// 启用内存记忆
ChatMemory chatMemory = new InMemoryChatMemory();
```

**特点**：
- ✅ 性能更高
- ❌ 数据不持久化，重启后丢失
- ✅ 适合临时会话

### 6. Advisor 配置和切换

#### 默认 Advisor

**配置位置**：`LoveApp.java` 构造函数（第 56-62 行）

```java
.defaultAdvisors(
    new MessageChatMemoryAdvisor(chatMemory),  // 记忆管理
    new MyLoggerAdvisor()                      // 日志记录
)
```

#### 可选 Advisor

**ReReadingAdvisor**：增强推理能力

**启用方式**：在 `LoveApp` 构造函数中取消注释：

```java
.defaultAdvisors(
    new MessageChatMemoryAdvisor(chatMemory),
    new MyLoggerAdvisor(),
    new ReReadingAdvisor()  // 取消注释启用
)
```

#### RAG Advisor

在 `doChatWithRag()` 方法中动态添加，见 [RAG 方法配置](#4-rag-方法配置和切换)。

### 7. MCP 服务配置

#### SSE 方式（推荐）

**配置位置**：`application.yml`

```yaml
spring:
  ai:
    mcp:
      client:
        sse:
          connections:
            server1:
              url: http://localhost:8127  # MCP 服务器地址
```

#### Stdio 方式

**配置位置**：`application.yml`

```yaml
spring:
  ai:
    mcp:
      client:
        stdio:
          servers-configuration: classpath:mcp-servers.json
```

**配置文件**：`src/main/resources/mcp-servers.json`

```json
{
  "servers": {
    "image-search": {
      "command": "java",
      "args": ["-jar", "path/to/image-search-mcp-server.jar"]
    }
  }
}
```

#### 切换方式

注释/取消注释对应配置块即可切换连接方式。

### 8. 工具注册配置

#### 配置位置

`ToolRegistration.java`

#### 添加新工具

1. **创建工具类**：实现 `ToolCallback` 接口或使用 `@Tool` 注解

2. **注册工具**：在 `allTools()` 方法中添加：

```java
@Bean
public ToolCallback[] allTools() {
    // ... 现有工具
    YourNewTool yourNewTool = new YourNewTool();
    return ToolCallbacks.from(
        // ... 现有工具
        yourNewTool  // 添加新工具
    );
}
```

#### Search API 配置

**配置文件**：`application.yml`

```yaml
search-api:
  api-key: your-search-api-key
```

**用途**：`WebSearchTool` 使用此 API Key 进行网络搜索。

### 9. 文档处理配置

#### 文档加载器

**配置类**：`LoveAppDocumentLoader`

**功能**：加载 Markdown 文档到向量存储

**文档路径**：`src/main/resources/document/`（默认）

#### 文本切分器

**配置类**：`MyTokenTextSplitter`

**启用位置**：`LoveAppVectorStoreConfig.java`（第 35 行）

```java
// 取消注释启用自定义切分
List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
```

#### 关键词增强

**配置类**：`MyKeywordEnricher`

**状态**：默认启用

**功能**：自动为文档补充关键词元信息，提升检索效果

**配置位置**：`LoveAppVectorStoreConfig.java`（第 37 行）

```java
List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
```

## API 文档

### 基础信息

- **Base URL**：`http://localhost:8123/api`
- **API 文档**：`http://localhost:8123/api/swagger-ui.html`
- **健康检查**：`GET /health`

### 健康顾问接口（URL 仍为 `/ai/love_app/...`）

#### 1. 同步对话

```http
GET /ai/love_app/chat/sync?message={message}&chatId={chatId}
```

**参数**：
- `message`（必需）：用户消息
- `chatId`（必需）：会话 ID

**响应**：完整文本响应

#### 2. SSE 流式对话（推荐）

```http
GET /ai/love_app/chat/sse?message={message}&chatId={chatId}
```

**参数**：
- `message`（必需）：用户消息
- `chatId`（必需）：会话 ID

**响应**：Server-Sent Events 流式响应

**前端示例**：

```javascript
const eventSource = new EventSource(
  `/api/ai/love_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${chatId}`
);

eventSource.onmessage = (event) => {
  console.log('收到数据:', event.data);
};
```

#### 3. Server-SentEvent 格式

```http
GET /ai/love_app/chat/server_sent_event?message={message}&chatId={chatId}
```

**响应**：标准 SSE 格式的流式响应

#### 4. SseEmitter 格式

```http
GET /ai/love_app/chat/sse_emitter?message={message}&chatId={chatId}
```

**特点**：支持更复杂的错误处理和完成事件

### 超级智能体接口

#### 流式对话

```http
GET /ai/manus/chat?message={message}
```

**参数**：
- `message`（必需）：用户请求

**响应**：SseEmitter 流式响应，包含 AI 思考和工具执行过程

**前端示例**：

```javascript
const eventSource = new EventSource(
  `/api/ai/manus/chat?message=${encodeURIComponent(message)}`
);

eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('思考:', data.thought);
  console.log('工具调用:', data.toolCalls);
  console.log('执行结果:', data.results);
};
```

## 部署说明

### Docker 部署

1. **构建镜像**：

```bash
mvn clean package -DskipTests
docker build -t ai-agent:latest .
```

2. **运行容器**：

```bash
docker run -d \
  -p 8123:8123 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_AI_DASHSCOPE_API_KEY=your-api-key \
  ai-agent:latest
```

### 环境变量配置

支持通过环境变量覆盖配置：

- `SPRING_PROFILES_ACTIVE`：激活的配置文件
- `SPRING_AI_DASHSCOPE_API_KEY`：DashScope API Key
- `SPRING_DATASOURCE_URL`：数据库连接 URL
- `APP_PGVECTOR_ENABLED`：是否启用 PgVector

## 常见问题

### 1. 如何切换 AI 模型？

修改配置文件中的 `spring.ai.dashscope.chat.options.model` 或 `spring.ai.ollama.chat.model`。

### 2. 如何启用 PgVector？

1. 配置 PostgreSQL 数据源
2. 设置 `app.pgvector.enabled=true`
3. 重启应用

### 3. 如何添加新的工具？

1. 创建工具类实现 `ToolCallback` 接口
2. 在 `ToolRegistration.java` 中注册
3. 重启应用

### 4. 对话记忆存储在哪里？

默认存储在 `{项目根目录}/tmp/chat-memories/` 目录下，每个会话一个 `.kryo` 文件。

### 5. 如何切换 RAG 实现方式？

在 `LoveApp.java` 的 `doChatWithRag()` 方法中，注释/取消注释对应的 `.advisors()` 调用。

## 许可证

本项目仅用于学习与演示。

## 贡献

欢迎提交 Issue 和 Pull Request。
