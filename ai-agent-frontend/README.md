# AI Agent 前端应用

基于 Vue3 的 AI 聊天应用前端项目。

## 功能特性

1. **主页**：应用切换中心，可以选择不同的 AI 应用
2. **AI 恋爱大师**：专业的恋爱咨询助手，支持实时流式对话
3. **AI 超级智能体**：强大的 AI 助手，处理各种复杂任务

## 技术栈

- Vue 3
- Vue Router 4
- Axios
- Vite

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

项目将在 `http://localhost:3000` 启动

### 3. 构建生产版本

```bash
npm run build
```

## 项目结构

```
ai-agent-frontend/
├── src/
│   ├── api/           # API 接口定义
│   │   └── chat.js    # 聊天相关接口
│   ├── router/        # 路由配置
│   │   └── index.js
│   ├── utils/         # 工具函数
│   │   └── request.js # Axios 配置
│   ├── views/         # 页面组件
│   │   ├── Home.vue   # 主页
│   │   ├── LoveApp.vue # AI 恋爱大师
│   │   └── ManusApp.vue # AI 超级智能体
│   ├── App.vue        # 根组件
│   └── main.js        # 入口文件
├── index.html
├── package.json
└── vite.config.js
```

## 后端接口

项目配置了代理，所有 `/api` 请求会被代理到 `http://localhost:8123`

### 接口列表

1. **AI 恋爱大师 SSE 聊天**
   - 路径：`GET /api/ai/love_app/chat/sse`
   - 参数：`message`（用户消息）、`chatId`（聊天室ID）

2. **AI 超级智能体 SSE 聊天**
   - 路径：`GET /api/ai/manus/chat`
   - 参数：`message`（用户消息）

## 注意事项

1. 确保后端服务运行在 `http://localhost:8123`
2. 后端需要支持 CORS 跨域请求
3. SSE 连接在组件卸载时会自动关闭
