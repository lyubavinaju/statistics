package socialNetwork;

import java.net.URI;
import java.util.Deque;

public interface SocialNetwork {
    URI getUriGetDates(String query, long startTime, long endTime);

    Deque<Long> extractDatesFromResponse(String response);

    Deque<Long> getDates(String query, long startTime, long endTime);
}
