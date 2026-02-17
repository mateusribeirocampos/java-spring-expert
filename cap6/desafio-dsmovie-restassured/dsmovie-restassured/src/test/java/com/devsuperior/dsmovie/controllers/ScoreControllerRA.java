package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ScoreControllerRA {

	private Long existingMovieId, nonExistingMovieId;
	private Double score;
	private String adminToken;
	private String adminUsername, adminPassword;

	private Map<String, Object> putMovieInstance;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

		existingMovieId = 1L;
		nonExistingMovieId = 100L;
		score = 4.0;

		putMovieInstance = new HashMap<>();
		putMovieInstance.put("movieId", existingMovieId);
		putMovieInstance.put("score", score);
	}
		@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		putMovieInstance.put("movieId", nonExistingMovieId);
		JSONObject saveScore = new JSONObject(putMovieInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(saveScore)
				.when()
				.put("/scores")
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"))
				.body("status", equalTo(404));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putMovieInstance.put("movieId", null);
		JSONObject saveScore = new JSONObject(putMovieInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(saveScore)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"))
				.body("path", equalTo("/scores"))
				.body("status", equalTo(422))
				.body("errors.message[0]", equalTo("Campo requerido"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		putMovieInstance.put("score", -2.0);
		JSONObject saveScore = new JSONObject(putMovieInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(saveScore)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"))
				.body("path", equalTo("/scores"))
				.body("status", equalTo(422))
				.body("errors.message[0]", equalTo("Valor mínimo 0"));
	}
}
