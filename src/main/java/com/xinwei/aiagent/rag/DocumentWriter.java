package com.xinwei.aiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * 文档写入器接口和实现
 * 提供统一的文档写入接口，支持文件写入和向量存储写入
 */
public interface DocumentWriter extends Consumer<List<Document>> {
    default void write(List<Document> documents) {
        accept(documents);
    }
}

/**
 * 文件文档写入器
 * 将文档列表写入到指定的文件中
 */
@Component
class MyDocumentWriter {
    public void writeDocuments(List<Document> documents) {
        FileDocumentWriter writer = new FileDocumentWriter("tmp/document-vectorStore/output.txt", true, MetadataMode.ALL, false);
        writer.accept(documents);
    }
}

/**
 * 向量存储文档写入器
 * 将文档列表存储到向量数据库中
 */
@Component
class MyVectorStoreWriter {
    
    private final VectorStore vectorStore;
    
    /**
     * 构造函数注入向量存储
     * @param vectorStore 向量存储实例
     */
    MyVectorStoreWriter(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    
    /**
     * 将文档列表存储到向量数据库
     * @param documents 要存储的文档列表
     */
    public void storeDocuments(List<Document> documents) {
        vectorStore.accept(documents);
    }
    
    /**
     * 使用 add 方法存储文档（VectorStore 的标准方法）
     * @param documents 要存储的文档列表
     */
    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }
}
