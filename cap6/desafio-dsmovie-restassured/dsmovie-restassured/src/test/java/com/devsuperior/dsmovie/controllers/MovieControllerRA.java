package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private String movieTitle;
	private Long existingId, nonExistingId;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		movieTitle = "Matrix";

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
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
