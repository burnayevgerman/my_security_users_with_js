package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class SpringBootSecurityDemoApplicationTests {

	@Test
	void contextLoads() {
		Integer[] nums = {1, 2, 3, 4, 5, 65};
		Arrays.<Integer>sort(nums, (x, y) -> (int)y - (int)x);
	}

}
