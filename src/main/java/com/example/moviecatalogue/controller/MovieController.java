package com.example.moviecatalogue.controller;

import com.example.moviecatalogue.model.*;
import com.example.moviecatalogue.repository.FavouriteMovieRepository;
import com.example.moviecatalogue.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {
    @Autowired
    private TmdbService movieService;

    @Autowired
    private FavouriteMovieRepository favoriteRepository;

    // Home page
    @GetMapping({"/", "/movie/popular"})
    public String home(Model model) {
        TMDbResponse movies = movieService.getPopularMovies();
        model.addAttribute("movies", movies.getResults());
        return "movie-list";
    }

    // Detail page
    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.getMovieDetails(id);
        model.addAttribute("movie", movie);
        return "movie-details";
    }

    // Search for movies by title
    @GetMapping("/search/movie")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        TMDbResponse movies = movieService.searchMovies(query);
        model.addAttribute("movies", movies.getResults());
        return "movie-list";
    }

    // Add movie
    @PostMapping("/favorites/add")
    public String addFavorite(@RequestParam("id") int movieId) {
        Movie movie = movieService.getMovieDetails(movieId);
        if (!favoriteRepository.existsById(movieId)) {
            favoriteRepository.save(movie);
        }
        return "redirect:/favorites";
    }

    // List all favorite movies
    @GetMapping("/favorites")
    public String favorites(Model model) {
        model.addAttribute("favorites", favoriteRepository.findAll());
        return "favorites";
    }

    // Remove movie
    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam("id") int movieId) {
        favoriteRepository.deleteById(movieId);
        return "redirect:/favorites";
    }
}
