import request from '../utils/request'

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
  const url = `/api/ai/love_app/chat/sse?${params.toString()}`
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
  const url = `/api/ai/manus/chat?${params.toString()}`
  return new EventSource(url)
}
