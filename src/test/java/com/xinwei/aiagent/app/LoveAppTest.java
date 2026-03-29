package com.xinwei.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// 本地rag测试时禁用PgVector配置
@SpringBootTest(properties = "app.pgvector.enabled=false")
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我最近工作压力大，想咨询一些健康问题";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想了解如何预防颈椎病，因为经常久坐办公";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "刚才我提到的工作压力问题，你能给我一些缓解建议吗？";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChat() {
    }
    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "我最近经常失眠，想了解如何改善睡眠质量，请给我一份详细的健康建议报告";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我想了解如何通过饮食和运动来预防高血压？";
        String answer =  loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("最近有什么新的健康养生方法？帮我搜索一下最新的健康资讯");

        // 测试网页抓取：恋爱案例分析
        testMessage("帮我抓取一下健康网站（如丁香园）上关于糖尿病预防的最新文章内容");

        // 测试资源下载：图片下载
        testMessage("下载一张健康饮食金字塔的图片作为参考");

        // 测试终端操作：执行代码
        //testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的健康咨询记录为文件，包括症状描述和你的建议");

        // 测试 PDF 生成
        testMessage("生成一份'个人健康管理计划'PDF，包含饮食建议、运动方案和作息安排");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
//        String message = "我住在北京朝阳区，请帮我找到附近5公里内的三甲医院";
//        String answer =  loveApp.doChatWithMcp(message, chatId);
//
        // 测试图片搜索 MCP
        String message = "帮我搜索一些关于健康饮食和营养搭配的图片";
        String answer =  loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);

    }



}
