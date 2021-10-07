package socialNetwork;

import http.UriReader;

import java.net.URI;
import java.util.Deque;

public abstract class AbstractSocialNetwork implements SocialNetwork {
    UriReader uriReader;

    public AbstractSocialNetwork(UriReader uriReader) {
        this.uriReader = uriReader;
    }

    @Override
    public Deque<Long> getDates(String query, long startTime, long endTime) {
        URI uri = getUriGetDates(query, startTime, endTime);
        String response = uriReader.getResponseToString(uri);
        Deque<Long> dates = extractDatesFromResponse(response);
        return dates;
    }
}
