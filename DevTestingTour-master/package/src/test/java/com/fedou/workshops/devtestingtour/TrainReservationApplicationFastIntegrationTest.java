package com.fedou.workshops.devtestingtour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedou.workshops.devtestingtour.infrastructure.traindata.TrainDataInMemoryPersistenceTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(locations = "classpath:application-integrationtests.properties")
public class TrainReservationApplicationFastIntegrationTest {

	@Autowired
	protected MockMvc mvc;
	@Autowired
	protected ObjectMapper mapper;

	protected <T> T getResponseContentAs(MvcResult result, @SuppressWarnings("SameParameterValue") Class<T> valueType) throws java.io.IOException {
		return mapper.readValue(result.getResponse().getContentAsString(), valueType);
	}

	protected String getRequestObjectAsJson(Object requestObject) throws JsonProcessingException {
		return mapper.writeValueAsString(requestObject);
	}

	@Autowired
	protected TrainDataInMemoryPersistenceTestUtils trainDataInMemory;


}
