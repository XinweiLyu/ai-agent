package com.xinwei.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
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
     * 测试：带阈值和元数据过滤的相似度搜索
     */
    @Test
    void testSearchWithThresholdAndFilter() {
        SearchRequest request = SearchRequest.builder()
            .query("什么是程序员鱼皮的编程导航学习网 codefather.cn？")
            .topK(5)
            .similarityThreshold(0.7)
            // 需要确保写入文档时 metadata 中包含 category/date 等键
            .filterExpression("category == 'web' AND date > '2025-05-03'")
            .build();

        List<Document> results = loveAppVectorStore.similaritySearch(request);

        System.out.println("========== 带阈值和过滤的查询 ==========");
        System.out.println("结果数量: " + results.size());

        for (int i = 0; i < results.size(); i++) {
            Document doc = results.get(i);
            System.out.println("【结果 " + (i + 1) + "】");
            System.out.println("内容: " + doc.getText());
            System.out.println("元数据: " + doc.getMetadata());
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

    /**
     * 示例：预检索与检索相关的 API 用法（示例代码，未实际执行）
     * 便于查阅 Spring AI RAG 相关组件使用方式。
     */
    @Test
    @Disabled("示例不参与执行")
    void preRetrievalAndRetrievalExamples() {
        /*
        // 1) 重写查询（Rewrite）
        Query query1 = new Query("啥是程序员啊啊啊啊？");
        QueryTransformer rewriteTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
        Query rewritten = rewriteTransformer.transform(query1);

        // 2) 翻译查询（Translation）
        Query query2 = new Query("hi, who is coder yupi? please answer me");
        QueryTransformer translationTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .targetLanguage("chinese")
                .build();
        Query translated = translationTransformer.transform(query2);

        // 3) 压缩查询（Compression，结合对话历史）
        Query query3 = Query.builder()
                .text("编程导航有啥内容？")
                .history(new UserMessage("谁是程序员？"),
                        new AssistantMessage("编程导航的创始人 codefather.cn"))
                .build();
        QueryTransformer compressionTransformer = CompressionQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
        Query compressed = compressionTransformer.transform(query3);

        // 4) 多路查询扩展（Multi-query）
        MultiQueryExpander expander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)       // 生成额外查询数
                .includeOriginal(true)    // 是否包含原始查询
                .build();
        List<Query> queries = expander.expand(new Query("啥是程序员？他会啥？"));

        // 5) 基于 VectorStore 的检索
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.7)
                .topK(5)
                .filterExpression(new FilterExpressionBuilder()
                        .eq("type", "web")
                        .build())
                .build();
        List<Document> documents = retriever.retrieve(new Query("谁是程序员"));

        // 6) 动态 FilterExpression（从 Query 上下文读取）
        Query queryWithContext = Query.builder()
                .text("谁是程序员？")
                .context(Map.of(VectorStoreDocumentRetriever.FILTER_EXPRESSION, "type == 'boy'"))
                .build();
        List<Document> documents2 = retriever.retrieve(queryWithContext);

        // 7) 文档合并（拼接多个查询的结果集）
        Map<Query, List<List<Document>>> documentsForQuery = ...; // 来自检索结果
        DocumentJoiner joiner = new ConcatenationDocumentJoiner();
        List<Document> joined = joiner.join(documentsForQuery);
        */
    }

    /**
     * 示例：顾问/检索增强链路（均为演示用途，未执行）
     */
    @Test
    @Disabled("示例不参与执行")
    void advisorAndRetrievalChainExamples() {
        /*
        // 1) 最简单的问答顾问，自动做向量检索
        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user("什么是编程导航？")
                .call()
                .chatResponse();

        // 2) 配置检索阈值 / topK
        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.8d).topK(6).build())
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(qaAdvisor)
                .build();

        // 3) 运行时动态修改过滤表达式
        String content = chatClient.prompt()
                .user("看着我的眼睛，回答我！")
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == 'web'"))
                .call()
                .content();

        // 4) 自定义 prompt 模板
        QuestionAnswerAdvisor qaAdvisorWithPrompt = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(customPromptTemplate)
                .build();

        // 5) 更通用的 RetrievalAugmentationAdvisor，组合检索和可选的查询改写
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        String answer = chatClient.prompt()
                .advisors(retrievalAugmentationAdvisor)
                .user("什么是编程导航？")
                .call()
                .content();

        // 6) 加入 Query 改写，再做检索
        Advisor retrievalWithRewrite = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.build().mutate())
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        // 7) 用上下文增强查询（允许空上下文）
        Advisor retrievalWithContextAugment = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();

        // 8) 自定义上下文增强模板
        QueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                .promptTemplate(customPromptTemplate)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
        */
    }
}
