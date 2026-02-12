package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService scoreService;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	private Long existingMovieId, nonExistingMovieId;
	private ScoreEntity scoreEntity;
	private Double scoreValue;
	private MovieEntity movieEntity;
	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		scoreEntity = ScoreFactory.createScoreEntity();
		movieEntity = scoreEntity.getId().getMovie();

		scoreValue = ScoreFactory.scoreValue;
		scoreDTO = new ScoreDTO(existingMovieId, scoreValue);

		Mockito.when(userService.authenticated()).thenReturn(new UserEntity());
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);

		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = scoreService.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingMovieId);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		scoreDTO = new ScoreDTO(nonExistingMovieId, scoreValue);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			scoreService.saveScore(scoreDTO);
		});
	}
}
