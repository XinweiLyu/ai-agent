package com.xinwei.aiagent.rag;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文查询增强器工厂类
 * 用于创建专门处理恋爱相关查询的上下文查询增强器实例
 */
public class LoveAppContextualQueryAugmenterFactory {
    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                有问题可以联系编程导航客服 https://codefather.cn
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
