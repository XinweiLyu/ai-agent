package com.xinwei.aiagent.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DefaultContentFormatter;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * 转换（Transfer）
 * 文档处理示例：切分、元信息增强、格式化。
 * 将大文档合理差分为便于检索的知识碎片
 * 放在 rag/pipeline 包下，便于与 RAG 配置协同。
 */
public class DocumentProcessingComponents {

    /**
     * 文档转换器接口，接收/返回文档列表。
     */
    public interface DocumentTransformer extends Function<List<Document>, List<Document>> {
        default List<Document> transform(List<Document> documents) {
            return apply(documents);
        }
    }

    /**
     * 文本按 token 切分。考虑了语义边界（如句子结尾）来创建有意义的文本段落，低成本文本切割方式
     */
    @Component
    public static class MyTokenTextSplitter {

        public List<Document> splitDocuments(List<Document> documents) {
            TokenTextSplitter splitter = new TokenTextSplitter();
            return splitter.apply(documents);
        }

        public List<Document> splitCustomized(List<Document> documents) {
            TokenTextSplitter splitter = new TokenTextSplitter(
                    1000,
                    400,
                    10,
                    5000,
                    true);
            return splitter.apply(documents);
        }
    }

    /**
     * 文档元信息增强。作用是为文档补充更多的元信息，便于后续检索，而不是改变文档本身切分规则。
     */
    @Component
    public static class MyDocumentEnricher {

        private final ChatModel chatModel;

        public MyDocumentEnricher(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
            this.chatModel = chatModel;
        }

        /**
         * 关键词元信息增强。
         */
        public List<Document> enrichDocumentsByKeyword(List<Document> documents) {
            KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.chatModel, 5);
            return enricher.apply(documents);
        }

        /**
         * 摘要元信息增强。
         */
        public List<Document> enrichDocumentsBySummary(List<Document> documents) {
            SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(chatModel,
                    List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT));
            return enricher.apply(documents);
        }
    }

    /**
     * 文档格式化器：将元数据与正文拼装为最终字符串。
     * 1. 文档格式化：将文档的元数据和正文内容按照一定的模板进行拼接，生成适合模型输入的文本格式。
     * 2. 元数据过滤：在格式化过程中，可以选择性地排除某些元数据键值对，以避免无关信息干扰模型的理解。
     *  - ALL：包含所有元数据键值对。
     *  - INFERENCE：排除与推理无关的元数据键值对。
     *  - EMBED：排除与嵌入无关的元数据键值对。
     *  - None：不包含任何元数据。
     * 3. 模板自定义：支持自定义元数据和正文的拼接模板，灵活适应不同的应用场景和需求。
     */
    @Component
    public static class MyContentFormatter {

        private final DefaultContentFormatter formatter = DefaultContentFormatter.builder()
                .withMetadataTemplate("{key}: {value}")
                .withMetadataSeparator("\n")
                .withTextTemplate("{metadata_string}\n\n{content}")
                .withExcludedInferenceMetadataKeys("embedding", "vector_id")
                .withExcludedEmbedMetadataKeys("source_url", "timestamp")
                .build();

        public String formatForInference(Document document) {
            return formatter.format(document, MetadataMode.INFERENCE);
        }
    }
}
