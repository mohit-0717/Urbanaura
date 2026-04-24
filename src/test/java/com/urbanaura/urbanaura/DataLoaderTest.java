package com.urbanaura.urbanaura;

import com.urbanaura.urbanaura.repository.AqiLogRepository;
import com.urbanaura.urbanaura.service.DataLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DataLoaderTest {

    @Autowired
    private DataLoaderService dataLoaderService;

    @MockBean
    private AqiLogRepository aqiLogRepository;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private ChatModel chatModel;

    @Test
    public void testInsertion() {
        dataLoaderService.loadAllData();
    }
}
