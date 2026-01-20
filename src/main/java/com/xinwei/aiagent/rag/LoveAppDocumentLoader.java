package com.xinwei.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 恋爱大师应用的文档加载器
 */
@Component
@Slf4j
class LoveAppDocumentLoader {
    // 资源解析器，用于加载多个 Markdown 文件
    private final ResourcePatternResolver resourcePatternResolver;
    // 构造函数注入 ResourcePatternResolver
    LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载多篇markdown文件，自带切分文档功能
     * @return
     */
    public List<Document> loadMarkdowns() {
        List<Document> allDocuments = new ArrayList<>(); // 存储所有加载的文档
        try {
            // 这里可以修改为你要加载的多个 Markdown 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                // 配置 MarkdownDocumentReader
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .build();
                // 使用 MarkdownDocumentReader 读取文档,接收配置和资源
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载多篇JSON文件，基本用法
     * @return
     */
    public List<Document> loadJsons() {
        List<Document> allDocuments = new ArrayList<>(); // 存储所有加载的文档
        try {
            // 这里可以修改为你要加载的多个 JSON 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.json");
            for (Resource resource : resources) {
                // 使用 JsonReader 读取文档
                JsonReader reader = new JsonReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("JSON 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载多篇JSON文件，指定使用哪些 JSON 字段作为文档内容
     * @param fields 要提取的字段名称
     * @return
     */
    public List<Document> loadJsonsWithFields(String... fields) {
        List<Document> allDocuments = new ArrayList<>(); // 存储所有加载的文档
        try {
            // 这里可以修改为你要加载的多个 JSON 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.json");
            for (Resource resource : resources) {
                // 使用 JsonReader 读取文档,接收指定字段
                JsonReader reader = new JsonReader(resource, fields);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("JSON 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载多篇JSON文件，使用 JSON 指针精确提取文档内容
     * @param jsonPointer JSON 指针路径，例如 "/items"
     * @return
     */
    public List<Document> loadJsonsWithPointer(String jsonPointer) {
        List<Document> allDocuments = new ArrayList<>(); // 存储所有加载的文档
        try {
            // 这里可以修改为你要加载的多个 JSON 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.json");
            for (Resource resource : resources) {
                // 使用 JsonReader 读取文档,接收 JSON 指针
                JsonReader reader = new JsonReader(resource);
                allDocuments.addAll(reader.get(jsonPointer));
            }
        } catch (IOException e) {
            log.error("JSON 文档加载失败", e);
        }
        return allDocuments;
    }
    /**
     * 加载纯文本文件
     */
    public List<Document> loadTexts() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.txt");
            for (Resource resource : resources) {
                TextReader reader = new TextReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("文本文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载 PDF 文档 - 按页读取
     */
    public List<Document> loadPdfsByPage() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.pdf");
            for (Resource resource : resources) {
                PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("PDF 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载 PDF 文档 - 按段落读取
     */
    public List<Document> loadPdfsByParagraph() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.pdf");
            for (Resource resource : resources) {
                ParagraphPdfDocumentReader reader = new ParagraphPdfDocumentReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("PDF 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 加载 HTML 文档
     */
    // 用 Tika 读取 HTML
    public List<Document> loadHtmls() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.html");
            for (Resource resource : resources) {
                TikaDocumentReader reader = new TikaDocumentReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("HTML 文档加载失败", e);
        }
        return allDocuments;
    }

    /**
     * 使用 Tika 加载多种格式文档（更灵活）
     */
    public List<Document> loadDocumentsByTika(String pattern) {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            for (Resource resource : resources) {
                TikaDocumentReader reader = new TikaDocumentReader(resource);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Tika 文档加载失败", e);
        }
        return allDocuments;
    }
}
