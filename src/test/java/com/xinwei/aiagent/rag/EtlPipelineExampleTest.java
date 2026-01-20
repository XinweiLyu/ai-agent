package com.xinwei.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ETL流程示例测试类
 */
@SpringBootTest(properties = "app.pgvector.enabled=false")
class EtlPipelineExampleTest {

    @Resource
    private EtlPipelineExample etlPipelineExample;

    @Test
    void testExecuteEtlPipeline() {
        etlPipelineExample.executeEtlPipeline();
    }
}
