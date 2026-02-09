package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Optional<Movie> obj = movieRepository.findById(id);
        Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new MovieDetailsDTO(entity);
    }

    @Transactional
    public Page<MovieCardDTO> findByGenre(Long genreId, Pageable pageable) {
        Long filterGenreId;
        if (genreId != null && genreId == 0) {
            filterGenreId = null;
        } else {
            filterGenreId = genreId;
        }

        Page<MovieProjection> page = movieRepository.searchMovie(filterGenreId, pageable);

        List<Long> movieIds =  page.map(MovieProjection::getId).toList();

        List<Movie> movies = movieRepository.searchMovieWithGenre(movieIds);

        List<MovieCardDTO> dtos = movies.stream().map(x -> new MovieCardDTO(x)).toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }
}
