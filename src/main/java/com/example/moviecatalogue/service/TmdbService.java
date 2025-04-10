package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public TMDbResponse getPopularMovies() {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/popular")
                .queryParam("api_key", apiKey)
                .toUriString();
        return restTemplate.getForObject(uri, TMDbResponse.class);
    }

    public TMDbResponse searchMovies(String query) {
        String url = String.format("%s/search/movie?api_key=%s&query=%s", baseUrl, apiKey, query);
        return restTemplate.getForObject(url, TMDbResponse.class);
    }

    public Movie getMovieDetails(int movieId) {
        String url = String.format("%s/movie/%d?api_key=%s", baseUrl, movieId, apiKey);
        return restTemplate.getForObject(url, Movie.class);
    }
}
