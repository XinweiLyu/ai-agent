import request from '../utils/request'

// 获取 API 基础路径
const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api'

/**
 * AI恋爱大师 SSE 聊天
 * @param {string} message - 用户消息
 * @param {string} chatId - 聊天室ID
 * @returns {EventSource} EventSource实例
 */
export function doChatWithLoveAppSse(message, chatId) {
  const params = new URLSearchParams({
    message: message,
    chatId: chatId
  })
  const url = `${API_BASE}/ai/love_app/chat/sse?${params.toString()}`
  return new EventSource(url)
}

/**
 * AI超级智能体 SSE 聊天
 * @param {string} message - 用户消息
 * @returns {EventSource} EventSource实例
 */
export function doChatWithManus(message) {
  const params = new URLSearchParams({
    message: message
  })
  const url = `${API_BASE}/ai/manus/chat?${params.toString()}`
  return new EventSource(url)
}
