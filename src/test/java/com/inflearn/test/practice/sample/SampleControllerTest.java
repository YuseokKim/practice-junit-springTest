package com.inflearn.test.practice.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) // 내장톰켓을 사용, TestRestTemplate, WebClient를 사용
@SpringBootTest // webEnvironment Defualt 값은 Mock, MockMvc를 사용
@AutoConfigureMockMvc
public class SampleControllerTest {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	SampleService mockSampleService;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired 
	WebTestClient webTestClient;
	 
	
	@Test
	public void mockMvcTest() throws Exception {
		mockMvc.perform(get("/hello"))
		.andExpect(status().isOk())
		.andExpect(content().string("hello yuseok"))
		.andDo(print());
		
	}
	
	@Test
	public void testRestTemplate() {
		String result = testRestTemplate.getForObject("/hello", String.class);
		assertThat(result).isEqualTo("hello yuseok");
	}
	
	@Test
	public void mockBeantest() {
		//SampleService를 mocking함 
		when(mockSampleService.getName()).thenReturn("kim");
		
		String result = testRestTemplate.getForObject("/hello", String.class);
		assertThat(result).isEqualTo("hello kim");
		
	}

	@Test
	public void webTestClient() {
		when(mockSampleService.getName()).thenReturn("kim");

		webTestClient.get().uri("/hello").exchange().expectStatus()
				.isOk().expectBody(String.class)
				.isEqualTo("hello kim");
	}
	 
}
