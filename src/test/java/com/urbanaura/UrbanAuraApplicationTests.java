package com.urbanaura;

import com.urbanaura.urbanaura.repository.AqiLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UrbanAuraApplicationTests {

	@MockBean
	private AqiLogRepository aqiLogRepository;

	@MockBean
	private MongoTemplate mongoTemplate;

	@MockBean
	private ChatModel chatModel;

	@Test
	void contextLoads() {
	}

}
