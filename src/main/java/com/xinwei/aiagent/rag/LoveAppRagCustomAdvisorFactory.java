package com.xinwei.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/** 4
 *  RAG 自定义检索增强 Advisor 工厂类
 * 根据用户状态（预防、治疗、康复）创建对应的检索增强 Advisor
 */
@Slf4j
public class LoveAppRagCustomAdvisorFactory {

     /**
     *  创建自定义的 RAG 检索增强顾问
     *
     * @param vectorStore 向量存储
     * @param status      状态
     * @return 自定义的 RAG 检索增强顾问
     */
    public static Advisor createLoveAppRagCustomAdvisor(VectorStore vectorStore, String status) {
        // 构建过滤表达式，根据用户状态过滤文档。元信息字段：status
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        // 创建文档检索器，应用过滤条件。 spring 原生文档过滤器
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression) // 过滤条件
                .similarityThreshold(0.5) // 相似度阈值
                .topK(3) // 返回文档数量
                .build();
        // 创建并返回检索增强 Advisor. 该会默认使用上下文查询增强其
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // 自定义错误处理逻辑，当系统无法找到相关文档时，提示用户
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();

    }
}
