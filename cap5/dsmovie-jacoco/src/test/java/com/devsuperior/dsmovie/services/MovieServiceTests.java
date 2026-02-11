package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.tests.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService movieService;

	@Mock
	private MovieRepository movieRepository;

	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private Long existingId, nonExistingId;
	private PageImpl<MovieEntity> page;
	private String movieTitle;

	@BeforeEach
	void setUp() throws Exception {

		movieEntity = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movieEntity);

		movieTitle = "Matrix Revolution";

		page = new PageImpl<>(List.of(movieEntity));

		Mockito.when(movieRepository.searchByTitle(any(), any(Pageable.class))).thenReturn(page);


	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		Pageable pageable = PageRequest.of(0,12);

		Page<MovieDTO> result = movieService.findAll(movieTitle, pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1L, result.getTotalElements());
		Assertions.assertEquals(result.iterator().next().getTitle(), movieEntity.getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
