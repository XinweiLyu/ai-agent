package com.xinwei.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 向量存储查询测试类
 * 用于查看和验证向量存储中的数据
 */
@SpringBootTest(properties = "app.pgvector.enabled=false")
class VectorStoreQueryTest {

    @Resource
    private VectorStore loveAppVectorStore;

    /**
     * 测试：查询向量存储中的所有数据（通过相似度搜索）
     */
    @Test
    void testQueryAllDocuments() {
        // 使用一个通用查询词来获取所有相关文档
        // 由于 SimpleVectorStore 没有直接获取所有文档的方法，我们使用相似度搜索
        List<Document> results = loveAppVectorStore.similaritySearch(
            SearchRequest.builder()
                .query("恋爱")  // 使用文档主题相关的查询词
                .topK(100)      // 获取前100个结果（应该能覆盖所有文档）
                .build()
        );

        System.out.println("========== 向量存储中的数据 ==========");
        System.out.println("找到 " + results.size() + " 个文档片段");
        System.out.println();

        // 打印每个文档的详细信息
        for (int i = 0; i < results.size(); i++) {
            Document doc = results.get(i);
            System.out.println("--- 文档片段 " + (i + 1) + " ---");
            String content = doc.getText();
            System.out.println("内容: " + (content.length() > 200 ? content.substring(0, 200) + "..." : content));
            System.out.println("元数据: " + doc.getMetadata());
            System.out.println();
        }
    }

    /**
     * 测试：根据特定问题查询相关文档
     */
    @Test
    void testQueryByQuestion() {
        String query = "如何找到合适的对象";
        
        List<Document> results = loveAppVectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(5)  // 获取最相关的5个文档片段
                .build()
        );

        System.out.println("========== 查询问题: " + query + " ==========");
        System.out.println("找到 " + results.size() + " 个相关文档片段");
        System.out.println();

        for (int i = 0; i < results.size(); i++) {
            Document doc = results.get(i);
            System.out.println("【相关文档 " + (i + 1) + "】");
            System.out.println("内容: " + doc.getText());
            System.out.println("相似度元数据: " + doc.getMetadata());
            System.out.println();
        }
    }

    /**
     * 测试：查看向量存储的统计信息
     */
    @Test
    void testVectorStoreStatistics() {
        // 通过多次查询不同主题来估算文档数量
        String[] queries = {"单身", "恋爱", "已婚", "沟通", "关系"};
        
        System.out.println("========== 向量存储统计信息 ==========");
        
        for (String query : queries) {
            List<Document> results = loveAppVectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(query)
                    .topK(10)
                    .build()
            );
            System.out.println("查询 '" + query + "': 找到 " + results.size() + " 个相关文档");
        }
    }
}
