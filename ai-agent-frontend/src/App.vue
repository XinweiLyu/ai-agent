<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="logo" @click="goHome">
        <span class="logo-mark">&gt;_</span>
        <span class="logo-text">AI Agent Hub</span>
      </div>
      <nav class="nav-links">
        <button class="nav-link" @click="goHome">应用中心</button>
        <button class="nav-link" @click="goLove">健康顾问</button>
        <button class="nav-link" @click="goManus">超级智能体</button>
      </nav>
    </header>

    <main class="app-main">
      <router-view />
    </main>

    <footer class="app-footer">
      <div class="footer-main">
        <div class="footer-left">
          <div class="footer-title">AI Agent 应用中心</div>
          <div class="footer-desc">
            专注打造高体验的 AI 工具集合，陪你一起探索更高效的学习与创作方式。
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>© {{ year }} AI Agent. All rights reserved.</span>
        <span class="footer-divider">·</span>
        <span>本项目仅用于学习与演示，数据由本地后端服务提供。</span>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const year = computed(() => new Date().getFullYear())

const goHome = () => {
  if (route.path !== '/') {
    router.push('/')
  }
}

const goLove = () => {
  if (route.path !== '/love-app') {
    router.push('/love-app')
  }
}

const goManus = () => {
  if (route.path !== '/manus-app') {
    router.push('/manus-app')
  }
}
</script>

<style>
*,
*::before,
*::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue',
    Arial, sans-serif;
  background-color: #050816;
  color: #e5e7eb;
  -webkit-font-smoothing: antialiased;
}

#app {
  width: 100%;
  min-height: 100vh;
}

.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: radial-gradient(circle at top left, #1f2937 0, #020617 50%, #000 100%);
}

.app-header {
  position: sticky;
  top: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.15);
  background: rgba(15, 23, 42, 0.92);
  backdrop-filter: blur(16px);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-mark {
  font-family: 'JetBrains Mono', ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas,
    'Liberation Mono', 'Courier New', monospace;
  font-weight: 700;
  color: #22c55e;
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid rgba(34, 197, 94, 0.4);
  background: radial-gradient(circle at top left, rgba(34, 197, 94, 0.25), transparent);
  box-shadow: 0 0 12px rgba(34, 197, 94, 0.45);
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.04em;
  color: #e5e7eb;
}

.nav-links {
  display: flex;
  gap: 8px;
}

.nav-link {
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid rgba(148, 163, 184, 0.4);
  background: radial-gradient(circle at top left, rgba(30, 64, 175, 0.5), rgba(15, 23, 42, 0.9));
  color: #e5e7eb;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.nav-link:hover {
  border-color: rgba(129, 140, 248, 0.9);
  box-shadow: 0 0 14px rgba(79, 70, 229, 0.5);
  transform: translateY(-1px);
}

.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 16px;
}

.app-footer {
  padding: 16px 24px 20px;
  border-top: 1px solid rgba(148, 163, 184, 0.2);
  background: radial-gradient(circle at top, rgba(15, 23, 42, 0.9), #020617);
  color: #9ca3af;
  font-size: 12px;
}

.footer-main {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 8px;
}

.footer-left {
  max-width: 480px;
}

.footer-title {
  font-size: 14px;
  font-weight: 600;
  color: #e5e7eb;
  margin-bottom: 4px;
}

.footer-desc {
  font-size: 12px;
  color: #9ca3af;
}

.footer-right {
  display: flex;
  align-items: flex-end;
}

.footer-link {
  color: #a5b4fc;
  text-decoration: none;
  font-size: 12px;
}

.footer-link:hover {
  text-decoration: underline;
}

.footer-bottom {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  font-size: 11px;
  color: #6b7280;
}

.footer-divider {
  opacity: 0.6;
}

@media (max-width: 768px) {
  .app-header {
    padding-inline: 16px;
  }

  .logo-text {
    display: none;
  }

  .nav-links {
    gap: 4px;
  }

  .nav-link {
    padding-inline: 10px;
    font-size: 12px;
  }

  .app-main {
    padding: 12px;
  }

  .app-footer {
    padding-inline: 16px;
  }

  .footer-main {
    flex-direction: column;
    align-items: flex-start;
  }

  .footer-right {
    align-items: flex-start;
  }
}
</style>
