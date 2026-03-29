# AI Agent 前端应用

基于 Vue 3 构建的现代化 AI 聊天应用前端，提供流畅的用户体验和实时流式对话功能。

## 项目介绍

这是一个功能丰富的 AI Agent 前端应用，为用户提供友好的界面来与后端 AI 服务交互。主要包含三个核心页面：

- **应用中心（Home）**：应用切换中心，展示所有可用的 AI 应用
- **AI 健康顾问（LoveApp）**：健康场景咨询助手（非替代诊疗），支持实时流式对话和多轮对话记忆；前端路由仍为 `/love-app`，后端路径为 `/ai/love_app/...`
- **AI 超级智能体（ManusApp）**：强大的 AI 助手，能够处理各种复杂任务，展示 AI 的思考和工具执行过程

### 技术栈

- **框架**：Vue 3.4.0（Composition API）
- **路由**：Vue Router 4.2.5
- **HTTP 客户端**：Axios 1.6.0
- **构建工具**：Vite 5.0.0
- **开发服务器**：Vite Dev Server
- **生产部署**：Nginx（Docker）

### 核心特性

- ✅ **实时流式对话**：使用 Server-Sent Events (SSE) 实现实时流式响应
- ✅ **现代化 UI**：深色主题，优雅的界面设计
- ✅ **响应式布局**：支持桌面和移动端
- ✅ **多应用切换**：统一的应用中心，轻松切换不同 AI 应用
- ✅ **会话管理**：支持多会话，每个会话独立管理
- ✅ **自动滚动**：消息自动滚动到底部
- ✅ **错误处理**：完善的错误处理和用户提示

## 快速开始

### 环境要求

- **Node.js**：18.0+ 或更高版本
- **npm**：9.0+ 或更高版本（或使用 yarn/pnpm）

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd ai-agent-frontend
```

2. **安装依赖**

```bash
npm install
```

### 启动方式

#### 开发环境

```bash
npm run dev
```

项目将在 `http://localhost:3000` 启动，支持热重载。

#### 生产构建

```bash
npm run build
```

构建产物将输出到 `dist/` 目录。

#### 预览生产构建

```bash
npm run preview
```

预览构建后的应用（需要先执行 `npm run build`）。

### 访问地址

- **开发环境**：http://localhost:3000
- **生产环境**：根据部署配置（默认 80 端口）

## 项目结构

```
ai-agent-frontend/
├── src/
│   ├── api/                    # API 接口层
│   │   └── chat.js            # 聊天相关 API（SSE 连接）
│   ├── router/                 # 路由配置
│   │   └── index.js           # 路由定义（3个路由）
│   ├── utils/                  # 工具函数
│   │   └── request.js         # Axios 配置和拦截器
│   ├── views/                  # 页面组件
│   │   ├── Home.vue           # 应用中心主页
│   │   ├── LoveApp.vue        # AI 健康顾问页面
│   │   └── ManusApp.vue       # AI 超级智能体页面
│   ├── App.vue                 # 根组件（布局、导航、页脚）
│   └── main.js                 # 应用入口文件
├── index.html                   # HTML 模板
├── package.json                 # 项目配置和依赖
├── vite.config.js              # Vite 配置（代理、端口等）
├── nginx.conf                  # Nginx 配置（生产部署）
├── Dockerfile                  # Docker 构建文件
└── dist/                       # 构建输出目录（构建后生成）
```

### 目录说明

#### `src/api/` - API 接口层

**chat.js**：封装了所有聊天相关的 API 调用

- `doChatWithLoveAppSse()`：健康顾问 SSE 聊天
- `doChatWithManus()`：超级智能体 SSE 聊天

#### `src/router/` - 路由配置

**index.js**：定义了三个路由

- `/`：应用中心（Home）
- `/love-app`：健康顾问（LoveApp，历史路径名）
- `/manus-app`：超级智能体（ManusApp）

#### `src/utils/` - 工具函数

**request.js**：Axios 实例配置

- 基础 URL 配置
- 请求/响应拦截器
- 超时设置（30秒）

#### `src/views/` - 页面组件

**Home.vue**：应用中心页面
- 展示所有可用的 AI 应用
- 提供快速导航入口

**LoveApp.vue**：健康顾问聊天页面
- 聊天界面
- SSE 流式消息接收
- 消息历史管理
- 会话 ID 管理

**ManusApp.vue**：超级智能体页面
- 任务执行界面
- 展示 AI 思考过程
- 展示工具调用过程
- 执行结果展示

## 架构说明

### 整体架构

```
┌─────────────────────────────────────┐
│          Vue 3 应用层                │
│  ┌───────────────────────────────┐  │
│  │      App.vue (根组件)          │  │
│  │  - 导航栏                      │  │
│  │  - 路由视图                    │  │
│  │  - 页脚                        │  │
│  └───────────┬───────────────────┘  │
│              │                       │
│  ┌───────────▼───────────────────┐  │
│  │    Vue Router (路由)           │  │
│  │  - Home                        │  │
│  │  - LoveApp                     │  │
│  │  - ManusApp                    │  │
│  └───────────┬───────────────────┘  │
│              │                       │
│  ┌───────────▼───────────────────┐  │
│  │    API 层 (chat.js)           │  │
│  │  - EventSource (SSE)          │  │
│  │  - API 调用封装               │  │
│  └───────────┬───────────────────┘  │
│              │                       │
│  ┌───────────▼───────────────────┐  │
│  │    HTTP 层 (Axios)            │  │
│  │  - 请求拦截                   │  │
│  │  - 响应拦截                   │  │
│  └───────────┬───────────────────┘  │
└──────────────┼───────────────────────┘
               │ HTTP/SSE
┌──────────────▼───────────────────────┐
│        后端 API 服务                  │
│    http://localhost:8123/api         │
└───────────────────────────────────────┘
```

### 数据流

#### SSE 流式对话流程

```
用户输入消息
    ↓
调用 API (chat.js)
    ↓
创建 EventSource 连接
    ↓
监听 message 事件
    ↓
接收流式数据块
    ↓
更新组件状态
    ↓
渲染到界面
    ↓
自动滚动到底部
```

#### 消息管理流程

```
组件挂载
    ↓
生成/获取 chatId
    ↓
发送消息
    ↓
添加到 messages 数组
    ↓
接收 AI 响应（流式）
    ↓
实时更新最后一条消息
    ↓
完成时标记为完成
    ↓
组件卸载时关闭连接
```

## 配置说明

### 环境变量配置

创建 `.env` 文件（开发环境）或 `.env.production`（生产环境）：

```bash
# API 基础路径
VITE_API_BASE_URL=/api

# API 目标地址（开发环境代理目标）
VITE_API_TARGET=http://localhost:8123
```

### Vite 配置

**vite.config.js**：主要配置项

```javascript
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,                    // 开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8123',  // 后端地址
        changeOrigin: true
      }
    }
  }
})
```

**修改代理目标**：
- 开发环境：修改 `vite.config.js` 中的 `target`
- 生产环境：修改 `nginx.conf` 中的 `proxy_pass`

### Nginx 配置

**nginx.conf**：生产环境配置

主要配置项：
- **静态文件服务**：`/usr/share/nginx/html`
- **API 代理**：`/api/` 路径代理到后端
- **SSE 支持**：配置了 SSE 相关的代理参数
- **路由支持**：所有路由返回 `index.html`（支持 Vue Router）

**修改后端地址**：

```nginx
location ^~ /api/ {
    proxy_pass https://your-backend-url.com/;
    proxy_set_header Host your-backend-host;
    # ... 其他配置
}
```

## API 文档

### 基础配置

- **Base URL**：`/api`（开发环境通过 Vite 代理，生产环境通过 Nginx 代理）
- **协议**：HTTP/HTTPS
- **数据格式**：Server-Sent Events (SSE)

### 接口列表

#### 1. AI 健康顾问 SSE 聊天

**接口地址**：`GET /api/ai/love_app/chat/sse`

**请求参数**：
- `message`（必需）：用户消息内容
- `chatId`（必需）：会话 ID，用于多轮对话记忆

**请求示例**：

```javascript
import { doChatWithLoveAppSse } from '@/api/chat'

const chatId = 'chat-123'
const message = '你好，我最近睡眠不足，想咨询一下怎么调整'

const eventSource = doChatWithLoveAppSse(message, chatId)

eventSource.onmessage = (event) => {
  console.log('收到数据:', event.data)
  // 处理流式数据
}

eventSource.onerror = (error) => {
  console.error('连接错误:', error)
  eventSource.close()
}
```

**响应格式**：Server-Sent Events 流式数据

```
data: 你好
data: ，
data: 我是
data: 健康顾问
...
```

**使用示例**（LoveApp.vue）：

```javascript
const eventSource = doChatWithLoveAppSse(inputMessage.value, chatId.value)

eventSource.onmessage = (event) => {
  if (currentAiMessage.value) {
    currentAiMessage.value.content += event.data
  }
}

eventSource.onerror = () => {
  eventSource.close()
  isLoading.value = false
}
```

#### 2. AI 超级智能体 SSE 聊天

**接口地址**：`GET /api/ai/manus/chat`

**请求参数**：
- `message`（必需）：用户请求内容

**请求示例**：

```javascript
import { doChatWithManus } from '@/api/chat'

const message = '帮我搜索一下 Vue 3 的最新特性'

const eventSource = doChatWithManus(message)

eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data)
  console.log('思考:', data.thought)
  console.log('工具调用:', data.toolCalls)
  console.log('执行结果:', data.results)
}
```

**响应格式**：Server-Sent Events，包含 JSON 格式的执行过程数据

**使用示例**（ManusApp.vue）：

```javascript
const eventSource = doChatWithManus(inputMessage.value)

eventSource.onmessage = (event) => {
  try {
    const data = JSON.parse(event.data)
    // 处理 AI 思考、工具调用、执行结果等
  } catch (e) {
    // 处理纯文本响应
  }
}
```

### API 工具函数

#### `doChatWithLoveAppSse(message, chatId)`

创建健康顾问 SSE 连接。

**参数**：
- `message`：用户消息
- `chatId`：会话 ID

**返回**：`EventSource` 实例

#### `doChatWithManus(message)`

创建超级智能体 SSE 连接。

**参数**：
- `message`：用户请求

**返回**：`EventSource` 实例

## 部署说明

### Docker 部署

#### 1. 构建镜像

```bash
docker build -t ai-agent-frontend:latest .
```

#### 2. 运行容器

```bash
docker run -d \
  -p 80:80 \
  --name ai-agent-frontend \
  ai-agent-frontend:latest
```

#### 3. 自定义后端地址

修改 `nginx.conf` 中的 `proxy_pass` 配置，然后重新构建镜像。

### 手动部署

#### 1. 构建项目

```bash
npm run build
```

#### 2. 配置 Nginx

将 `dist/` 目录内容复制到 Nginx 静态文件目录，并配置 `nginx.conf`。

#### 3. 启动 Nginx

```bash
nginx -s reload
```

### 环境变量配置

#### 开发环境

创建 `.env` 文件：

```bash
VITE_API_BASE_URL=/api
VITE_API_TARGET=http://localhost:8123
```

#### 生产环境

创建 `.env.production` 文件：

```bash
VITE_API_BASE_URL=/api
```

并在 `nginx.conf` 中配置后端代理地址。

## 开发指南

### 添加新页面

1. **创建页面组件**：在 `src/views/` 目录下创建新的 Vue 组件
2. **配置路由**：在 `src/router/index.js` 中添加路由配置
3. **添加导航**：在 `src/App.vue` 中添加导航链接（如需要）

### 添加新 API

1. **定义 API 函数**：在 `src/api/chat.js` 中添加新的 API 函数
2. **在组件中使用**：在页面组件中导入并使用

示例：

```javascript
// src/api/chat.js
export function doNewApiCall(params) {
  const url = `${API_BASE}/new/endpoint?${new URLSearchParams(params)}`
  return new EventSource(url)
}

// src/views/NewPage.vue
import { doNewApiCall } from '@/api/chat'
```

### 自定义样式

项目使用内联样式（`<style>` 标签），可以直接在组件中修改样式。

主要样式类：
- `.chat-page`：聊天页面容器
- `.chat-header`：页面头部
- `.chat-body`：消息区域
- `.chat-messages`：消息列表
- `.message-row`：单条消息行
- `.bubble`：消息气泡
- `.chat-input-area`：输入区域

### 调试技巧

1. **查看网络请求**：浏览器开发者工具 → Network → 查看 SSE 连接
2. **查看控制台日志**：组件中有 `console.log` 输出
3. **Vite 热重载**：修改代码后自动刷新
4. **Vue DevTools**：安装 Vue DevTools 浏览器扩展

## 常见问题

### 1. SSE 连接失败

**问题**：无法建立 SSE 连接

**解决方案**：
- 检查后端服务是否运行
- 检查代理配置是否正确
- 检查 CORS 配置
- 查看浏览器控制台错误信息

### 2. 消息不显示

**问题**：发送消息后没有显示

**解决方案**：
- 检查 `messages` 数组是否正确更新
- 检查 `isLoading` 状态
- 检查 EventSource 事件监听是否正确

### 3. 代理不生效

**问题**：开发环境 API 请求失败

**解决方案**：
- 检查 `vite.config.js` 中的代理配置
- 确认后端服务地址正确
- 重启开发服务器

### 4. 生产环境路由 404

**问题**：刷新页面后出现 404

**解决方案**：
- 确保 Nginx 配置了 `try_files $uri $uri/ /index.html;`
- 检查 `nginx.conf` 配置是否正确

### 5. 样式显示异常

**问题**：页面样式不正确

**解决方案**：
- 检查浏览器兼容性
- 清除浏览器缓存
- 检查 CSS 是否正确加载

## 注意事项

1. **后端服务依赖**：确保后端服务运行在 `http://localhost:8123`（开发环境）
2. **CORS 配置**：后端需要配置 CORS 允许前端域名
3. **SSE 连接管理**：组件卸载时会自动关闭 SSE 连接，避免内存泄漏
4. **会话 ID 管理**：每个会话使用独立的 `chatId`，确保多轮对话记忆正确
5. **生产环境配置**：部署前务必修改 `nginx.conf` 中的后端地址

## 许可证

本项目仅用于学习与演示。

## 贡献

欢迎提交 Issue 和 Pull Request。
