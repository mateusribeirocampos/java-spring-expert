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
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private String movieTitle;
	private Long existingId, nonExistingId;
	private String adminToken, clientToken, invalidToken;
	private String clientUsername, adminUsername, clientPassword, adminPassword;

	private Map<String, Object> postMovieInstance;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "ztak";

		movieTitle = "Matrix";

		postMovieInstance = new HashMap<>();
		postMovieInstance.put("title", "Matrix Revolutions");
		postMovieInstance.put("score", 3.5);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://media.fstatic.com/l-dFvprp4Xmyz8cpSKaIhoXSvd4=/220x330/smart/filters:format(webp)/media/movies/covers/2013/10/matrix-revolutions_t661_1.jpg");

	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
				.get("/movies")
				.then()
				.body("content.title", hasItems("Venom: Tempo de Carnificina",
						"Star Wars: A Guerra dos Clones",
						"O Silêncio dos Inocentes"));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		given()
				.get("/movies?title={movieTitle}", movieTitle)
				.then()
				.statusCode(200)
				.body("content.id[0]", is(4))
				.body("content.title[0]", equalTo("Matrix Resurrections"))
				.body("content.score[0]", is(0.0f))
				.body("content.count[0]", is(0))
				.body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/hv7o3VgfsairBoQFAawgaQ4cR1m.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		existingId = 11L;

		given()
				.get("/movies/{id}", existingId)
				.then()
				.statusCode(200)
				.body("id", is(11))
				.body("title", equalTo("Star Wars: A Guerra dos Clones"))
				.body("score", is(0.0f))
				.body("count", is(0))
				.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/uK15I3sGd8AudO9z6J6vi0HH1UU.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		nonExistingId = 100L;

		given()
				.get("/movies/{id}", nonExistingId)
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"));
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		postMovieInstance.put("title", "  ");
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"))
				.body("errors.message", hasItems("Campo requerido"));
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);

		given()
				.header("content-type", "application/json")
				.header("Authorization", "Bearer " + invalidToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(401);
	}
}
