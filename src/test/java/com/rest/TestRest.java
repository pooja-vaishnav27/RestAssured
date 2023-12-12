package com.rest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Epic-01")
@Feature("Basic API Automation")

public class TestRest{
	
	@BeforeClass
	public void beforeClass() {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
				setBaseUri("https://api.publicapis.org").
				//addHeader("X-API-Key", "PMAK-65523acce6e34a00316ff562-b16e73a7ef763ec0caa2b49a184a1565ed").
				setContentType(ContentType.JSON).
				log(LogDetail.ALL);
		RestAssured.requestSpecification = requestSpecBuilder.build();
		
		
		ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
				//expectContentType(ContentType.JSON).
				log(LogDetail.ALL);
		RestAssured.responseSpecification = responseSpecBuilder.build();
		
			}
	
	
	@Story("Story-01")
	@Test
	@Description("GET API")
	@Severity(SeverityLevel.NORMAL)
	public void validate_response() {
		given().filter(new AllureRestAssured()).
		when().
			get("/entries").
		then().
			log().body().
			assertThat().
			statusCode(200).body("entries.Category", hasItems("Animals"),
					"entries.API[0]", is(equalTo("AdoptAPet")));
	} 
			

	@Story("Story-01")
	@Test
	@Description("GET API")
	@Severity(SeverityLevel.NORMAL)
	public void validate_response_using_hamcrest() {
		String name1 = given().filter(new AllureRestAssured()).
				when().
				get("/entries").
				then().
				log().all().
				assertThat().
				statusCode(200).
				extract().
				response().path("entries.API[2]");
		
				assertThat(name1 , equalTo("Cat Facts"));
	}
	
	@Story("Story-01")
	@Test
	@Description("GET API")
	@Severity(SeverityLevel.NORMAL)
	public void extract_resp() {
		Response resp = given().filter(new AllureRestAssured()).
		log().headers().
	when().
		get("/entries").
	then().
		log().ifValidationFails().
		assertThat().
		statusCode(200).
		extract().response();
		
		System.out.println("Response is " +resp.asString());
	}
	
	@Story("Story-01")
	@Test
	@Description("GET API")
	@Severity(SeverityLevel.NORMAL)
	public void extract_single_resp() {
			Response name = given().filter(new AllureRestAssured()).
			when().
			get("/entries").
			then().
			log().all().
			assertThat().
			statusCode(200).
			extract().
			response(); 
			
			JsonPath jsonpath = new JsonPath(name.asString());
			System.out.println("entries" +jsonpath.getString("entries.Description[0]"));		
	}
	
	@Story("Story-01")
	@Test
	@Description("POST API")
	@Severity(SeverityLevel.NORMAL)
	public void validate_post_request() {
		String payload = "{\r\n"
				+ "  \"webhook_url\": \"https://api.example.com/webhooks/listener-for-payment-gateway\",\r\n"
				+ "  \"events\": [\"payment.created\", \"payment.updated\", \"transaction.created\", \"transaction.updated\", \"refund.created\", \"refund.updated\"]\r\n"
				+ "}";
		given().filter(new AllureRestAssured()).
		baseUri("https://fcde83ce-ae2a-4165-bf7f-c2bb5f7fe762.mock.pstmn.io").
		body(payload).
		when().
		post("/webhooks").
		then().assertThat().
		statusCode(200).
		body("events", hasItem("payment.created"));
				
	}
	
	@Story("Story-01")
	@Test
	@Description("PUT API")
	@Severity(SeverityLevel.NORMAL)
	public void validate_put_request() {
	String payload1 = "{\r\n"
			+ "  \"url\": \"https://api.example.com/webhooks/new-listener-for-payment-gateway\",\r\n"
			+ "  \"events\": [\"payment.created\", \"refund.created\"]\r\n"
			+ "}";
	given().filter(new AllureRestAssured()).
	baseUri("https://fcde83ce-ae2a-4165-bf7f-c2bb5f7fe762.mock.pstmn.io").
	body(payload1).
	when().
	put("/webhooks/W123456").
	then().
	assertThat().
	statusCode(200);		
	}
}
	

	
