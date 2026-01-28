package com.xinwei.aiagent.demo.invoke.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;



@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expand() {
        List<Query> queries =multiQueryExpanderDemo.expand("谁是程序员啊？请回答我啊啊撒啊啊啊");
        Assertions.assertNotNull(queries);
    }
}