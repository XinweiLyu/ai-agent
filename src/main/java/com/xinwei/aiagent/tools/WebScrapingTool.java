package com.xinwei.aiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页抓取工具类，使用Jsoup库实现网页内容的抓取功能。
 */
public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.html(); // 返回整个 HTML 文档的字符串形式（包含所有标签）
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
