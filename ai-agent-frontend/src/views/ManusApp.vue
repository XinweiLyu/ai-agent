<template>
  <div class="chat-page manus-app">
    <div class="chat-header">
      <div class="chat-header-left">
        <div class="chat-app-badge manus">超级智能体</div>
        <div class="chat-app-desc">复杂任务拆解 · 多步骤执行 · 实时进度反馈</div>
      </div>
      <div class="chat-header-right">
        <span class="chat-id-label">模式</span>
        <span class="chat-id-value">多步骤工作流</span>
      </div>
    </div>

    <div class="chat-body">
      <div class="chat-messages" ref="messagesRef">
        <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message-row', msg.type]"
        >
          <template v-if="msg.type === 'ai'">
            <div class="avatar-wrapper">
              <div class="avatar ai">
                <span>⚙</span>
              </div>
            </div>
            <div class="bubble-wrapper ai">
              <div class="bubble">
                <div class="bubble-text">
                  {{ msg.content }}
                </div>
                <div class="bubble-meta">
                  <span class="role-label">执行步骤</span>
                  <span class="time">{{ msg.time }}</span>
                </div>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="bubble-wrapper user">
              <div class="bubble">
                <div class="bubble-text">
                  {{ msg.content }}
                </div>
                <div class="bubble-meta">
                  <span class="role-label">我</span>
                  <span class="time">{{ msg.time }}</span>
                </div>
              </div>
            </div>
            <div class="avatar-wrapper">
              <div class="avatar user">
                <span>U</span>
              </div>
            </div>
          </template>
        </div>

        <div v-if="isLoading" class="message-row ai">
          <div class="avatar-wrapper">
            <div class="avatar ai">
              <span>⚙</span>
            </div>
          </div>
          <div class="bubble-wrapper ai">
            <div class="bubble">
              <div class="bubble-text typing">正在规划并执行任务步骤...</div>
            </div>
          </div>
        </div>
      </div>

      <div class="chat-input-area">
        <div class="input-inner">
          <textarea
            v-model="inputMessage"
            @keyup.enter.exact.prevent="sendMessage"
            @keyup.enter.shift.stop
            :disabled="isLoading"
            class="input-box"
            placeholder="描述一个你想完成的复杂任务，例如生成报告、拆解学习计划、整理项目需求等...（Shift+Enter 换行）"
          />
          <button
            @click="sendMessage"
            :disabled="isLoading || !inputMessage.trim()"
            class="send-btn"
          >
            发送
          </button>
        </div>
        <div class="input-hint">
          <span>🧩 提示：可以直接贴需求文档 / 任务清单，智能体会自动拆成多个步骤输出。</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { doChatWithManus } from '../api/chat'

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesRef = ref(null)
let eventSource = null

// 格式化时间
const formatTime = () => {
  const now = new Date()
  return now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim() || isLoading.value) return
  
  const userMessage = inputMessage.value.trim()
  inputMessage.value = ''
  
  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: userMessage,
    time: formatTime()
  })
  
  scrollToBottom()
  // 进入“思考中”状态，直到收到第一条步骤消息
  isLoading.value = true
  
  // 关闭之前的连接
  if (eventSource) {
    eventSource.close()
  }
  
  // 创建SSE连接
  eventSource = doChatWithManus(userMessage)

  eventSource.onmessage = (event) => {
    // 只要有数据流入，就说明后端已经开始响应，此时可以关闭“思考中”状态
    isLoading.value = false
    if (event.data) {
      // 如果后端约定了结束标记（例如 [DONE]），在此处判断并关闭连接
      if (event.data === '[DONE]') {
        eventSource.close()
        return
      }

      // 每个步骤单独使用一个 AI 气泡展示（后端每条 SSE 为一个步骤）
      messages.value.push({
        type: 'ai',
        content: event.data,
        time: formatTime()
      })
      scrollToBottom()
    }
  }
  
  eventSource.onerror = (error) => {
    console.error('SSE错误:', error)
    isLoading.value = false
    eventSource.close()
    
    // 如果AI消息为空，添加错误提示
    const hasAiMsg = messages.value.some(m => m.type === 'ai')
    if (!hasAiMsg) {
      messages.value.push({
        type: 'ai',
        content: '抱歉，发生了错误，请重试。',
        time: formatTime()
      })
    }
  }
  
  eventSource.addEventListener('end', () => {
    isLoading.value = false
    eventSource.close()
  })
}

// 返回主页
const goHome = () => {
  if (eventSource) {
    eventSource.close()
  }
  router.push('/')
}

onUnmounted(() => {
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<style scoped>
.chat-page {
  width: 100%;
  max-width: 1120px;
  margin: 0 auto;
  height: calc(100vh - 64px - 72px);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: center;
}

.chat-header-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.chat-app-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 11px;
  color: #f9fafb;
  width: fit-content;
}

.chat-app-badge.manus {
  background: linear-gradient(135deg, #38bdf8, #6366f1);
}

.chat-app-desc {
  font-size: 0.85rem;
  color: #9ca3af;
}

.chat-header-right {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 9px;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.8);
  border: 1px solid rgba(55, 65, 81, 0.9);
  font-size: 0.75rem;
  color: #9ca3af;
}

.chat-id-label {
  opacity: 0.8;
}

.chat-id-value {
  font-family: 'JetBrains Mono', ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas,
    'Liberation Mono', 'Courier New', monospace;
  color: #e5e7eb;
}

.chat-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: 1px solid rgba(55, 65, 81, 0.9);
  background: radial-gradient(circle at top left, rgba(15, 23, 42, 0.98), #020617);
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 14px 16px 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message-row {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  animation: fadeIn 0.25s ease-out;
  min-width: 0; /* 允许行收缩 */
  width: 100%; /* 确保占满容器宽度 */
}

.message-row.ai {
  justify-content: flex-start;
}

.message-row.user {
  justify-content: flex-end;
}

.avatar-wrapper {
  width: 32px;
  display: flex;
  justify-content: center;
  margin-top: 2px;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  color: #f9fafb;
}

.avatar.ai {
  background: linear-gradient(135deg, #38bdf8, #6366f1);
}

.avatar.user {
  background: linear-gradient(135deg, #4f46e5, #22c55e);
  font-size: 13px;
}

.bubble-wrapper {
  max-width: 100%;
  min-width: 0; /* 允许 flex 子元素收缩 */
}

.bubble-wrapper.ai {
  flex: 0 1 auto; /* 允许收缩，但不增长 */
  max-width: min(100%, 720px);
}

.bubble-wrapper.user {
  flex: 0 1 auto; /* 允许收缩，但不增长 */
  max-width: min(100%, 720px);
}

.bubble {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 8px 10px;
  border-radius: 10px;
  border: 1px solid rgba(55, 65, 81, 0.9);
  width: 100%;
  min-width: 0; /* 允许内容收缩 */
  box-sizing: border-box; /* 确保 padding 包含在宽度内 */
}

.bubble-wrapper.user .bubble {
  background: radial-gradient(circle at top left, rgba(30, 64, 175, 0.85), rgba(15, 23, 42, 0.95));
  border-color: rgba(129, 140, 248, 0.95);
}

.bubble-wrapper.ai .bubble {
  background: rgba(15, 23, 42, 0.95);
}

.bubble-text {
  font-size: 0.9rem;
  color: #e5e7eb;
  line-height: 1.6;
  white-space: pre-wrap; /* 保留换行和空格，允许自动换行 */
  word-wrap: break-word; /* 长单词换行 */
  word-break: break-word; /* 允许在任意字符间换行 */
  overflow-wrap: break-word; /* 现代浏览器的换行属性 */
  text-align: left;
  min-width: 0; /* 允许文本收缩 */
  max-width: 100%; /* 确保不超过父容器 */
}

.bubble-meta {
  display: flex;
  gap: 6px;
  align-items: center;
  font-size: 0.7rem;
  color: #6b7280;
}

.role-label {
  text-transform: none;
}

.time {
  opacity: 0.8;
}

.typing {
  display: inline-block;
}

.typing::after {
  content: '...';
  animation: dots 1.5s steps(4, end) infinite;
}

.chat-input-area {
  border-top: 1px solid rgba(31, 41, 55, 0.95);
  padding: 8px 10px 10px;
  background: radial-gradient(circle at top, rgba(15, 23, 42, 0.98), rgba(2, 6, 23, 1));
}

.input-inner {
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.input-box {
  flex: 1;
  padding: 8px 10px;
  border-radius: 10px;
  border: 1px solid rgba(55, 65, 81, 0.95);
  background: rgba(15, 23, 42, 0.95);
  color: #e5e7eb;
  font-size: 0.9rem;
  font-family: inherit;
  resize: none;
  min-height: 40px;
  max-height: 120px;
  line-height: 1.5;
}

.input-box:focus {
  outline: none;
  border-color: rgba(129, 140, 248, 0.9);
  box-shadow: 0 0 0 1px rgba(129, 140, 248, 0.5);
}

.input-box:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.send-btn {
  padding: 8px 14px;
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #4f46e5, #22c55e);
  color: #f9fafb;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  box-shadow: 0 8px 18px rgba(79, 70, 229, 0.75);
  transition: transform 0.15s ease, box-shadow 0.15s ease, opacity 0.15s ease;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 12px 26px rgba(79, 70, 229, 0.85);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  box-shadow: none;
}

.input-hint {
  margin-top: 4px;
  font-size: 0.75rem;
  color: #6b7280;
}

@keyframes dots {
  0%,
  20% {
    content: '.';
  }
  40% {
    content: '..';
  }
  60%,
  100% {
    content: '...';
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .chat-page {
    height: calc(100vh - 56px - 74px);
  }

  .chat-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .chat-header-right {
    margin-top: 4px;
  }

  .chat-body {
    border-radius: 12px;
  }

  .bubble-wrapper.ai,
  .bubble-wrapper.user {
    max-width: 100%; /* 移动端占满宽度 */
  }

  .bubble {
    max-width: 100%; /* 确保气泡不超过容器 */
  }

  .bubble-text {
    font-size: 0.85rem; /* 移动端稍微缩小字体 */
  }
}
</style>
