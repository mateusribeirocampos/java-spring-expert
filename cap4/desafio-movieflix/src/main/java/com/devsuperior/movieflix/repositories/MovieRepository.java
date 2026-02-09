package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre WHERE obj.id =:id")
    Optional<Movie> findByIdWithGenre(Long id);

    @Query(nativeQuery = true, value = """
        SELECT tb_movie.id, tb_movie.title
        FROM tb_movie
        WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
        ORDER BY tb_movie.title""",
        countQuery = """
        SELECT COUNT(*)
        FROM tb_movie
        WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
        """)
    Page<MovieProjection> searchMovie(Long genreId, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre " +
            "WHERE obj.id IN :movieIds ORDER BY obj.title")
    List<Movie> searchMovieWithGenre(List<Long> movieIds);

}
