package com.xinwei.aiagent.rag;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 自定义错误处理逻辑，当系统无法找到相关文档时
 * 创建上下文查询增强器工厂类
 */
public class LoveAppContextualQueryAugmenterFactory {
    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答健康相关的问题，别的没办法帮到您哦！
                """);
        return ContextualQueryAugmenter.builder()
                // false系统在没有相关上下文时，会 默认改写userText->
                // “The user query is outside your knowledge base.
                //  Politely inform the user that you can't answer it.”
                // true 会抛出异常
                .allowEmptyContext(false)
                // 设置当没有相关上下文时的提示模板
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }
}
