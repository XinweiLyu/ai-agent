package com.xinwei.aiagent.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL流程示例：从原始文档到向量库的完整流程
 */
@Component
public class EtlPipelineExample {

    private final ResourcePatternResolver resourcePatternResolver;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public EtlPipelineExample(
            ResourcePatternResolver resourcePatternResolver,
            @Qualifier("dashscopeChatModel") ChatModel chatModel,
            VectorStore vectorStore) {
        this.resourcePatternResolver = resourcePatternResolver;
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    public void executeEtlPipeline() {
        // 抽取：从 Markdown 文件读取文档
        List<Document> documents = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, 
                    MarkdownDocumentReaderConfig.builder().build());
                documents.addAll(reader.get());
            }
        } catch (IOException e) {
            throw new RuntimeException("文档加载失败", e);
        }

        // 转换：分割文本并添加摘要
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitDocuments = splitter.apply(documents);

        SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(chatModel,
                List.of(SummaryType.CURRENT));
        List<Document> enrichedDocuments = enricher.apply(splitDocuments);

        // 加载：写入向量数据库
        vectorStore.write(enrichedDocuments);

        // 或者使用链式调用
        // vectorStore.write(enricher.apply(splitter.apply(documents)));
    }
}
