package com.xinwei.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.ai.embedding.EmbeddingModel;

import java.util.List;

/**
 * 本地，恋爱大师向量数据库配置（初始化基于内存的向量数据库bean）
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    // 引入自定义文本切分器
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource MyKeywordEnricher myKeywordEnricher;
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        // 自主切分
        //List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
        // 自动补充关键词原信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }
}
