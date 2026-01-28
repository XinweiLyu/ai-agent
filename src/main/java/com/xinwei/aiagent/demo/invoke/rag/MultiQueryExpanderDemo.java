package com.xinwei.aiagent.demo.invoke.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多查询扩展器示例组件
 * 使用AI模型将单一查询扩展为多个相关查询，以提升检索覆盖面
 */
@Component
public class  MultiQueryExpanderDemo {

    private final ChatClient.Builder chatClientBuilder;

    // 初始化ChatClient.Builder
    public MultiQueryExpanderDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }
    // 多查询扩展器
    // eg : 输入“谁是程序员啊？”，扩展为多个查询
    // Query[text=谁是程序员啊？, history=[], context={}]
    // Query[text=程序员的职业定义与核心工作内容是什么？  , history=[], context={}]
    // Query[text=当代程序员的典型职业路径与技能要求有哪些？  , history=[], context={}]
    // Query[text=程序员在不同行业（如互联网、金融、制造业）中的角色与职责差异, history=[], context={}]
    public List<Query> expand(String query){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query("谁是程序员啊？"));
        return queries;
    }


}
