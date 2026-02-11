package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService movieService;

	@Mock
	private MovieRepository movieRepository;

	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private Long existingId, nonExistingId, dependentId;
	private PageImpl<MovieEntity> page;
	private String movieTitle;

	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;

		movieEntity = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movieEntity);

		movieTitle = "Matrix Revolution";

		page = new PageImpl<>(List.of(movieEntity));

		Mockito.when(movieRepository.searchByTitle(any(), any(Pageable.class))).thenReturn(page);

		Mockito.when(movieRepository.findById(existingId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);

		Mockito.when(movieRepository.getReferenceById(existingId)).thenReturn(movieEntity);
		Mockito.when(movieRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(movieRepository.existsById(existingId)).thenReturn(true);
		Mockito.when(movieRepository.existsById(dependentId)).thenReturn(true);
		Mockito.when(movieRepository.existsById(nonExistingId)).thenReturn(false);

		Mockito.doNothing().when(movieRepository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(movieRepository).deleteById(dependentId);
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

		MovieDTO result = movieService.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = movieService.findById(nonExistingId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {

		MovieDTO result = movieService.insert(movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), movieEntity.getTitle());
		Assertions.assertEquals(result.getScore(), movieEntity.getScore());
		Assertions.assertEquals(result.getImage(), movieEntity.getImage());
		Assertions.assertEquals(result.getCount(), movieEntity.getCount());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {

		MovieDTO result = movieService.update(existingId, movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), movieEntity.getTitle());
		Assertions.assertEquals(result.getScore(), movieEntity.getScore());
		Assertions.assertEquals(result.getImage(), movieEntity.getImage());
		Assertions.assertEquals(result.getCount(), movieEntity.getCount());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = movieService.update(nonExistingId, movieDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			movieService.delete(existingId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			movieService.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			movieService.delete(dependentId);
		});
	}
}
