<template>
  <div class="home">
    <section class="hero">
      <div class="hero-left">
        <div class="hero-tag">AI Agent Hub · 极客工作台</div>
        <h1 class="hero-title">
          把多个
          <span class="highlight">AI 应用</span>
          收进一块面板，像写代码一样高效对话。
        </h1>
        <p class="hero-subtitle">
          健康咨询、复杂任务拆解、工具调用……在一个统一的极客风界面中完成，专注输入，享受输出。
        </p>
        <div class="hero-actions">
          <button class="primary-btn" @click="goToManusApp">
            立即体验超级智能体
          </button>
          <button class="ghost-btn" @click="goToLoveApp">
            和健康顾问聊一聊
          </button>
        </div>
        <div class="hero-meta">
          <span class="meta-dot"></span>
          <span>支持 PC / 平板 / 手机自适应布局</span>
        </div>
      </div>

      <div class="hero-right">
        <div class="terminal-card">
          <div class="terminal-header">
            <span class="dot red"></span>
            <span class="dot yellow"></span>
            <span class="dot green"></span>
            <span class="terminal-title">terminal · ai-agent</span>
          </div>
          <div class="terminal-body">
            <div class="terminal-line">
              <span class="prompt">&gt;_</span>
              <span class="code">npm run dev</span>
            </div>
            <div class="terminal-line dim">▶ 启动本地 AI Agent 前端服务...</div>
            <div class="terminal-divider"></div>
            <div class="terminal-line">
              <span class="prompt">user@you</span>
              <span class="code"> 想同时管理多个 AI 会话，有没有好用的面板？</span>
            </div>
            <div class="terminal-line ai">
              <span class="prompt">ai-agent</span>
              <span class="code">
                试试右侧的「AI 超级智能体」，为你串联工具与上下文，像工程师一样拆解任务。
              </span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="apps">
      <h2 class="apps-title">内置应用</h2>
      <p class="apps-subtitle">为不同场景准备的两个 AI 角色</p>

      <div class="app-grid">
        <div class="app-card" @click="goToLoveApp">
          <div class="app-card-header">
            <div class="app-icon love">💚</div>
            <div class="app-info">
              <h3 class="app-name">AI 健康顾问</h3>
              <p class="app-tagline">健康场景专用 · 更懂预防、治疗与康复</p>
            </div>
          </div>
          <p class="app-desc">
            帮你解读体检结果、梳理就医思路、优化生活方式，让健康管理更科学、更安心。
          </p>
          <div class="app-meta">
            <span>💬 多轮健康问答</span>
            <span>·</span>
            <span>🧠 结合个人情况给出建议</span>
          </div>
        </div>

        <div class="app-card" @click="goToManusApp">
          <div class="app-card-header">
            <div class="app-icon manus">⚙</div>
            <div class="app-info">
              <h3 class="app-name">AI 超级智能体</h3>
              <p class="app-tagline">任务拆解 · 工具调用 · 步骤化执行</p>
            </div>
          </div>
          <p class="app-desc">
            像调度一个小型工程团队：自动拆解复杂需求、逐步执行，并通过多个步骤气泡向你汇报进度。
          </p>
          <div class="app-meta">
            <span>📂 多步骤输出</span>
            <span>·</span>
            <span>🛠️ 适合复杂工作流</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLoveApp = () => {
  router.push('/love-app')
}

const goToManusApp = () => {
  router.push('/manus-app')
}
</script>

<style scoped>
.home {
  max-width: 1120px;
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.hero {
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(0, 1fr);
  gap: 32px;
  align-items: center;
}

.hero-left {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 999px;
  border: 1px solid rgba(148, 163, 184, 0.6);
  background: radial-gradient(circle at top left, rgba(51, 65, 85, 0.7), rgba(15, 23, 42, 0.95));
  font-size: 11px;
  color: #e5e7eb;
  width: fit-content;
}

.hero-title {
  font-size: 2.2rem;
  line-height: 1.3;
  font-weight: 700;
  color: #f9fafb;
}

.highlight {
  color: #a5b4fc;
  text-shadow: 0 0 18px rgba(129, 140, 248, 0.8);
}

.hero-subtitle {
  font-size: 0.95rem;
  color: #9ca3af;
  line-height: 1.7;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 8px;
}

.primary-btn {
  padding: 10px 16px;
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #4f46e5, #22c55e);
  color: white;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 10px 25px rgba(79, 70, 229, 0.6);
  transition: transform 0.15s ease, box-shadow 0.15s ease, opacity 0.15s ease;
}

.primary-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 32px rgba(79, 70, 229, 0.7);
}

.ghost-btn {
  padding: 10px 16px;
  border-radius: 999px;
  border: 1px solid rgba(148, 163, 184, 0.7);
  background: transparent;
  color: #e5e7eb;
  font-size: 0.9rem;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  transition: background 0.15s ease, border-color 0.15s ease;
}

.ghost-btn:hover {
  background: rgba(15, 23, 42, 0.8);
  border-color: rgba(129, 140, 248, 0.9);
}

.hero-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.8rem;
  color: #6b7280;
  margin-top: 4px;
}

.meta-dot {
  width: 6px;
  height: 6px;
  border-radius: 999px;
  background: #22c55e;
  box-shadow: 0 0 8px rgba(34, 197, 94, 0.8);
}

.hero-right {
  display: flex;
  justify-content: flex-end;
}

.terminal-card {
  width: 100%;
  max-width: 420px;
  border-radius: 16px;
  border: 1px solid rgba(55, 65, 81, 0.9);
  background: radial-gradient(circle at top left, #020617, #020617 40%, #0b1120);
  box-shadow: 0 18px 45px rgba(15, 23, 42, 0.95);
  overflow: hidden;
}

.terminal-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 10px;
  border-bottom: 1px solid rgba(31, 41, 55, 0.9);
  background: linear-gradient(to right, #020617, #111827);
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 999px;
}

.dot.red {
  background: #f87171;
}

.dot.yellow {
  background: #facc15;
}

.dot.green {
  background: #4ade80;
}

.terminal-title {
  margin-left: 4px;
  font-size: 0.75rem;
  color: #6b7280;
}

.terminal-body {
  padding: 10px 12px 12px;
  font-family: 'JetBrains Mono', ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas,
    'Liberation Mono', 'Courier New', monospace;
  font-size: 0.75rem;
  color: #e5e7eb;
}

.terminal-line {
  display: flex;
  gap: 6px;
  margin-bottom: 4px;
}

.prompt {
  color: #22c55e;
}

.code {
  color: #e5e7eb;
}

.terminal-line.dim .code,
.terminal-line.dim {
  color: #6b7280;
}

.terminal-line.ai .prompt {
  color: #60a5fa;
}

.terminal-divider {
  height: 1px;
  margin: 6px 0 8px;
  background: linear-gradient(to right, transparent, rgba(75, 85, 99, 0.9), transparent);
}

.apps {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.apps-title {
  font-size: 1.1rem;
  color: #e5e7eb;
  font-weight: 600;
}

.apps-subtitle {
  font-size: 0.9rem;
  color: #9ca3af;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 18px;
  margin-top: 8px;
}

.app-card {
  border-radius: 14px;
  border: 1px solid rgba(55, 65, 81, 0.9);
  background: radial-gradient(circle at top left, rgba(15, 23, 42, 0.95), #020617);
  padding: 14px 14px 12px;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}

.app-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.9);
  border-color: rgba(129, 140, 248, 0.9);
}

.app-card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.app-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #f9fafb;
}

.app-icon.love {
  background: linear-gradient(135deg, #10b981, #22c55e);
}

.app-icon.manus {
  background: linear-gradient(135deg, #38bdf8, #6366f1);
}

.app-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.app-name {
  font-size: 0.95rem;
  color: #e5e7eb;
  font-weight: 600;
}

.app-tagline {
  font-size: 0.8rem;
  color: #9ca3af;
}

.app-desc {
  font-size: 0.85rem;
  color: #9ca3af;
  line-height: 1.6;
  margin-bottom: 6px;
}

.app-meta {
  font-size: 0.78rem;
  color: #6b7280;
  display: flex;
  gap: 6px;
  align-items: center;
}

@media (max-width: 900px) {
  .hero {
    grid-template-columns: minmax(0, 1fr);
  }

  .hero-right {
    order: -1;
    justify-content: flex-start;
  }

  .terminal-card {
    max-width: 100%;
  }
}

@media (max-width: 640px) {
  .home {
    padding-inline: 4px;
  }

  .hero-title {
    font-size: 1.6rem;
  }

  .hero-subtitle {
    font-size: 0.9rem;
  }

  .primary-btn,
  .ghost-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
