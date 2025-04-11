import com.example.moviecatalogue.model.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {
    @Test
    public void testGetterAndSetter() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setTitle("Inception");
        movie.setOverview("A mind-bending thriller");
        movie.setPoster_path("/inception.jpg");
        movie.setRelease_date("2010-07-16");
        movie.setVote_average(8.8);

        // Assert
        assertEquals(1, movie.getId(), "Movie id should be 1");
        assertEquals("Inception", movie.getTitle(), "Title should be 'Inception'");
        assertEquals("A mind-bending thriller", movie.getOverview(), "Overview does not match");
        assertEquals("/inception.jpg", movie.getPoster_path(), "Poster path does not match");
        assertEquals("2010-07-16", movie.getRelease_date(), "Release date does not match");
        assertEquals(8.8, movie.getVote_average(), "Vote average does not match");
    }
}
