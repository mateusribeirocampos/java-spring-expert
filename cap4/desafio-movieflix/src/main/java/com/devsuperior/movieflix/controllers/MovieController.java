package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('VISITOR','MEMBER')")
    public ResponseEntity<MovieDetailsDTO> findById(@Valid @PathVariable Long id) {
        MovieDetailsDTO movieDetailsDTO = movieService.findById(id);
        return ResponseEntity.ok().body(movieDetailsDTO);
    }

}
