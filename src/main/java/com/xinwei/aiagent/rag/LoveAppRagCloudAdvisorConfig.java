package com.xinwei.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 2 自定义基于阿里云知识库服务的RAG增强顾问
 * https://bailian.console.aliyun.com/cn-beijing/?tab=app#/knowledge-base
 * 教程
 * https://java2ai.com/docs/1.0.0-M6.1/tutorials/retriever/#documentretriever
 */
@Configuration
@Slf4j
class LoveAppRagCloudAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        final String KNOWLEDGE_INDEX = "健康顾问"; // 云知识库名称
        // 创建 DashScopeDocumentRetriever, 配置索引名称
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder() // 构建检索器选项
                        .withIndexName(KNOWLEDGE_INDEX) // 从指定的知识库索引中检索
                        .build());
        // 创建并返回 RetrievalAugmentationAdvisor
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever) // 设置文档检索器
                .build();
    }
}
