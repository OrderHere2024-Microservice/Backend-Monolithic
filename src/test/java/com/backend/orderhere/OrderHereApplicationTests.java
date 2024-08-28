package com.backend.orderhere;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderHereApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void applicationStarts() {
		assertDoesNotThrow(() -> SpringApplication.run(OrderHereApplication.class));
	}

}
