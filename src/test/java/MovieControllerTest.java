import com.example.moviecatalogue.controller.MovieController;
import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.model.TMDbResponse;
import com.example.moviecatalogue.repository.FavouriteMovieRepository;
import com.example.moviecatalogue.service.TmdbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private TmdbService movieService;

    @Mock
    private FavouriteMovieRepository favoriteRepository;

    @InjectMocks
    private MovieController movieController;

    // dummy Movie.
    private Movie createDummyMovie(int id, String title) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        return movie;
    }

    // dummy TMDbResponse.
    private TMDbResponse createDummyResponse(Movie... movies) {
        TMDbResponse response = new TMDbResponse();
        response.setResults(Arrays.asList(movies));
        return response;
    }

    // instance before each test.
    private Model createModel() {
        return new ExtendedModelMap();
    }

    // / and /movie/popular
    @Test
    public void home_ShouldAddMoviesAndReturnMovieListView() {
        Movie movie1 = createDummyMovie(1, "Movie One");
        Movie movie2 = createDummyMovie(2, "Movie Two");
        TMDbResponse response = createDummyResponse(movie1, movie2);
        when(movieService.getPopularMovies()).thenReturn(response);

        Model model = createModel();
        String view = movieController.home(model);

        assertEquals("movie-list", view);
        assertEquals(response.getResults(), model.asMap().get("movies"));
        verify(movieService).getPopularMovies();
    }

    // /movie/{id}
    @Test
    public void movieDetails_ShouldAddMovieAndReturnMovieDetailsView() {
        int movieId = 10;
        Movie movie = createDummyMovie(movieId, "Detail Movie");
        when(movieService.getMovieDetails(movieId)).thenReturn(movie);

        Model model = createModel();
        String view = movieController.movieDetails(movieId, model);

        assertEquals("movie-details", view);
        assertEquals(movie, model.asMap().get("movie"));
        verify(movieService).getMovieDetails(movieId);
    }

    // "/search/movie"
    @Test
    public void searchMovies_ShouldAddMoviesAndReturnMovieListView() {
        String query = "action";
        Movie movie = createDummyMovie(20, "Action Movie");
        TMDbResponse response = createDummyResponse(movie);
        when(movieService.searchMovies(query)).thenReturn(response);

        Model model = createModel();
        String view = movieController.searchMovies(query, model);

        assertEquals("movie-list", view);
        assertEquals(response.getResults(), model.asMap().get("movies"));
        verify(movieService).searchMovies(query);
    }

    // /favorites/add
    @Test
    public void addFavorite_WhenMovieNotExists_ShouldSaveMovie() {
        int movieId = 30;
        Movie movie = createDummyMovie(movieId, "Favorite Movie");

        when(movieService.getMovieDetails(movieId)).thenReturn(movie);
        when(favoriteRepository.existsById(movieId)).thenReturn(false);

        String result = movieController.addFavorite(movieId);
        verify(movieService).getMovieDetails(movieId);
        verify(favoriteRepository).existsById(movieId);
        verify(favoriteRepository).save(movie);
    }


    @Test
    public void addFavorite_WhenMovieAlreadyExists_ShouldNotSaveMovie() {
        int movieId = 30;
        Movie movie = createDummyMovie(movieId, "Favorite Movie");

        when(movieService.getMovieDetails(movieId)).thenReturn(movie);
        // Simulate the movie is already in the favorites list.
        when(favoriteRepository.existsById(movieId)).thenReturn(true);

        String result = movieController.addFavorite(movieId);
        verify(movieService).getMovieDetails(movieId);
        verify(favoriteRepository).existsById(movieId);
        verify(favoriteRepository, never()).save(any(Movie.class));
    }

    // /favorites
    @Test
    public void favorites_ShouldAddFavoritesAndReturnFavoritesView() {
        Movie movie = createDummyMovie(40, "Favorite Listed");
        // Simulate repository returns a list with one movie.
        when(favoriteRepository.findAll()).thenReturn(Collections.singletonList(movie));

        Model model = createModel();
        String view = movieController.favorites(model);

        assertEquals(Collections.singletonList(movie), model.asMap().get("favorites"));
        verify(favoriteRepository).findAll();
    }

    // /favorites/remove
    @Test
    public void removeFavorite_ShouldDeleteMovieAndRedirectToFavorites() {
        int movieId = 50;
        // No need to simulate repository behaviour beyond expecting deleteById to be called.
        String view = movieController.removeFavorite(movieId);
        verify(favoriteRepository).deleteById(movieId);
    }
}
