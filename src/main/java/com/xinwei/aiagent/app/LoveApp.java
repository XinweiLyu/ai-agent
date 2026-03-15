package com.xinwei.aiagent.app;

import com.xinwei.aiagent.advisor.MyLoggerAdvisor;
import com.xinwei.aiagent.advisor.ReReadingAdvisor;
import com.xinwei.aiagent.chatmemory.FileBasedChatMemory;
import com.xinwei.aiagent.rag.LoveAppRagCustomAdvisorFactory;
import com.xinwei.aiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "扮演专业的健康顾问。" +
    "行为准则：" +
    "1. 如果这是与用户的第一次对话（对话历史为空），请简短介绍你的身份：'你好，我是你的健康顾问，可以为你提供健康相关的咨询服务。'然后询问用户需要什么帮助。" +
    "2. 如果对话历史中已有交流记录，直接回答用户的问题，不要重复介绍身份。" +
    "3. 围绕预防、治疗、康复三种状态提供咨询：预防状态关注日常保健、运动健身、营养饮食及健康管理；" +
    "   治疗状态关注症状分析、就医建议、用药指导及疾病管理；康复状态关注恢复计划、运动康复、心理调适及生活调整。" +
    "4. 引导用户详述身体状况、症状表现、生活习惯及健康目标，以便给出专业的健康建议。" +
    "重要提示：本服务仅供参考，不能替代专业医疗诊断，严重症状请及时就医。";

    /**
     * 初始化ChatClient
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {

        // 1.  初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir")+"/tmp/chat-memories";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 2. 初始化基于内存的对话记忆
        //ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 添加自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 添加自定义 Re2 Advisor，可按需开启,增强推理能力
                        // ,new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话（支持多轮记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 应用接口
     * AI 基础对话（支持多轮记忆, SSE 流式传输）
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }



    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * AI 报告功能（结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成健康结果，标题为{用户名}的健康报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    //AI 知识库问答功能（RAG）
    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppQuestionAnswerAdvisor;

//    @Resource
//    private VectorStore pgVectorVectorStore;

    @Resource
    private QueryRewriter queryRewriter;


    /**
     * Rag 知识库进行对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {

        // 查询重写
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                //.user(message)
                .user(rewrittenMessage) //使用改写后的
                // 设置对话记忆 最后10条
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 1.应用 RAG 问答（基于本地知识库）
                //     ├─ 将查询转换为向量
                //     ├─ 在向量存储中搜索相似文档（余弦相似度）
                //     ├─ 返回 Top-K 相关文档
                //     └─ 将文档作为上下文注入到 AI 提示词
               .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 2.应用 RAG 检索增强服务（ 基于云知识库服务）
                //.advisors(loveAppRagCloudAdvisor)
                // 3. 应用RAG 检索增强服务（基于PgVector云向量存储）
                //.advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 4. 应用自定义 RAG 检索增强服务（文档查询器+上下文增强）
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
//                                loveAppVectorStore, "预防"
//                        )
//                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI 工具调用功能
    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI 调用MCP 服务
//     @Resource /
    @Autowired(required = false) // 改为非必传，避免启动报错
    private ToolCallbackProvider toolCallbackProvider; // 将所有mcp服务相关工具整合到ToolCallbackProvider
    /**
     * 调用 MCP 服务进行对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(String message, String chatId) {
        if (toolCallbackProvider == null) {
            log.warn("MCP服务未配置，无法使用MCP功能");
            return "MCP服务未配置，请先配置MCP客户端后再使用此功能";
        }
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


}
