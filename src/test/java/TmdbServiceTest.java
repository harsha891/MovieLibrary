import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.model.TMDbResponse;
import com.example.moviecatalogue.service.TmdbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class TmdbServiceTest {

    private TmdbService tmdbService;
    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate;

    // Dummy values for testing
    private final String apiKey = "dummyApiKey";
    private final String baseUrl = "http://dummybaseurl.com";

    @BeforeEach
    public void setup() {
        // Create instance of the service.
        tmdbService = new TmdbService();
        // Set the private fields using ReflectionTestUtils.
        ReflectionTestUtils.setField(tmdbService, "apiKey", apiKey);
        ReflectionTestUtils.setField(tmdbService, "baseUrl", baseUrl);

        // Retrieve the private RestTemplate instance from the service.
        restTemplate = (RestTemplate) ReflectionTestUtils.getField(tmdbService, "restTemplate");
        // Set up the MockRestServiceServer to intercept HTTP calls.
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetPopularMovies() {
        // Build the expected URI.
        String expectedUri = baseUrl + "/movie/popular?api_key=" + apiKey;
        // Prepare a dummy JSON response.
        String jsonResponse = "{\"page\":1,\"results\":[],\"total_pages\":10,\"total_results\":100}";

        // Configure the mock server to expect a GET request to expectedUri and return the jsonResponse.
        mockServer.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Execute the service method.
        TMDbResponse response = tmdbService.getPopularMovies();

        // Assert that the response values are correctly parsed.
        assertEquals(1, response.getPage(), "Page should be 1");
        assertEquals(10, response.getTotal_pages(), "Total pages should be 10");
        assertEquals(100, response.getTotal_results(), "Total results should be 100");

        // Verify that all expectations met.
        mockServer.verify();
    }

    @Test
    public void testSearchMovies() {
        String query = "Inception";
        // Format the URL as built in the service.
        String expectedUri = String.format("%s/search/movie?api_key=%s&query=%s", baseUrl, apiKey, query);
        String jsonResponse = "{\"page\":1,\"results\":[],\"total_pages\":5,\"total_results\":50}";

        mockServer.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        TMDbResponse response = tmdbService.searchMovies(query);

        assertEquals(1, response.getPage(), "Page should be 1");
        assertEquals(5, response.getTotal_pages(), "Total pages should be 5");
        assertEquals(50, response.getTotal_results(), "Total results should be 50");

        mockServer.verify();
    }

    @Test
    public void testGetMovieDetails() {
        int movieId = 42;
        // Build the expected URL: baseUrl/movie/42?api_key=dummyApiKey
        String expectedUri = String.format("%s/movie/%d?api_key=%s", baseUrl, movieId, apiKey);
        // Provide a dummy JSON response with minimal fields.
        String jsonResponse = "{\"id\":42,\"title\":\"The Answer\"}";

        mockServer.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Movie movie = tmdbService.getMovieDetails(movieId);

        assertEquals(42, movie.getId(), "Movie id should be 42");
        assertEquals("The Answer", movie.getTitle(), "Movie title should be 'The Answer'");

        mockServer.verify();
    }
}